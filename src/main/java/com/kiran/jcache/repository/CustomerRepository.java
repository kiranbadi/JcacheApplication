package com.kiran.jcache.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kiran.jcache.model.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer,Long>{
	
	
	@Query("delete from Customer c where c.id IN :keys")
	void deleteAll(@Param("keys") Collection<?> keys);
	

	@Query(value = "SELECT * FROM customer where id = ? " , nativeQuery = true)
	Customer findOne(@Param("key") Long key);

}
