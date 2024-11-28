package encoders;

import java.util.stream.Stream;

import structs.HitResult;

public interface DataEncoder {
    String getEncodedHitResult(String acceptFormat, HitResult data);

    String getEncodedHitTable(String acceptFormat, Stream<HitResult> hitHistory);    
}
