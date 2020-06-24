package com.kiran.jcache.util;

import javax.cache.configuration.Factory;

public class CustomerCacheLoaderFactory implements Factory<CustomerCacheLoader>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7660295717518198075L;

	@Override
	public CustomerCacheLoader create() {
		return ApplicationContextSingleton
				.getApplicationContext()
				.getBean(CustomerCacheLoader.class);
	}

}
