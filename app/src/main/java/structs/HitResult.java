package structs;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import exceptions.BadParameterException;
import exceptions.MissingParametersException;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HitResult implements Serializable {
    RequestData requestData;

    boolean hit;

    String durationMilliSeconds;

    String serverTime;

    public String toJsonFields() {
        return String.format(Locale.US, """
            "x": "%.1f", "y": "%.3f", "r": "%d", "hit":%s, "duration_milliseconds": "%s", "server_time": "%s"
            """,requestData.x, requestData.y, requestData.r, hit ? "true" : "false", durationMilliSeconds, serverTime);
    }

    public String toHTMLTable() {
        return String.format(Locale.US, """
        <tr>
            <td>%.1f</td>
            <td>%.3f</td> 
            <td>%d</td> 
            <td>%s</td> 
            <td>%s</td> 
            <td>%s</td>
        </tr>
            """,requestData.x, requestData.y, requestData.r, hit ? "true" : "false", durationMilliSeconds, serverTime);
    }

    public HitResult(RequestData data, boolean hit, long durationNanoSeconds) {
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedDateCustom = ZonedDateTime.now().format(customFormatter);

        this.serverTime = formattedDateCustom;
        this.requestData = data;
        this.hit = hit;
        this.durationMilliSeconds = "%.3f ms".formatted((double)durationNanoSeconds / 1000000D);
    }

    public static HitResult createNewHitData(RequestData data, long startTime) throws MissingParametersException, BadParameterException {
        boolean hitResult = CoordinateSpace.testHit(data);

        long durationNanoSeconds = System.nanoTime() - startTime;

        HitResult hitData = new HitResult(data, hitResult, durationNanoSeconds);

        return hitData;
    }
}
