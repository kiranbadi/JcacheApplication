package com.kiran.jcache.util;


import com.kiran.jcache.model.Customer;

import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryEventFilter;
import javax.cache.event.CacheEntryListenerException;


public class CustomerCacheEntryEventFilter implements CacheEntryEventFilter<Long,Customer> {

	@Override
	public boolean evaluate(CacheEntryEvent<? extends Long, ? extends Customer> event)
			throws CacheEntryListenerException {
		return event.getKey() % 2 == 0;
	}

}
