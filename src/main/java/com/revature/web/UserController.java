package com.revature.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {
	/**
	 * This class will be responsible for calling a service that generates
	 * a JWT token to send back to the client.
	 */
	
	@GetMapping("/home")
	public String showUser() {
		
		return "application works";
	}

}
