package com.githum.napalm4j.examples.invoicing.model;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity @Data
@NamedQuery(name="customers",query="SELECT c FROM Customer c ORDER BY c.lastName, c.firstName")
public class Customer {
	@Id @GeneratedValue
	private Long id;
	
	private String firstName;
	private String lastName;
	@Temporal(TemporalType.DATE)
	private Calendar birthDate;
	private String email;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zipCode;
	
}
