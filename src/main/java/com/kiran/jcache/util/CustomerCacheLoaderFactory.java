package com.kiran.jcache.util;

import javax.cache.configuration.Factory;
import java.io.Serial;

public class CustomerCacheLoaderFactory implements Factory<CustomerCacheLoader>{

	/**
	 * 
	 */
	@Serial
    private static final long serialVersionUID = -7660295717518198075L;

	@Override
	public CustomerCacheLoader create() {
		return ApplicationContextSingleton
				.getApplicationContext()
				.getBean(CustomerCacheLoader.class);
	}

}
