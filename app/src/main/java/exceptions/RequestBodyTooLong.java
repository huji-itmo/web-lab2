package exceptions;

public class RequestBodyTooLong extends BadRequestBody {
    public RequestBodyTooLong(String s) {
        super(s);
    }
}
