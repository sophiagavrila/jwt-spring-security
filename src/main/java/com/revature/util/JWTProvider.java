package com.revature.util;

import static java.util.Arrays.stream;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.revature.models.UserPrincipal;

/**
 * This Class is responsible for generating the JWT's to send to the client,
 * And validating the JWT's received with the prev. generated token.
 */
public class JWTProvider {

	/**
	 * The secret is a random string that the algorithm uses to encode/decode our
	 * tokens typically you have this stored on a secure server, and integrate it
	 * via the properties file.
	 * 
	 * 1. If the secret is stored in a (remote) secure server, there would be a
	 * mechanism to authenticate on that server and retrieve the token to be used in
	 * the application.
	 * 
	 * 2. If the token is saved and encrypted locally, you will need to decrypt it
	 * in order to use it.
	 */
	@Value("${jwt.secret}") // @Value reads to an array
	private String secret;

	// generates the token by validating the UserPrincipal object
	public String generateJwtToken(UserPrincipal userPrincipal) {

		// take the UserPrincipal and then capture all authorities of the object
		String[] claims = getClaimsFromUser(userPrincipal);

		return JWT.create().withIssuer(SecurityConstant.REVATURE_LLC)
				.withAudience(SecurityConstant.REVATURE_ADMINISTRATION).withIssuedAt(new Date())
				.withSubject(userPrincipal.getUsername()).withArrayClaim(SecurityConstant.AUTHORITIES, claims)
				.withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
				.sign(Algorithm.HMAC512(secret.getBytes()));
	}

	private String[] getClaimsFromUser(UserPrincipal user) {

		List<String> authorities = new ArrayList<>();

		for (GrantedAuthority grantedAuthority : user.getAuthorities()) {
			authorities.add(grantedAuthority.getAuthority());
		}

		return authorities.toArray(new String[0]);
		/**
		 * Why are we returning a String[] by passing the argument new String[0]?
		 * 
		 * There are two styles to convert a collection to an Array: String[c.size()]
		 * versus using new String[0] as an argument of toArray method.
		 * 
		 * The former involves using active inspection which can cause performane
		 * overhead at runtime (cost of Refleciton).
		 * 
		 * Resource:
		 * https://stackoverflow.com/questions/18136437/whats-the-use-of-new-string0-in-toarraynew-string0
		 */
	}

	// Get the Authorities from the Token - this will call the getClaimsFromToken()
	public List<GrantedAuthority> getAuthorities(String token) {

		String[] claims = getClaimsFromToken(token);
		return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

	// this method will call on a JWT verifier method
	private String[] getClaimsFromToken(String token) {
		JWTVerifier verifier = getJWTVerifier();
		return verifier.verify(token).getClaim(SecurityConstant.AUTHORITIES).asArray(String.class);
	}

	private JWTVerifier getJWTVerifier() {

		JWTVerifier verifier;

		try {
			Algorithm algorithm = Algorithm.HMAC512(secret);
			verifier = JWT.require(algorithm).withIssuer(SecurityConstant.REVATURE_LLC).build();
		} catch (JWTVerificationException exception) {

			throw new JWTVerificationException(SecurityConstant.TOKEN_CANNOT_BE_VERIFIED);
			/**
			 * We don't throw the actual exception back to the user because we don't want
			 * the user to see the inner workings of the application - critical information
			 * can be revealed in that exception. Instead we use a pre-defined constant.
			 */
		}
		return verifier;
	}

	/**
	 * This method gets the authentication of the user. If we can verify a token is
	 * correct, then the next thing we have to do is tell Spring Security to get me
	 * the authentication of the user and then set that authentication in the spring
	 * Security Context.
	 * 
	 * This method gets the authentication AFTER we verify the token.
	 */
	public Authentication getAuthentication(String username, List<GrantedAuthority> authorities,
			HttpServletRequest request) {

		// Construct the token object - null represents extra credentials
		UsernamePasswordAuthenticationToken userPassAuthToken = new UsernamePasswordAuthenticationToken(username, null,
				authorities);

		// we now have to set up the User details about this authentication token
		userPassAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		return userPassAuthToken;
	}

	// This method checks that the token produced by the above method is valid
	public boolean isTokenValid(String username, String token) {
		JWTVerifier verifier = getJWTVerifier();

		/**
		 * Check username is not null, empty, etc...Look for a Apache Common Lang
		 * dependency by googling "stringutils java maven"
		 */
		boolean isValid = StringUtils.isNotEmpty(username) && !isTokenExpired(verifier, token); // create this method to check that it's NOT expired

		return isValid;
	}

	// This method is invoked in isTokenValid method() above ^
	public boolean isTokenExpired(JWTVerifier verifier, String token) {

		Date expiration = verifier.verify(token).getExpiresAt();
		boolean isExpired = expiration.before(new Date());
		/**
		 * Date() Allocates a Date object and initializes it so that it represents the
		 * time at which it was allocated, measured to the nearest millisecond.
		 */
		return isExpired;
	}
	
	public String getSubject(String token) {
		
		JWTVerifier verifier = getJWTVerifier();
		String subject = verifier.verify(token).getSubject();
		
		return subject;
		
	}
}