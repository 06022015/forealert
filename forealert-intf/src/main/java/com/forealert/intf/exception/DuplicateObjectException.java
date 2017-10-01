package com.forealert.intf.exception;

import com.forealert.intf.Constant;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/23/17
 * Time: 12:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class DuplicateObjectException extends ForeAlertException{

    public DuplicateObjectException(String s, Throwable throwable) {
        super(s, throwable, Constant.CONFLICT);
    }

    public DuplicateObjectException(String s) {
        super(s, Constant.CONFLICT);
    }
}
