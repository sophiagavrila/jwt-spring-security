package com.revatures.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

import org.springframework.web.filter.OncePerRequestFilter;

import com.revature.util.JwtProvider;
import com.revature.util.SecurityConstant;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private JwtProvider jwtProvider;

	/**
	 * Any time this class is instantiated, it has a JWT Provider object passed
	 * through and set as its property.
	 */
	public JwtAuthorizationFilter(JwtProvider jwtProvider) {
		super();
		this.jwtProvider = jwtProvider;
	}

	/**
	 * This method fires ONCE every time that a client sends an HTTP Request to this
	 * application.
	 * 
	 * It does: Check to make sure that the User is valid and that the token is
	 * valid. Then set the user as the authenticated User.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		/**
		 * Take the Request and make sure that the method is OPTIONS. Whenever there
		 * is a request that goes to a request, there is an OPTIONS reuqest that asks
		 * for information about the server so that client can collect info about the
		 * type of info that the server can accept.
		 * 
		 * IF it's options, we want to implement some logic to:
		 */
        if (request.getMethod().equalsIgnoreCase(SecurityConstant.OPTIONS_HTTP_METHOD)) {
            response.setStatus(OK.value());
            
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            
            if (authorizationHeader == null || !authorizationHeader.startsWith(SecurityConstant.TOKEN_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }
        }


	}

}
