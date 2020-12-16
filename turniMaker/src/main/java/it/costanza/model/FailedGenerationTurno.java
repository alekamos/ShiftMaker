package it.costanza.model;

public class FailedGenerationTurno extends Throwable {

    private String message;

    public FailedGenerationTurno(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
