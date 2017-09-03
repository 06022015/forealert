package com.forealert.intf.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/23/17
 * Time: 1:03 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "message")
public class MessageDTO {
   
    private String attributes;
    private List<FileDTO> files;

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public List<FileDTO> getFiles() {
        return files;
    }

    public void setFiles(List<FileDTO> files) {
        this.files = files;
    }

    public void addFile(FileDTO file){
        if(null == getFiles())
            setFiles(new ArrayList<FileDTO>());
        getFiles().add(file);
    }
}
