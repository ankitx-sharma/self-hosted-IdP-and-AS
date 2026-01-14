package com.project.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.auth.dto.AccessTokenResponse;
import com.project.auth.dto.LoginResponse;
import com.project.auth.dto.RefreshRequest;
import com.project.auth.dto.User;
import com.project.auth.service.RefreshTokenService;
import com.project.auth.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RefreshTokenService tokenService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User loginRequest, HttpServletRequest request) {
		String sessionId = request.getRemoteAddr();
		
		try{
			LoginResponse response = userService.loginUser(loginRequest, sessionId);
			return ResponseEntity.status(HttpStatus.ACCEPTED.value()).body(response);
		}catch(RuntimeException ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body("Invalid username or password. "+ ex);
		}
	}
	
	@PostMapping("/refresh-token/get")
	public ResponseEntity<AccessTokenResponse> refresh(@RequestBody RefreshRequest request) {
		try {
			String newAccessToken = tokenService.refreshToken(request.getRefreshToken());
			AccessTokenResponse response = new AccessTokenResponse(newAccessToken);
			return ResponseEntity.ok(response);
		}catch(IllegalArgumentException ex) {
			System.out.println(ex);
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	@PostMapping("/refresh-token/revoke")
	public ResponseEntity<Void> logout(@RequestBody RefreshRequest request) {
		tokenService.revoke(request.getRefreshToken());
		return ResponseEntity.noContent().build();
	}
}
