package com.revature.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.revature.models.UserPrincipal;

/**
 * This Class is responsible for generating the JWT's to 
 * send to the client.
 */
public class JWTProvider {
	
	// The secret is a random string that the algorithm uses to encode/decode our tokens
	@Value("jwt.secret") // @Value reads from the yml file
	private String secret; // typically you have this stored on a secure server, and integrate it via the properties file
	// This is typically MUCH more secure on production grade code...
	// When the App is deployed, then the configuration file would be bundled in the deployed JAR
	
	public String generateJwtToken(UserPrincipal userPrincipal) {
		
		// take the UserPrincipal and then capture all authorities of the object
		String[] claims = getClaimsFromUser(userPrincipal);
		
		return JWT.create().withIssuer(SecurityConstant.REVATURE_LLC).withAudience(SecurityConstant.REVATURE_ADMINISTRATION)
				.withIssuedAt(new Date()).withSubject(userPrincipal.getUsername()).withArrayClaim(SecurityConstant.AUTHORITIES, claims)
				.withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME)).sign(Algorithm.HMAC512(secret.getBytes()));
	}
	
	private String[] getClaimsFromUser(UserPrincipal userPrincipal) {
		
		return null;
		
	}
	
	
	

}
