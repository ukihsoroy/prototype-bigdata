package io.github.ukihsoroy.bigdata.component.security.exception;

public class SecurityException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String securityMessage;

    public SecurityException(String message) {
        super(message);
    }

    public SecurityException(String message, String securityMessage) {
        this(message);
        this.securityMessage = securityMessage;
    }

    public SecurityException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public SecurityException(String message, String securityMessage, Throwable throwable) {
        this(message, throwable);
        this.securityMessage = securityMessage;
    }

    public String getSecurityMessage() {
        return this.securityMessage;
    }
}
