package com.project.auth.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.project.auth.entity.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Component
public class JwtUtil {
	
	private final SecretKey signingKey;
	private final JwtProperties props;
	
	public JwtUtil(SecretKey signingKey, JwtProperties props) {
		this.signingKey = signingKey;
		this.props = props;
	}
	
	public String generateAccessToken(String username, Role role) {
		Instant now = Instant.now();
		Instant expire = now.plus(props.getAccessTokenTtlMinutes(), ChronoUnit.MINUTES);
		
		return Jwts.builder()
				.issuer(props.getIssuer())
				.subject(username)
				.claim("role", role.name())
				.issuedAt(Date.from(now))
				.expiration(Date.from(expire))
				.signWith(signingKey)
				.compact();
	}
	
	public Claims parseAndValidate(String token) throws JwtException{
		return Jwts.parser()
				.verifyWith(signingKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	
	public String extractUsername(String token) throws JwtException{
		return parseAndValidate(token).getSubject();
	}
	
	public String extractRole(String token) throws JwtException{
		return parseAndValidate(token).get("role", String.class);
	}
}
