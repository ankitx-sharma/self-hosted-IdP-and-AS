package com.project.auth.dto;

public class TokenResponse {
	private String accessToken;
	
	public TokenResponse(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}
