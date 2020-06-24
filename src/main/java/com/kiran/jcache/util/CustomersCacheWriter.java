package com.kiran.jcache.util;

import java.util.Collection;
import java.util.Set;

import java.util.stream.Collectors; 


import javax.cache.Cache;
import javax.cache.Cache.Entry;
import javax.cache.integration.CacheWriter;
import javax.cache.integration.CacheWriterException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kiran.jcache.model.Customer;
import com.kiran.jcache.repository.CustomerRepository;


@Component
public class CustomersCacheWriter implements CacheWriter<Long,Customer>{
	
	private static final Log LOG = LogFactory.getLog(CustomersCacheWriter.class);
	

	
   
	CustomerRepository customerRepository;
	
	 @Autowired
	public CustomersCacheWriter(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}
	

	@Override
	public void write(Entry<? extends Long, ? extends Customer> entry) throws CacheWriterException {
		LOG.info("write called with entries" + entry);
		customerRepository.save(entry.getValue());
		
	}

	@Override
	public void writeAll(Collection<Cache.Entry<? extends Long, ? extends Customer>> entries) throws CacheWriterException {
		LOG.info("writeAll called with entries" + entries);
		Set<Customer> customers = entries
				.stream()
				.map(Cache.Entry::getValue)
				.collect(Collectors.toSet());
		customerRepository.saveAll(customers);
		
	}

	@Override
	public void delete(Object key) throws CacheWriterException {
		LOG.info("Delete called with keys" + (Long)key);
		customerRepository.deleteById((Long) key);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteAll(Collection<?> keys) throws CacheWriterException {
		LOG.info("DeleteAll called with keys" + keys);
		customerRepository.deleteAll((Collection<Long>)keys);
		
	}
}
