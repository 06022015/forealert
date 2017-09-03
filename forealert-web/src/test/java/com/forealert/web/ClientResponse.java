package com.forealert.web;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 8/13/17
 * Time: 4:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientResponse {
    
    
    private int code;
    private String content;

    public ClientResponse(int code, String content) {
        this.code = code;
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
