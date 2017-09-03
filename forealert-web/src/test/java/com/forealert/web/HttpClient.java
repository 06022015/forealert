package com.forealert.web;

import com.forealert.intf.exception.ForeAlertException;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/18/17
 * Time: 8:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class HttpClient {

    private static Logger logger = LoggerFactory.getLogger(HttpClient.class);

    public ClientResponse get(final String endpoint) throws ForeAlertException {
        HttpURLConnection connection = getConnection(endpoint, "GET");
        return connect(connection);
    }

    public ClientResponse post(String endPoint, String content) throws ForeAlertException {
        HttpURLConnection connection = getConnection(endPoint, "POST");
        OutputStream os = null;
        try {
            connection.setDoOutput(true);
            os = connection.getOutputStream();
            os.write(content.getBytes());
            os.flush();
        } catch (IOException e) {
            logger.error("Unable to write request:- " + e.getMessage());
            throw new ForeAlertException("Unable to write request", e.getCause(), HttpStatus.SC_NOT_FOUND);
        } finally {
            close(os);
        }
        return connect(connection);
    }

    private HttpURLConnection getConnection(String endPoint, String method) {
        try {
            URL url = new URL(endPoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            return connection;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            logger.error("Unable to form URL:- " + e.getMessage());
            throw new ForeAlertException("URL not formed properly", e.getCause(), HttpStatus.SC_PRECONDITION_FAILED);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ForeAlertException("Unable to connect to server",e.getCause(),  HttpStatus.SC_BAD_REQUEST);
        }
    }

    private ClientResponse connect(HttpURLConnection connection) throws ForeAlertException {
        int code;
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            //logger.debug("Connecting to dsp server....");
            connection.connect();
            code = connection.getResponseCode();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String output = null;
            while ((output = reader.readLine()) != null) {
                sb.append(output);
            }
        } catch (SocketTimeoutException e) {
            logger.error("DSP connection timed out:- ");
            code = HttpStatus.SC_GATEWAY_TIMEOUT;
        } catch (FileNotFoundException e) {
            logger.error(" File not found:- " + connection.getURL().toString());
            code = HttpStatus.SC_NO_CONTENT;
        } catch (IOException e) {
            try {
                logger.debug("failed to connect. Reading error message");
                code = connection.getResponseCode();
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                String output = null;
                while ((output = reader.readLine()) != null) {
                    sb.append(output);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new ForeAlertException("Unable to connect to server", ex.getCause(), HttpStatus.SC_SERVICE_UNAVAILABLE);
            } finally {
                close(reader);
            }
            e.printStackTrace();
        } finally {
            close(reader);
        }
        return new ClientResponse(code, sb.toString());
    }

    private void close(Object obj) {
        try {
            if (null == obj)
                return;
            if (obj instanceof InputStream) {
                InputStream in = (InputStream) obj;
                in.close();
            } else if (obj instanceof OutputStream) {
                OutputStream out = (OutputStream) obj;
                out.close();
            }
            obj = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws InterruptedException {
        for (int i = 4; i < 50000; i++) {
            final int index = i;
            Thread thread = new Thread() {
                public void run() {
                    testForeAlertUser(index);
                }
            };
            thread.start();
            Thread.sleep(1);
        }
    }


    public static String testForeAlertUser(Integer count) {
        long startTime = Calendar.getInstance().getTimeInMillis();
        System.out.println("Thread Number" + count + " Start time:- " + startTime);
        HttpClient client = new HttpClient();
        String endPoint = "http://localhost:8080/forealert/services/v1/user";
        /*String content = "{  \n\" +\n" +
                "                \"   \"uuId\":\"asdfajhsgf2342hjasdf768df\"+count+\"\",\n\" +\n" +
                "                \"   \"name\":\"Rizwan Qureshi\"+count+\"\",\n\" +\n" +
                "                \"   \"email\":\"rizwanqureshi\"+count+\"@gmail.com\",\n\" +\n" +
                "                \"   \"username\":\"rubinaqureshi\"+count+\"\",\n\" +\n" +
                "                \"   \"mobile\":\"9886327\"+count+\"\",\n\" +\n" +
                "                \"   \"location\":{  \n\" +\n" +
                "                \"      \"latitude\":\"+2342.33*count+\",\n\" +\n" +
                "                \"      \"longitude\":\"+3424.23*count+\",\n\" +\n" +
                "                \"      \"altitude\":\"+24*count+\",\n\" +\n" +
                "                \"      \"radius\":\"+23*count+\"\n\" +\n" +
                "                \"   }\n\" +\n" +
                "                \"}";*/
        String content = "{  \n" +
                "   \"uuId\":\"asdfajhsgf2342hjasdf768df"+count+"\",\n" +
                "   \"name\":\"Ashif Qureshi"+count+"\",\n" +
                "   \"email\":\"ashifqureshi15@gmail.com\",\n" +
                "   \"username\":\"ashifqureshi"+count+"\",\n" +
                "   \"mobile\":\"9886387129\",\n" +
                "   \"location\":{  \n" +
                "      \"latitude\":"+(130.34+count)+",\n" +
                "      \"city\":\"California\",\n" +
                "      \"longitude\":"+(120.35+count)+",\n" +
                "      \"altitude\":"+(12+count)+",\n" +
                "      \"radius\":5\n" +
                "   }\n" +
                "}";
        try {
            ClientResponse response = client.post(endPoint, content);
            long endTime = Calendar.getInstance().getTimeInMillis();
            System.out.println("Thread Number" + count + " End time:- " + endTime);
            System.out.println("Thread Number" + count + " Diff:- " + (endTime - startTime));
            System.out.println(response.getContent());
            return response.getContent();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Unable to write request:- " + e.getMessage());
            throw new ForeAlertException("Unable to write request", e.getCause(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public static String testForeAlertReportAnIssue(Integer count) {
        long startTime = Calendar.getInstance().getTimeInMillis();
        System.out.println("Thread Number" + count + " Start time:- " + startTime);
        String endPoint = "http://localhost:8080/forealert/services/v1/message/report/issue";
        String content = "{  \n" +
                "   \"title\":\"Accident\",\n" +
                "   \"message\":\"Accident happened in Indira Nagar\",\n" +
                "   \"app\":\"EMG_APP\",\n" +
                "   \"senderUUId\":\"asdfajhsgf2342hjasdf76"+count+"\",\n" +
                "   \"messageLocation\":{  \n" +
                "      \"latitude\":23.33"+count+",\n" +
                "      \"longitude\":"+34.24*count+",\n" +
                "      \"altitude\":"+23*count+",\n" +
                "      \"radius\":"+23*count+",\n" +
                "      \"warningRadius\":"+23*count+"\n" +
                "   },\n" +
                "   \"senderLocation\":{  \n" +
                "      \"latitude\":"+2342.33*count+",\n" +
                "      \"longitude\":"+3424.23*count+",\n" +
                "      \"altitude\":"+23*count+",\n" +
                "      \"radius\":"+23*count+",\n" +
                "      \"warningRadius\":"+23*count+"\n" +
                "   },\n" +
                "   \"status\":\"A\",\n" +
                "   \"type\":\"ZONE\",\n" +
                "   \"isUserGEOMessage\":true,\n" +
                "   \"device\":\"IOS\"\n" +
                "}";
        HttpClient client = new HttpClient();
        try {
            ClientResponse response = client.post(endPoint, content);
            long endTime = Calendar.getInstance().getTimeInMillis();
            System.out.println("Thread Number" + count + " End time:- " + endTime);
            System.out.println("Thread Number" + count + " Diff:- " + (endTime - startTime));
            System.out.println(response.getContent());
            return response.getContent();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Unable to write request:- " + e.getMessage());
            throw new ForeAlertException("Unable to write request", e.getCause(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
