package com.project.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.auth.dto.LoginResponse;
import com.project.auth.dto.User;
import com.project.auth.entity.Role;
import com.project.auth.entity.UserEntity;
import com.project.auth.repository.UserRepository;
import com.project.auth.util.JwtUtil;

@Service
public class UserService {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private RefreshTokenService tokenService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public void registerUser(User user) {
		String password = passwordEncoder.encode(user.getPassword());
		UserEntity userEntity = new UserEntity(user.getUsername(), password, Role.USER);
		userRepository.save(userEntity);
	}
	
	public void registerAdmin(User user) {
		String password = passwordEncoder.encode(user.getPassword());
		UserEntity userEntity = new UserEntity(user.getUsername(), password, Role.ADMIN);
		userRepository.save(userEntity);
	}
	
	public LoginResponse loginUser(User loginRequest, String sessionId) {
		System.out.println(loginRequest.getUsername());
		UserEntity userEntity = findUserEntityByName(loginRequest.getUsername());
		if(userEntity == null) { throw new RuntimeException("Invalid credentials"); }
		
		if (!passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword())) {
		    throw new RuntimeException("Invalid credentials");
		}
		
		String accessToken = jwtUtil.generateAccessToken(loginRequest.getUsername(), userEntity.getRole());
		String refreshToken = tokenService.issueRefreshToken(userEntity, sessionId);
		
		return new LoginResponse(accessToken, refreshToken);
	}
	
	public User findByUserName(String username) {
		UserEntity entity = findUserEntityByName(username);
		
		return entity == null ? null : new User(entity);
	}
	
	private UserEntity findUserEntityByName(String username){
		return userRepository.findByUsername(username).orElse(null);
	}
}
