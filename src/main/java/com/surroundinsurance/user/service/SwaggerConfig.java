package com.surroundinsurance.user.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket getDocket() {

		return new Docket(DocumentationType.SWAGGER_2).groupName("si-user-service").select()
				.apis(RequestHandlerSelectors.basePackage("com.surroundinsurance.user.service.controller"))
				.paths(PathSelectors.any()).build().apiInfo(getApiInfo());
	}

	private ApiInfo getApiInfo() {

		return new ApiInfoBuilder().title("SI User Service").description("SI User Service API Documentation")
				.version("1.0").build();
	}

}
