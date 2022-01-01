package com.revature.filter;

import static com.revature.util.SecurityConstant.FORBIDDEN_MESSAGE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.HttpResponse;

/**
 * This class represents a handler for whenever Authentication fails.
 * (When a User is NOT authenticated and they try to access the application).
 * 
 * This will fire whenever that happens so that we can control what's returned.
 */
@Component // configure when Spring starts
public class JwtAuthenticationEntryPoint extends Http403ForbiddenEntryPoint {

	/*
	 * We are overriding the commence() method of Http403ForbiddenEntryPoint - You
	 * can grab the method signature by opening the declaration of
	 * Http403ForbiddenEntryPoint and c/p-ing it here. Then we modify the Overriden method.
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException arg2)
			throws IOException {

		// Here we customize the HttpResponse returned by using our HttpResponse custom class.
		HttpResponse httpResponse = new HttpResponse(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN,
				HttpStatus.FORBIDDEN.getReasonPhrase().toUpperCase(), FORBIDDEN_MESSAGE);

		response.setContentType(APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.FORBIDDEN.value()); // sets the status code #

		// Now take the response and send the httpResponse obj we created on line 30 and stream it out!
		OutputStream outputStream = response.getOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(outputStream, httpResponse);

		outputStream.flush();
		/**
		 * The flush() method of OutputStream class is used to flush the content of the
		 * buffer to the output stream. A buffer is a portion in memory that is used to
		 * store a stream of data(characters). That data sometimes will only get sent to
		 * an output device, when the buffer is full.
		 */
	}
}