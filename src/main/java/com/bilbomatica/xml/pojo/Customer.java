package com.bilbomatica.xml.pojo;

public class Customer {
	private String name;
	private Integer age;
	private Address address;
	
	public Customer() {}
	
	public Customer(String name, Integer age, Address address) {
		this.name = name;
		this.age = age;
		this.address = address;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setAge(Integer age) {
		this.age = age;
	}
	
	public Integer getAge() {
		return this.age;
	}
	
	public void setAddress(Address address) {
		this.address = address;
	}
	
	public Address getAddress() {
		return this.address;
	}
	
	@Override
	public String toString() {
		String info = String.format("Customer[name=%s, age=%d, Address[street=%s, postcode=%s]]", 
										name, age, address.getStreet(), address.getPostcode());
		return info;
	}
}
