package org.codelibs.elasticsearch.minhash;

public class SherlokException extends RuntimeException {

    private static final long serialVersionUID = -556052564224933360L;

    public SherlokException(final String message) {
        super(message);
    }

    public SherlokException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
