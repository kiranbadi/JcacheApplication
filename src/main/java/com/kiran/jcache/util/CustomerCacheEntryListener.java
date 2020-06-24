package com.kiran.jcache.util;

import javax.cache.event.CacheEntryCreatedListener;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryUpdatedListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kiran.jcache.model.Customer;

import javax.cache.event.CacheEntryRemovedListener;
import javax.cache.event.CacheEntryExpiredListener;
import javax.cache.event.CacheEntryListenerException;


public class CustomerCacheEntryListener implements 
                     CacheEntryCreatedListener<Long,Customer>,
                     CacheEntryUpdatedListener<Long,Customer>,
                     CacheEntryRemovedListener<Long,Customer>,
                     CacheEntryExpiredListener<Long,Customer>
    {
	
	private static final Log LOG = LogFactory.getLog(CustomerCacheEntryListener.class);

	@Override
	public void onExpired(Iterable<CacheEntryEvent<? extends Long, ? extends Customer>> events)
			throws CacheEntryListenerException {
		events.forEach(entry -> LOG.info("Cache Entry onExpired for key: " + entry.getKey() + " new value: " + entry.getValue() + " old value :" + entry.getOldValue()));
		
	}

	@Override
	public void onUpdated(Iterable<CacheEntryEvent<? extends Long, ? extends Customer>> events)
			throws CacheEntryListenerException {
		events.forEach(entry -> LOG.info("Cache Entry onUpdated for key: " + entry.getKey() + " new value: " + entry.getValue() + " old value :" + entry.getOldValue()));

		
	}

	@Override
	public void onCreated(Iterable<CacheEntryEvent<? extends Long, ? extends Customer>> events)
			throws CacheEntryListenerException {
		events.forEach(entry -> LOG.info("Cache Entry onCreated for key: " + entry.getKey() + " new value: " + entry.getValue() + " old value :" + entry.getOldValue()));
		
	}

	@Override
	public void onRemoved(Iterable<CacheEntryEvent<? extends Long, ? extends Customer>> events)
			throws CacheEntryListenerException {
		events.forEach(entry -> LOG.info("Cache Entry onRemoved for key: " + entry.getKey() + " new value: " + entry.getValue() + " old value :" + entry.getOldValue()));
		
	}

}
