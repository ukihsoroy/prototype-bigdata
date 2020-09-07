package io.github.ukihsoroy.bigdata.component.security.sm;

public class DecoderException extends IllegalStateException {
    private final Throwable cause;

    DecoderException(String msg, Throwable cause) {
        super(msg);
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return this.cause;
    }
}
