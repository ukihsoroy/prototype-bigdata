package io.github.ukihsoroy.bigdata.component.security.sm;

public class EncoderException extends IllegalStateException {
    private final Throwable cause;

    EncoderException(String msg, Throwable cause) {
        super(msg);
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return this.cause;
    }
}
