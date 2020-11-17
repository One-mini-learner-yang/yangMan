package com.yang.crowd.exception;
/**
 * 表示用户没有登录就访问受保护资源
 */
public class AccessForBiddenException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public  AccessForBiddenException() {
    }

    public AccessForBiddenException(String message) {
        super(message);
    }

    public AccessForBiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessForBiddenException(Throwable cause) {
        super(cause);
    }

    public AccessForBiddenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
