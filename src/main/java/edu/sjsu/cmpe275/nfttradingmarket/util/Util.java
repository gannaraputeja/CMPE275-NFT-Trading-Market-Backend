package edu.sjsu.cmpe275.nfttradingmarket.util;


import edu.sjsu.cmpe275.nfttradingmarket.dto.response.MessageResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


/**
 * This is Util class.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Sai Charan Peda, Ramya Kotha
 */

public class Util {

    /**
     * Prepares a Response with given Response, HttpStatus
     *
     * @param response
     * @param status
     * @return Response in responseType format
     */
    public static ResponseEntity prepareResponse(Object response, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity(response, headers, status);
    }

    /**
     * Prepares a Response with given Response, HttpStatus
     *
     * @param response
     * @param headers
     * @param status
     * @return Response in responseType format
     */
    public static ResponseEntity prepareResponse(Object response, HttpHeaders headers, HttpStatus status) {
        return new ResponseEntity(response, headers, status);
    }

    /**
     * Prepares Error Response with given Code, Message, HttpStatus
     *
     * @param msg
     * @param status
     * @return Response in responseType format
     */
    public static ResponseEntity prepareErrorResponse(String msg, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();

        return new ResponseEntity(new MessageResponse(msg), headers, status);
    }

    /**
     * Prepares Error Response with given Code, Message, HttpStatus
     *
     * @param msg
     * @param headers
     * @param status
     * @return Response in responseType format
     */
    public static ResponseEntity prepareErrorResponse(String msg, HttpHeaders headers, HttpStatus status) {

        return new ResponseEntity(new MessageResponse(msg), headers, status);
    }


}
