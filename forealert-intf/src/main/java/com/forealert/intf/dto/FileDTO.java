package com.forealert.intf.dto;

import com.forealert.intf.entity.type.FileType;

import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 8/4/17
 * Time: 9:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class FileDTO {

    private String name;
    private InputStream content;
    private String fileType;

    public FileDTO(String name, InputStream content, String fileType) {
        this.name = name;
        this.content = content;
        this.fileType = fileType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InputStream getContent() {
        return content;
    }

    public void setContent(InputStream content) {
        this.content = content;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
