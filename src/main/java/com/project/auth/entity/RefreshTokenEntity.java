package com.project.auth.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "refresh_tokens", indexes = {
	@Index(name = "idx_refresh_token_hash", columnList = "tokenHash", unique = true),
	@Index(name = "idx_refresh_token_user", columnList = "user_id")
})
public class RefreshTokenEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true, length = 64)
	private String tokenHash;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private UserEntity user;
	
	@Column(nullable = false)
	private Instant expiresAt;
	
	@Column(nullable = false)
	private boolean revoked = false;
	
	@Column(nullable = false)
	private Instant createdAt = Instant.now();
	
	private Instant revokedAt;
	private Instant lastUsedAt;
	private String sessionId;
	
	public Long getId() { return id; }

	public String getTokenHash() { return tokenHash; }
	public void setTokenHash(String tokenHash) { this.tokenHash = tokenHash; }
	
	public UserEntity getUser() { return user; }
	public void setUser(UserEntity user) { this.user = user; }
	
	public Instant getExpiresAt() {	return expiresAt; }
	public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
	
	public boolean isRevoked() { return revoked; }
	public void setRevoked(boolean revoked) { this.revoked = revoked; }
	
	public Instant getCreatedAt() { return createdAt; }
	public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
	
	public Instant getRevokedAt() { return revokedAt; }
	public void setRevokedAt(Instant revokedAt) { this.revokedAt = revokedAt; }
	
	public Instant getLastUsedAt() { return lastUsedAt;	}
	public void setLastUsedAt(Instant lastUsedAt) {	this.lastUsedAt = lastUsedAt; }
	
	public String getSessionId() { return sessionId; }
	public void setSessionId(String sessionId) { this.sessionId = sessionId; }
	
}
