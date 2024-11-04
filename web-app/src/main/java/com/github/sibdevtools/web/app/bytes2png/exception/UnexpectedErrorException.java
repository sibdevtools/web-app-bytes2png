package com.github.sibdevtools.web.app.bytes2png.exception;

import com.github.sibdevtools.error.exception.ServiceException;
import com.github.sibdevtools.web.app.bytes2png.constant.Constants;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public class UnexpectedErrorException extends ServiceException {

    /**
     * Construct an unexpected error exception.
     *
     * @param systemMessage system message
     */
    public UnexpectedErrorException(String systemMessage) {
        super(Constants.ERROR_SOURCE, "UNEXPECTED_ERROR", systemMessage);
    }

    /**
     * Construct an unexpected error exception with cause.
     *
     * @param systemMessage system message
     * @param cause         cause
     */
    public UnexpectedErrorException(String systemMessage, Throwable cause) {
        super(Constants.ERROR_SOURCE, "UNEXPECTED_ERROR", systemMessage, cause);
    }
}
