package com.project.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.auth.dto.User;
import com.project.auth.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class UserController {
	
	@Autowired
	private UserService userService;

	@PostMapping("/register/user")
	public ResponseEntity<String> registerUser(@RequestBody User user) {
		User userData = userService.findByUserName(user.getUsername());
		if(userData != null) { 
			return sendErrorResponse(HttpStatus.BAD_REQUEST.name(), "User already registered."); 
		}
		
		userService.registerUser(user);
		return ResponseEntity.ok("User Registered");
	}
	
	@PostMapping("/register/admin")
	public ResponseEntity<String> registerAdmin(@RequestBody User user) {
		User userData = userService.findByUserName(user.getUsername());
		
		if(userData == null) { 
			sendErrorResponse(HttpStatus.BAD_REQUEST.name(), "User already registered."); 
		}
		userService.registerAdmin(user);
		return ResponseEntity.ok("User Registered");
	}
	
	private ResponseEntity<String> sendErrorResponse(String status, String jsonBody) {
		return ResponseEntity.status(HttpStatus.valueOf(status)).body(jsonBody);
	}
}
