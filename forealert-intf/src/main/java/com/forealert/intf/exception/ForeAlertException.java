package com.forealert.intf.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/22/17
 * Time: 6:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class ForeAlertException extends RuntimeException{

    private Logger logger = LoggerFactory.getLogger(ForeAlertException.class);

    private static Integer DEFAULT_ERROR_CODE = 500;

    private int code;

    public ForeAlertException(String s, Throwable throwable, int code) {
        super(s, throwable);
        this.code = code;
    }

    public ForeAlertException(String s, int code) {
        super(s);
        this.code = code;
    }

    public ForeAlertException(String s) {
        super(s);
        this.code = DEFAULT_ERROR_CODE;
    }

    public int getCode() {
        return code;
    }

}
