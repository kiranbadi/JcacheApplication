package com.kiran.jcache.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;



@Entity
@Table(name = "customer")
public class Customer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1296280821345484703L;
	
	
	
	public Customer(Long id, String firstName, String lastName, Date dob) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dob = dob;
	}
	
	

	public Customer() {
		super();
	}



	@Id
    @Column(name = "id")
	private Long id;
	
    @Column(name = "first_Name")
	private String firstName;
    
    @Column(name = "last_Name")
	private String lastName;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@Column(name = "dob")
	private Date dob;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dob, firstName, id, lastName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Customer)) {
			return false;
		}
		Customer other = (Customer) obj;
		return Objects.equals(dob, other.dob) && Objects.equals(firstName, other.firstName)
				&& Objects.equals(id, other.id) && Objects.equals(lastName, other.lastName);
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", dob=" + dob + "]";
	}
	
	
	
	
}

