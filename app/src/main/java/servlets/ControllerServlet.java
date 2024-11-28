package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Deque;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingDeque;

import encoders.DataEncoder;
import encoders.JsonEncoder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import structs.HitResult;

@WebServlet("/controller")
public class ControllerServlet extends HttpServlet {

    static DataEncoder encoder = new JsonEncoder();

    private static final String DEFAULT_TYPE = "application/json";
    
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        String accept = Objects.requireNonNullElse(req.getHeader("Accept"), DEFAULT_TYPE);

        if ("*/*".equals(accept)) {
            accept = DEFAULT_TYPE;
        }

        if (!"text/html".equals(accept) && !"application/json".equals(accept)) {
            resp.sendError(400, "Can accept only text/html or application/json. Request accept header was: " + accept);
            return;
        }

        Object context = req.getServletContext().getAttribute("globalHitHistoryDeque");

        assert(context.getClass() == LinkedBlockingDeque.class);
        @SuppressWarnings("unchecked")
        Deque<HitResult> hitData = (LinkedBlockingDeque<HitResult>)context;

        String table = encoder.getEncodedHitTable(accept, hitData.stream());

        try (PrintWriter writer = resp.getWriter()) {
            resp.setContentType(accept);
            writer.println(table);
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/area-check").forward(req, resp);
    }
}
