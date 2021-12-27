package com.revature.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor
@Entity
@Table(name="users")
public class User implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable=false)
	private Long id; // PK used as secret index of User obj
	private String userId; // for User to see like CWJ123
	private String firstName;
	private String lastName;
	private String username;
	
	@Column(name="pwd")
	private String password;
	private String email;
	private String profileImageUrl;
	
	private Date lastLoginDate; // 
	private Date lastLoginDateDisplay; // Date used to display to user when last logged in
	private Date joinDate; // when User joined platform
	
	private String[] roles; // ROSE_USER { delete, update, create }, ROLE_ADMIN 
	private String[] authorities; // permissions that can exist within some roles (i.e you might have a ROLE_USER that you don't want to have delete permission

	private boolean isActive;
	private boolean isNotLocked;

}
