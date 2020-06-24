package com.kiran.jcache.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Address implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7190780643800480467L;

	@Id
	private Long id;
	
	private String street;
	
	private String city;
	
	private String postcode;
	
	

	public Address() {
		super();
	}

	public Address(Long id, String street, String city, String postcode) {
		super();
		this.id = id;
		this.street = street;
		this.city = city;
		this.postcode = postcode;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the postcode
	 */
	public String getPostcode() {
		return postcode;
	}

	/**
	 * @param postcode the postcode to set
	 */
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	
	
	

}
