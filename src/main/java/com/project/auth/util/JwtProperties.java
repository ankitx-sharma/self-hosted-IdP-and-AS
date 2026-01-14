package com.project.auth.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
	
	private String issuer;
	private String secretBase64;
	private long accessTokenTtlMinutes;
	private long refreshTokenTtlDays;
	
	public String getIssuer() { return issuer; }
	public void setIssuer(String issuer) { this.issuer = issuer; }
	
	public String getSecretBase64() { return secretBase64; }
	public void setSecretBase64(String secretBase64) { this.secretBase64 = secretBase64; }
	
	public long getAccessTokenTtlMinutes() { return accessTokenTtlMinutes; }
	public void setAccessTokenTtlMinutes(long accessTokenTtlMinutes) { this.accessTokenTtlMinutes = accessTokenTtlMinutes; }
	
	public long getRefreshTokenTtlDays() { return this.refreshTokenTtlDays; }
	public void setRefreshTokenTtlDays(long refreshTokenTtlDays) { this.refreshTokenTtlDays = refreshTokenTtlDays; }
}
