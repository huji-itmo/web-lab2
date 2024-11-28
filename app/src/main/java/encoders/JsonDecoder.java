package encoders;

import java.io.Reader;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import exceptions.RequestBodyIsEmpty;

// import encoders.SmartDouble;


import structs.RequestData;

public class JsonDecoder implements DataDecoder {
    public static Gson gson = null;

    public RequestData getDecodedRequestData(String contentType, Reader reader) throws RequestBodyIsEmpty {
        if (gson == null) {
            gson = new GsonBuilder()
                .registerTypeAdapter(Double.class, new SmartDouble())
                .setPrettyPrinting()
                .create();
        }

        String type = Objects.requireNonNullElse(contentType, "application/json") ;

        if (!"application/json".equals(type)) {
            throw new UnsupportedOperationException("This app only accepts JSON");
        }

        RequestData data = gson.fromJson(reader, RequestData.class);
        if (data == null) {
            throw new RequestBodyIsEmpty("The request body is empty. Expected JSON with fields x,y,r");
        }
        
        return data;
    }

    // public String getEncodedHitTable(String acceptFormat, Stream<HitData> hitHistory) {

    //     switch (acceptFormat) {
    //         case "text/html":
    //             String html = hitHistory
    //             .map(data -> data.toHTMLTable())
    //             .collect(Collectors.joining("\n"));

    //             return html;
    //         default:
    //             AtomicInteger counter = new AtomicInteger(0);

    //             String json = hitHistory
    //                 .map(data -> "\"%d\": {%s}\n".formatted(counter.getAndIncrement(), data.toJsonFields()))
    //                 .collect(Collectors.joining(", ", "{", "}"));

    //             return json;
    //     }
    // }
}
