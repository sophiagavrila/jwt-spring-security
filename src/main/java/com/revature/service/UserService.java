package com.revature.service;

import java.util.List;

import com.revature.models.User;

public interface UserService {
	
    User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, UsernameExistException, EmailExistException; // custom exceptions

    List<User> getUsers();

    User findUserByUsername(String username);

    User findUserByEmail(String email);
}


