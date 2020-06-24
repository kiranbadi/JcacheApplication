package com.kiran.jcache.util;



import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.cache.integration.CacheLoader;
import javax.cache.integration.CacheLoaderException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kiran.jcache.model.Customer;
import com.kiran.jcache.repository.CustomerRepository;


// TODO : Need to fix the cache loader.Difference in implmentation for jcache and spring respositories.

@Component
public class CustomerCacheLoader implements CacheLoader<Long,Customer>{
	
	private static final Log LOG = LogFactory.getLog(CustomerCacheLoader.class);
	
   
	 CustomerRepository customerRepository;
	
   @Autowired
	public CustomerCacheLoader(CustomerRepository customerRepository) {
		super();
		this.customerRepository = customerRepository;
	}


	@Override
	public Map<Long, Customer> loadAll(Iterable<? extends Long> keys) throws CacheLoaderException {
		LOG.info("loadAll called with keys" + keys);
	   Iterable<Customer> customers = customerRepository.findAll();
		return StreamSupport
			   .stream(customers.spliterator(),false)
			   .collect(Collectors.toMap(Customer::getId,Function.identity()));
	}

	@Override
	public Customer load(Long key) throws CacheLoaderException {
		LOG.info("load called with key " + key);
		Customer customer = customerRepository.findOne(key);
		return customer;
	}

}
