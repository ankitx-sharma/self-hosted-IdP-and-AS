package com.project.auth.dto;

import com.project.auth.entity.UserEntity;

public class User {
	private long id;
	private String username;
	private String password;
	
	public User() { super(); }
	
	public User(long id, String username, String password) {
		this.id = id;
		this.username = username;
		this.password = password;
	}

	public User(UserEntity entity) {
		this.id = entity.getId();
		this.username = entity.getUsername();
		this.password = entity.getPassword();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
