package com.project.auth.dto;

public class RefreshRequest {
	private String refreshToken;
	
	public String getRefreshToken() { return this.refreshToken; }
	public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}