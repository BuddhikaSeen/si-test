package com.surroundinsurance.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {
	
	@Value("${si.cors.mapping}")
	private String corsMapping;

	@Value("${si.cors.allowed.origins}")
	private String allowedOrigins;

	@Value("${si.cors.allowed.methods}")
	private String allowedMethods;

	@Value("${si.cors.allowed.headers}")
	private String allowedHeaders;
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping(corsMapping).allowedOrigins(allowedOrigins).allowedMethods(allowedMethods)
				.allowedHeaders(allowedHeaders);
	}

}
