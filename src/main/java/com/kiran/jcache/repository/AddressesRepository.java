package com.kiran.jcache.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kiran.jcache.model.Address;




@Repository
public interface AddressesRepository extends CrudRepository<Address,Long> {

	
	@Query(value = "SELECT * FROM address where id = ? " , nativeQuery = true)
	Address findOne(Long addressId);

}
