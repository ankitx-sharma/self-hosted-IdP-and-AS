package com.project.auth.dto;

public class AccessTokenResponse {
	private String accessToken;
	
	public AccessTokenResponse(String accessToken) { this.accessToken = accessToken; }
	
	public String getAccessToken() { return this.accessToken; }
	public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
}
