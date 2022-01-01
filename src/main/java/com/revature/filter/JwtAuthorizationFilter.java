package com.revature.filter;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.revature.util.JwtProvider;
import com.revature.util.SecurityConstant;

@Component // configure when Spring starts
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
        if (request.getMethod().equalsIgnoreCase(SecurityConstant.OPTIONS_HTTP_METHOD)) { // Here we're making sure the client is just 
            response.setStatus(OK.value()); 											  // sending a request to gain info about the server.
        } else {																		  // i.e what header is acceptable?
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            
            // If it's either null or doesn't start with the Token Prefix, "Bearer", return ASAP
            if (authorizationHeader == null || !authorizationHeader.startsWith(SecurityConstant.TOKEN_PREFIX)) {
                filterChain.doFilter(request, response);
                return; // stop the execution of the method, otherwise continue...
            }
            
            /**
             * If the header is valid...(not null and starts with the TOKEN_PREFIX,
             * Capture the token and define the User as the Subject of the token.
             */
            String token = authorizationHeader.substring(SecurityConstant.TOKEN_PREFIX.length());
            String username = jwtProvider.getSubject(token);
            
            // Below may not be necessary since we aren't using Session (?) - future Sophia, work on this...
            if (jwtProvider.isTokenValid(username, token) && SecurityContextHolder.getContext().getAuthentication() == null) {
                List<GrantedAuthority> authorities = jwtProvider.getAuthorities(token);
                Authentication authentication = jwtProvider.getAuthentication(username,  authorities, request);
                SecurityContextHolder.getContext().setAuthentication(authentication); // Here we make Spring aware of the authenticated user
            } else {
            	SecurityContextHolder.clearContext(); // clear the context
            }
        }
        
        filterChain.doFilter(request,  response);
	}

}
