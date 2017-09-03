package com.forealert.intf.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 8/4/17
 * Time: 11:22 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "status")
public class ForeAlertStatus {
    
    private int code = 200;
    private String message;
    private List<String> errors;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
    
    public void addError(String error){
        if(null == getErrors())
            setErrors(new ArrayList<String>());
        getErrors().add(error);
    }

    public boolean hasError(){
        return null != errors && errors.size()>0;
    }
}
