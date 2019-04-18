package itglue.interview.mortgage;

public class RequestInvalidException extends Throwable {
    private String message;
    public RequestInvalidException(String reason) {
        this.message=reason;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
