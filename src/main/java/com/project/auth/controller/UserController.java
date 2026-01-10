package com.project.auth.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.auth.dto.TokenResponse;
import com.project.auth.dto.User;
import com.project.auth.service.UserService;
import com.project.auth.util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody User user) {
		Optional<User> userData = userService.findByUserName(user.getUsername());
		
		if(userData.isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body("User already registered.");
		}
		userService.registerUser(user);
		return ResponseEntity.ok("User Registered");
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User loginRequest){
		Optional<User> userOptional = userService.findByUserName(loginRequest.getUsername());
		
		if(userOptional.isPresent()) {
			User user = userOptional.get();
			if(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
				String token = jwtUtil.generateAccessToken(loginRequest.getUsername());
				return ResponseEntity.status(HttpStatus.ACCEPTED.value()).body(new TokenResponse(token));
			}
		}
		
		return ResponseEntity.status(401).body("Invalid username or password");
	}
	
	@GetMapping("/profile")
	public ResponseEntity<?> getProfile(){
		return ResponseEntity.ok("Access Granted. Secured profile info!");
	}

}
