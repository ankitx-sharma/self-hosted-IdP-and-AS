package com.project.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.project.auth.filter.JwtFilter;
import com.project.auth.security.RestAccessDeniedHandler;
import com.project.auth.security.RestAutheticationEntryPoint;

@Configuration
public class SecurityConfig {

	@Autowired
	private JwtFilter jwtFilter;
	
	@Autowired
	private RestAccessDeniedHandler accessDeniedHandler;
	
	@Autowired
	private RestAutheticationEntryPoint authEntryPoint;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http.securityMatcher("/**")
			.csrf(csrf -> csrf.disable())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			// API declared which needs to go through authentication process
			.authorizeHttpRequests(
				auth -> auth
					.requestMatchers("/api/auth/register", "/api/auth/login", "/error").permitAll()
					.anyRequest().authenticated()
			)
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
			// Define how the exceptions should be handled
			.exceptionHandling(ex -> ex
				.authenticationEntryPoint(authEntryPoint)
				.accessDeniedHandler(accessDeniedHandler)
			);
		
		return http.build();
	}
}
