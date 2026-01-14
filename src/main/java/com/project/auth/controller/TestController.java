package com.project.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.auth.service.UserService;

@RestController
@RequestMapping("/api/test")
public class TestController {
	
	@Autowired
	public UserService userService;

	@GetMapping("/profile/admin")
	public ResponseEntity<?> getAdminProfile(){
		return ResponseEntity.ok("Access Granted to Admin. Secured profile info!");
	}
	
	@GetMapping("/profile")
	public ResponseEntity<?> getProfile(){
		return ResponseEntity.ok("Access Granted. Secured profile info!");
	}
}