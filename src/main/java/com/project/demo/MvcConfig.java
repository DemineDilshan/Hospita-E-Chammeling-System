package com.project.demo;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		Path doctorUploadDir = Paths.get("./doctor-image"); 
		String doctorUploadPath = doctorUploadDir.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/doctor-image/**").addResourceLocations("file:/" + doctorUploadPath + "/");
	}
	
	

}
