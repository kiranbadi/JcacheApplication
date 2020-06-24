package com.kiran.jcache.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.MutableEntry;

import com.kiran.jcache.model.Customer;

public class CustomerEntryProcessor implements EntryProcessor<Long,Customer,Long> {

	@Override
	public Long process(MutableEntry<Long, Customer> entry, Object... arguments) throws EntryProcessorException {
		Customer customer = entry.getValue();
		boolean uppercase =(boolean)arguments[0];
		
		if(uppercase) {
			customer.setLastName(customer.getLastName().toUpperCase());
		} else {
			customer.setLastName(customer.getLastName().toLowerCase());

		}
		entry.setValue(customer);
		return calculateAge(customer);
	}

	private Long calculateAge(Customer customer) {
	    if(null == customer) {
	    	return null;
	    }
	    
	    LocalDate dobAtLocalTimezone = customer.getDob().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return Long.valueOf(Period.between(dobAtLocalTimezone, LocalDate.now()).getYears());
	}

}
