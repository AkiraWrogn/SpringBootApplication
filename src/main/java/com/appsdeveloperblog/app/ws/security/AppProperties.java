package com.appsdeveloperblog.app.ws.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {

	// one way to read value from application.property is 'use Enviormnet var '
	// to use enviornment variable we need to autowired it
	@Autowired
	private Environment env;
	
	public String getTokenSecret() {
		return env.getProperty("tokenSecret");
	}
}
