package com.forealert.web.provider;

import com.forealert.intf.dto.ForeAlertStatus;
import com.forealert.intf.exception.ForeAlertException;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/22/17
 * Time: 5:58 PM
 * To change this template use File | Settings | File Templates.
 */
@Provider
public class ServiceExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Throwable> {
    private Logger logger = LoggerFactory.getLogger(ServiceExceptionMapper.class);

    @Override
    public Response toResponse(Throwable throwable) {
        throwable.printStackTrace();
        int code = 500;
        String message = "Something went wrong";
        if(throwable instanceof ForeAlertException){
            ForeAlertException foreAlertException = (ForeAlertException) throwable;
            code = foreAlertException.getCode();
            message = foreAlertException.getMessage();
        }else if(throwable instanceof JwtException){
            code = HttpStatus.UNAUTHORIZED.value();
            message = throwable.getMessage();
        }else if(throwable instanceof ForbiddenException || throwable instanceof SecurityException){
             code = HttpStatus.FORBIDDEN.value();
            message = "Your current role does not allow to access";
        }else if(throwable instanceof NotAllowedException){
            code = HttpStatus.METHOD_NOT_ALLOWED.value();
            message = HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase();
        }else{
            throwable.printStackTrace();
        }
        ForeAlertStatus status = new ForeAlertStatus();
        status.setCode(code);
        status.setMessage(message);
        return Response.status(code).entity(status).build();
    }
}
