package com.revature.util;

public class SecurityConstant {
	
	public static final long EXPIRATION_TIME =  432_000_000; // 5 days (expressed in miliseconds), etc depending on enterprise demands for expiration of the token
	
	// The TOKEN_PREFIX is in front of every token
	public static final String TOKEN_PREFIX = "Bearer"; // This allows us to confirm that whatever HTTP request has this token, indeed has a JWT token
	public static final String JWT_TOKEN_HEADER = "Jwt-Token"; // The name of the header in the request.
	public static final String TOKEN_CANNOT_BE_VERIFIED = "Token Cannot be verified"; // to alert us if the token has been tampered with
    public static final	String REVATURE_LLC = "Revature LLC"; // company sending the actual token (what company's server provided us with the token? Google? FB?
	public static final String REVATURE_ADMINISTRATION = "User Manangement Portal"; // who will be using the app
	public static final String AUTHORITIES = "Authorities"; 
	public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page";
	public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
	public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
	
	// Here we define all variables which are accessible and valid within our app
	public static final String[] PUBLIC_URLS = { "/user/login", "/user/register", "/user/resetpassword/**", "/user/image/**" };
	
}
