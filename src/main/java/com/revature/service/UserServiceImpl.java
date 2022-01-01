package com.revature.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.revature.data.UserRepository;
import com.revature.models.User;
import com.revature.models.UserPrincipal;

@Service
@Transactional
@Qualifier("UserDetailsService") // name for this specific Bean
public class UserServiceImpl implements UserService, UserDetailsService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private UserRepository userRepository;
	
    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
    }

    /**
     * This method queries the DB and returns the User
     * as a UserDetails object (in the form of our customer UserPrincipal
     * class which has the methods/properties to store UserObject in Spring Context
     */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findUserByUsername(username);
		if (user == null) {
			logger.error("User not found with username: " + username);
			throw new UsernameNotFoundException("User not found by username: " + username);
			
		} else {
			user.setLastLoginDateDisplay(user.getLastLoginDate());
			user.setLastLoginDate(new Date());

			userRepository.save(user);
			/**
			 * Since UserPrincipal implements UserDetails from Spring Security, it contains
			 * all the methods and properties to save the user in Spring's Security Context
			 */
			UserPrincipal userPrincipal = new UserPrincipal(user);
			logger.info("User found with username: " + username);
			return userPrincipal;
		}
	}

	@Override
	public User register(String firstName, String lastName, String username, String email)
			throws UserNotFoundException, UsernameExistException, EmailExistException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User findUserByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User findUserByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

}
