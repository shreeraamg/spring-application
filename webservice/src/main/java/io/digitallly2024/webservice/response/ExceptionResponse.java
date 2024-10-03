package io.digitallly2024.webservice.response;

public class ExceptionResponse {

    private String errorMessage;
    private String timeStamp;

    public ExceptionResponse() {
    }

    public ExceptionResponse(String errorMessage, String timeStamp) {
        this.errorMessage = errorMessage;
        this.timeStamp = timeStamp;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

}
