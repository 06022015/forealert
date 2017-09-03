package com.forealert.core.util;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 8/3/17
 * Time: 10:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class ForeAlertS3Client {

    public static ForeAlertS3Client INSTANCE = null;

    private String s3Bucket;
    private AmazonDynamoDB dynamoDB;
    private DynamoDBMapper mapper;
    private AmazonS3 s3client;
    private TransferManager transferManager;
    private Regions regions;

    private ForeAlertS3Client() {
    }

    private ForeAlertS3Client(String bucketName, String key, String secretKey, String region) {
        this.regions = Regions.fromName(region);
        this.s3Bucket = bucketName;
        init(key,secretKey);
    }

    private void init(String key, String secretKey) {
        dynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(key, secretKey)))
                .withRegion(regions).build();
        mapper = new DynamoDBMapper(dynamoDB);
        s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(key, secretKey)))
                .withRegion(regions)
                .build();
        transferManager = TransferManagerBuilder.standard().withS3Client(s3client).build();
    }

    public String getS3Bucket() {
        return s3Bucket;
    }

    public AmazonDynamoDB getDynamoDB() {
        return dynamoDB;
    }

    public DynamoDBMapper getMapper() {
        return mapper;
    }

    public AmazonS3 getS3client() {
        return s3client;
    }

    public TransferManager getTransferManager() {
        return transferManager;
    }

    public Regions getRegions() {
        return regions;
    }


    public static ForeAlertS3Client create(String bucketName, String key, String secretKey, String region) {
        if (null == INSTANCE) {
            synchronized (ForeAlertS3Client.class) {
                if (null == INSTANCE) {
                    INSTANCE = new ForeAlertS3Client(bucketName, key, secretKey, region);
                }
            }
        }
        return INSTANCE;
    }

    public void disconnect(){
        com.amazonaws.http.IdleConnectionReaper.shutdown();
        getDynamoDB().shutdown();
        getTransferManager().shutdownNow();
        INSTANCE.getS3client().shutdown();
    }
}
