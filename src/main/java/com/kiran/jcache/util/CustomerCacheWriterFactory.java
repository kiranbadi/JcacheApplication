package com.kiran.jcache.util;

import javax.cache.configuration.Factory;

public class CustomerCacheWriterFactory implements Factory<CustomersCacheWriter> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4389567853024074742L;

	@Override
	public CustomersCacheWriter create() {
	
		return ApplicationContextSingleton
				.getApplicationContext()
				.getBean(CustomersCacheWriter.class);
	}
   
}
