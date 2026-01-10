package com.project.auth.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.auth.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authHeader = request.getHeader("Authorization");
		
		// No token -> let Spring handle (public endpoints ok, protected endpoints -> 401 via entrypoint)
		if(authHeader == null || !authHeader.startsWith("Bearer")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String token = authHeader.substring(7).trim();
		
		try {
			Claims tokenValidated = jwtUtil.parseAndValidate(token);
			String username = tokenValidated.getSubject();
			
			if(username == null || username.isBlank()) {
				write401(response, "INVALID_OR_EXPIRED_TOKEN", "JWT is expired or invalid.");
				return;
			}
			
			// Already authenticated -> continue
			
			if(SecurityContextHolder.getContext().getAuthentication() == null) {
				UsernamePasswordAuthenticationToken auth = 
						new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
			
			filterChain.doFilter(request, response);
		} catch (JwtException ex) {
			SecurityContextHolder.clearContext();
			write401(response, "INVALID_OR_EXPIRED_TOKEN", "JWT is expired or invalid.");
		}
		
	}
	
	private void write401(HttpServletResponse response, String code, String message) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		
		// Minimal consistent JSON can be expanded later
		String body = """
			{
				"code": "%s",
				"message": "%s",
				"timestamp": "%s"
			}
			""".formatted(code, message, Instant.now().toString());
		
		response.getWriter().write(body);
	}
}
