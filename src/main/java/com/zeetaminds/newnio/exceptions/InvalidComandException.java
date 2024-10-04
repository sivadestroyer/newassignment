package com.zeetaminds.newnio.exceptions;

public class InvalidComandException extends Exception{
    private static final String DEFAULT_MESSAGE="Invalid or unsupported command exception";
    private static final String ERROR_CODE="550";
    public InvalidComandException(String message) {
        super(message);
    }


}
