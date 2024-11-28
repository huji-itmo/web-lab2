package encoders;

import java.io.Reader;

import exceptions.RequestBodyIsEmpty;
import structs.RequestData;

public interface DataDecoder {
    RequestData getDecodedRequestData(String contentType, Reader reader) throws RequestBodyIsEmpty;
}