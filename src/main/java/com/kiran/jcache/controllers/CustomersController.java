package com.kiran.jcache.controllers;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
	
	private final Cache<Long,Customer> customerCache;

    protected static final String LETTERS =
            IntStream.rangeClosed('A', 'Z')
                    .mapToObj(c -> String.valueOf((char) c))
                    .collect(Collectors.joining()) +
                    "0123456789";
    protected static final SecureRandom RANDOM = new SecureRandom();
	
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
	@RequestMapping(value = "/load-cache-data" , method = RequestMethod.GET)
	public boolean loadCacheData() {
		Customer customer = new Customer();
		Date date = new Date();
		for(int i = 1 ; i <100 ;i++) {
            customer.setId((long) i);
            customer.setFirstName(getRandomString(5));
            customer.setLastName(getRandomString(4));
            customer.setDob(date);
			customerCache.put((long) i, customer);
		}
		return true;
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/remove-cache-element/{key}" , method = RequestMethod.DELETE)
	public boolean removeCacheElement(@PathVariable("key") Long key) {
		return customerCache.remove(key);
	}

    @ResponseBody
    @RequestMapping(value = "/ClearCache" , method = RequestMethod.DELETE)
    public boolean ClearCache() {
        customerCache.clear();
        return true;
    }
	
	//TODO: getAllCacheData needs to fixed for writethrough and read through implementation
	// No DAO for fetching data from DB so getting empty list.
	@ResponseBody
	@RequestMapping(value = "/get-all-cache-data" , method = RequestMethod.GET)
	public List<Customer> getAllCacheData() {
		Set<Long> keys = new HashSet<>();
		customerCache.forEach(entry -> keys.add(entry.getKey()));
        return new ArrayList<>(customerCache.getAll(keys).values());
	}

    protected static String getRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(LETTERS.length());
            sb.append(LETTERS.charAt(index));
        }
        return sb.toString();
    }


	@ResponseBody
	@RequestMapping(value = "/age" , method = RequestMethod.GET)
    @Deprecated
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
	@RequestMapping(value = "/correct-age" , method = RequestMethod.GET)
	public Map<Long,Long> getAgesCorrect(@RequestParam("keys") Set<Long> keys,@RequestParam(name = "uppercase") Boolean uppercase)
	{
		Map<Long,EntryProcessorResult<Long>> result = customerCache.invokeAll(keys,new CustomerEntryProcessor(),uppercase);
		return result.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
	}

}
