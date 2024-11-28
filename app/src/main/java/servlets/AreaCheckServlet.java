package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingDeque;

import encoders.DataDecoder;
import encoders.DataEncoder;
import encoders.JsonDecoder;
import encoders.JsonEncoder;
import exceptions.BadParameterException;
import exceptions.MissingParametersException;
import exceptions.RequestBodyIsEmpty;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import structs.HitResult;
import structs.RequestData;



@WebServlet("/area-check")
public class AreaCheckServlet extends HttpServlet {

    private DataDecoder decoder = new JsonDecoder();
    private DataEncoder encoder = new JsonEncoder();

    public static final String DEFAULT_TYPE = "application/json";

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    //after redirect, http method should be GET
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        String accept = Objects.requireNonNullElse(req.getHeader("Accept"), DEFAULT_TYPE) ;
        
        if ("*/*".equals(accept)) {
            accept = DEFAULT_TYPE;
        }

        if (!"application/json".equals(accept) && !"text/html".equals(accept)) {
            resp.sendError(400, "Accept can only be application/json and text/html");
            return;
        }

        HitResult result = null;
        try {
            result = getHitResult(req);
            addHitResultToTable(getServletContext(), result);
        } catch(Exception e) {
            resp.sendError(400, e.getMessage());
            return;
        }

        try (PrintWriter writer =  resp.getWriter()) {
            resp.setContentType(accept);
            writer.print(encoder.getEncodedHitResult(accept, result));
        }
    }

    public HitResult getHitResult(HttpServletRequest req) throws IOException, RequestBodyIsEmpty, MissingParametersException, BadParameterException {
        long startTime = System.nanoTime();

        RequestData data = decoder.getDecodedRequestData(req.getContentType(), req.getReader());
        data.throwIfBadData();
        HitResult hitResult = HitResult.createNewHitData(data, startTime);
        
        return hitResult;
    }

    public void addHitResultToTable(ServletContext context, HitResult result) {
        Object contextObject = context.getAttribute("globalHitHistoryDeque");
        assert (contextObject.getClass() == LinkedBlockingDeque.class);
        @SuppressWarnings("unchecked")
        LinkedBlockingDeque<HitResult> hitResults = (LinkedBlockingDeque<HitResult>)contextObject;

        hitResults.addLast(result);
    }
}
