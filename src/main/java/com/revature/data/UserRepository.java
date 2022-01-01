package com.revature.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.models.User;

/**
 * JPA allows you to manipulate a DB without having to write all of the 
 * @author SophieGavrila
 *
 */
public interface UserRepository extends JpaRepository<User, Long> { // <Object we're managing, Primary Key Type>

	// JPA Repo is smart enough to translate the following to a JDBC statement
    User findUserByUsername(String username);

    User findUserByEmail(String email);
}