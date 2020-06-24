package com.kiran.jcache.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.cache.Cache;
import javax.cache.processor.EntryProcessorResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kiran.jcache.model.Customer;
import com.kiran.jcache.util.CustomerEntryProcessor;


@RestController
@RequestMapping(value = "/customers", produces = "application/json")
public class CustomersController {
	
	private static final Log LOG = LogFactory.getLog(CustomersController.class);
	
	private Cache<Long,Customer> customerCache;
	
	public CustomersController(Cache<Long, Customer> customerCache) {
		this.customerCache = customerCache;
	}
	
	@ResponseBody
	@RequestMapping(value = "/{id}" , method = RequestMethod.GET)
	public Customer getCustomer(@PathVariable("id") Long id) {
		LOG.info("get for id" + id);
		return customerCache.get(id);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/put" , method = RequestMethod.POST)
	public boolean putCustomer(@RequestBody Customer customer) {
		LOG.info("put for customer" + customer);
	    customerCache.put(customer.getId(),customer);
	    return true;
		
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/loadcachedata" , method = RequestMethod.GET)
	public boolean loadCacheData() {
		Customer cust = new Customer();
		Date date = new Date();
		for(int i = 1 ; i <100 ;i++) {			
	    	cust.setId((long) i);
			cust.setFirstName(getRandomString() + i);
			cust.setLastName(getRandomString() + i);
			cust.setDob(date);
			customerCache.put((long) i, cust);
		}
		return true;
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/RemoveCacheElement/{key}" , method = RequestMethod.DELETE)
	public boolean removeCacheElement(@PathVariable("key") Long key) {
		return customerCache.remove(key);
	}
	
	@ResponseBody
	@RequestMapping(value = "/ClearCache" , method = RequestMethod.DELETE)
	public boolean ClearCache() {
		 boolean status = false ;
		 customerCache.clear();
		 if(customerCache == null) {
			 status = true;
		 return status;
		 }
		return status;
	}
	
	//TODO: getAllCacheData needs to fixed for writethrough and read through implemention
	// No DAO for fetching data from DB so getting empty list.
	
	@ResponseBody
	@RequestMapping(value = "/getallcachedata" , method = RequestMethod.GET)
	public List<Customer> getallCacheData() {
		Set<Long> keys = new HashSet<>();
		customerCache.forEach(entry -> keys.add(entry.getKey()));	
		List<Customer> customers = customerCache.getAll(keys).values().stream().collect(Collectors.toList());
		return customers;
	}
	
	protected static String getRandomString() {
        String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder str = new StringBuilder();
        Random rnd = new Random();
        while (str.length() < 18) {
            int index = (int) (rnd.nextFloat() * LETTERS.length());
            str.append(LETTERS.charAt(index));
        }
        String randStr = str.toString();
        return randStr;
    }

	//DEPRECATED: Do not use below call.
	@ResponseBody
	@RequestMapping(value = "/age" , method = RequestMethod.GET)
	public Map<Long,Long> getAges(@RequestParam("keys") Set<Long> keys,@RequestParam(name = "uppercase") Boolean uppercase)
	{
		Map<Long,Long> ages = new HashMap<>(keys.size());
		CustomerEntryProcessor cep = new CustomerEntryProcessor();
		for(Long key : keys) {
			ages.put(key,customerCache.invoke(key,cep,uppercase));
		}		
		return ages;
	}
	
	
	//TODO: Need to check if keys are present in cache or not
	@ResponseBody
	@RequestMapping(value = "/correctage" , method = RequestMethod.GET)
	public Map<Long,Long> getAgesCorrect(@RequestParam("keys") Set<Long> keys,@RequestParam(name = "uppercase") Boolean uppercase)
	{
		Map<Long,EntryProcessorResult<Long>> result = customerCache.invokeAll(keys,new CustomerEntryProcessor(),uppercase);
		return result.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
	}

}
