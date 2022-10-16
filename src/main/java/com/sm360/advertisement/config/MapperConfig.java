package com.sm360.advertisement.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.val;

@Configuration
public class MapperConfig {
	@Bean
    public ModelMapper modelMapper() {
		val mapper = new ModelMapper();
		return mapper;
	}
}
