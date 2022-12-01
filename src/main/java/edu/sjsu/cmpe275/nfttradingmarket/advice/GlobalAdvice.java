package edu.sjsu.cmpe275.nfttradingmarket.advice;

import edu.sjsu.cmpe275.nfttradingmarket.util.Util;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Sai Charan Peda, Ramya Kotha
 */

@ControllerAdvice
public class GlobalAdvice extends ResponseEntityExceptionHandler {

    /**
     * Handles SQLIntegrityConstraintViolationException
     *
     * @param e
     * @param request
     * @return Error Response
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(SQLIntegrityConstraintViolationException e, HttpServletRequest request) {

        return Util.prepareErrorResponse(e.getMessage(),  HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles MethodArgumentNotValidException
     *
     * @param e
     * @param headers
     * @param status
     * @param request
     * @return Error Response
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return Util.prepareErrorResponse(e.getMessage(), headers, status);
    }

}
