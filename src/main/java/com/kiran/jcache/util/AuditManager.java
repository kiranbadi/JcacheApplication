package com.kiran.jcache.util;

import javax.cache.Cache;
import javax.cache.configuration.MutableCacheEntryListenerConfiguration;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;

import com.kiran.jcache.model.Customer;


@Service
@ManagedResource(objectName = "jcacheExample:bean=AuditManager")
public class AuditManager {
	
	
	private MutableCacheEntryListenerConfiguration<Long,Customer> config;
	
	private Cache<Long,Customer> cache;

	public AuditManager(MutableCacheEntryListenerConfiguration<Long,Customer> config, Cache<Long,Customer> cache) {
		super();
		this.config = config;
		this.cache = cache;
	}
	
	
	@ManagedOperation
	public void startAudit() {
		cache.registerCacheEntryListener(config);
	}
	
	
	@ManagedOperation
	public void stopAudit() {
		cache.deregisterCacheEntryListener(config);
	}

}
