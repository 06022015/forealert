package com.forealert.intf.entity;

import com.couchbase.client.java.repository.annotation.Field;
import com.forealert.intf.entity.type.FileType;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.xml.bind.annotation.XmlRootElement;
import java.nio.ByteBuffer;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/26/17
 * Time: 7:31 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "group_attachment")
@Document(expiry = 0)
public class GroupAttachmentEntity extends Base{

    public static String TYPE = "GroupAttachment";

    @Field
    private String groupId;
    @Field
    private FileType fileType;
    @Field
    private ByteBuffer content;
    @Field
    private String title;
    @Field
    private String description;
    @Field
    private String URL;
    @Field
    private String iconURL;
    @Field
    private String uploadedBy;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public ByteBuffer getContent() {
        return content;
    }

    public void setContent(ByteBuffer content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    @Override
    public String getTypeKey() {
        return TYPE;  
    }
}
