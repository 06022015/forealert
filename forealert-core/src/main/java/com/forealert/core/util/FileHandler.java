package com.forealert.core.util;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.Upload;
import com.forealert.intf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 8/3/17
 * Time: 10:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileHandler {

    private static Logger logger = LoggerFactory.getLogger(FileHandler.class);

    public static String uploadFileToS3UsingTM(InputStream content, String fileName) {
        if(StringUtil.isNotBlank(fileName) && null != content) {
            PutObjectRequest request = new PutObjectRequest(ForeAlertS3Client.INSTANCE.getS3Bucket(), "images/"+fileName, content, null);
            request.setCannedAcl(CannedAccessControlList.PublicRead);
            Upload upload = ForeAlertS3Client.INSTANCE.getTransferManager().upload(request) ;
            URL url = ForeAlertS3Client.INSTANCE.getS3client().getUrl(ForeAlertS3Client.INSTANCE.getS3Bucket(), "images/");
            try {
                // Or you can block and wait for the upload to finish
                upload.waitForCompletion();
                logger.debug("Upload complete.");
            } catch (AmazonClientException acex) {
                logger.error("Unable to upload file, upload was aborted.");
                acex.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
            return url+fileName;
        }
        return null;
    }

    public static String uploadOverlayCoordinates(String overlayCoordinates, String id){
        if(StringUtil.isNotBlank(overlayCoordinates)) {
            byte[] fileContentBytes = overlayCoordinates
                    .getBytes(StandardCharsets.UTF_8);
            InputStream fileInputStream = new ByteArrayInputStream(
                    fileContentBytes);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("application/json");
            metadata.setContentLength(fileContentBytes.length);
            PutObjectRequest request = new PutObjectRequest(ForeAlertS3Client.INSTANCE.getS3Bucket(),
                    "images/" + id, fileInputStream, metadata);
            request.setCannedAcl(CannedAccessControlList.PublicRead);
            Upload upload = ForeAlertS3Client.INSTANCE.getTransferManager().upload(request);
            URL url = ForeAlertS3Client.INSTANCE.getS3client().getUrl(ForeAlertS3Client.INSTANCE.getS3Bucket(), "images/");
            try {
                // Or you can block and wait for the upload to finish
                upload.waitForCompletion();
               logger.debug("Overlay Upload complete.");
            } catch (AmazonClientException acex) {
                logger.error("Unable to upload overlay file, upload was aborted.");
                acex.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
            return url + id;
        }
        return null;
    }
}
