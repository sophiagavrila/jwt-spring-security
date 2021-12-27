package com.revature.models;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class models the response sent from a server to a client:
 * 
 * {
 * 		code: 200;
 * 		httpStatus: OK
 * 			reason: ok
 * 			message: your request was successful
 * }
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpResponse {

	private int httpStatusCode;
	private HttpStatus httpStatus;
	private String reason;
	private String message;
	
	
	
}
