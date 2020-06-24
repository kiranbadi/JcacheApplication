package com.kiran.jcache.util;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ApplicationContextSingleton {
	
	private static ApplicationContext applicationContext;
	
	public ApplicationContextSingleton(ApplicationContext applicationContext) {
		ApplicationContextSingleton.applicationContext = applicationContext;
	}
	
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
