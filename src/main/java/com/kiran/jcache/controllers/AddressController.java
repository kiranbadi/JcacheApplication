package com.kiran.jcache.controllers;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kiran.jcache.model.Address;
import com.kiran.jcache.repository.AddressesRepository;

@RestController
@RequestMapping(value = "/customers", produces = "application/json")
@CacheDefaults(cacheName="addresses")
public class AddressController {
	
	private static final Log LOG = LogFactory.getLog(AddressController.class);
	private AddressesRepository addressesRepository;

	public AddressController(AddressesRepository addressesRepository) {
		super();
		this.addressesRepository = addressesRepository;
	}
	
	@ResponseBody
	@RequestMapping(value = "/{addressId}" , method = RequestMethod.GET)
	@CacheResult
	public Address getAddress(@PathVariable("addressId") Long addressId) {
		LOG.info("get for id" + addressId);
		return addressesRepository.findOne(addressId);
	}
	
	@ResponseBody
	@RequestMapping(value = "/{addressId}" , method = RequestMethod.DELETE)
	@CacheRemove
	public void deleteAddress(@PathVariable("addressId") Long addressId) {
		LOG.info("delete for id" + addressId);
		var address = addressesRepository.findOne(addressId);
		addressesRepository.delete(address);
	}
	
	@RequestMapping(method = RequestMethod.DELETE)
	@CacheRemoveAll
	public void deleteAllAddress() {
		addressesRepository.deleteAll();
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/postaddress" , method = RequestMethod.POST)
	@CachePut
	public boolean postAddress(@PathVariable("id") @CacheKey Long id, @CacheValue Address address) {
		LOG.info("Post for Address" + address);
		addressesRepository.save(address);
	    return true;
		
	}

}
