package com.user.service.user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	  @Bean
	    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	        return http
	                .csrf(csrf -> csrf.disable())
	                .authorizeHttpRequests(auth -> auth
	                        .requestMatchers("/user/**").hasRole("USER")
	                        .anyRequest().authenticated()
	                )
	                .oauth2ResourceServer(oauth -> oauth.jwt())
	                .build();
	    }
	  
	   @Bean
	    public JwtAuthenticationConverter jwtAuthenticationConverter() {
	        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();

	        converter.setAuthoritiesClaimName("scope"); // read "scope"
	        converter.setAuthorityPrefix("");           // remove "SCOPE_"

	        JwtAuthenticationConverter authConverter = new JwtAuthenticationConverter();
	        authConverter.setJwtGrantedAuthoritiesConverter(converter);

	        return authConverter;
	    }
}
