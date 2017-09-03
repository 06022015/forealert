package com.forealert.intf.exception;

import com.forealert.intf.Constant;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/23/17
 * Time: 12:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class NoRecordFoundException extends ForeAlertException{

    public NoRecordFoundException(String s, Throwable throwable) {
        super(s, throwable, Constant.NO_RECORD_FOUND);
    }

    public NoRecordFoundException(String s) {
        super(s, Constant.NO_RECORD_FOUND);
    }
}
