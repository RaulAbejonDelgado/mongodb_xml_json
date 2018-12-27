package com.bilbomatica.xml;

import java.io.File;
import java.io.IOException;

import com.bilbomatica.xml.pojo.Customer;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class JavaReadXml2Object {
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		
		/*
		 * 1. Convert String XML to Object
		 */
		String xmlString = 	"<Customer>\r\n" + 
							"  <name>Mary</name>\r\n" + 
							"  <age>37</age>\r\n" + 
							"  <address>\r\n" + 
							"    <street>NANTERRE CT</street>\r\n" + 
							"    <postcode>77471</postcode>\r\n" + 
							"  </address>\r\n" + 
							"</Customer>\r\n";
		Customer cust = (Customer) convertXmlString2DataObject(xmlString, Customer.class);
		
		System.out.println(cust);
		// -> Customer[name=Mary, age=37, Address[street=NANTERRE CT, postcode=77471]]
		
		/*
		 * 2. Convert XML File to Object
		 */
		String pathFile = System.getProperty("user.dir") + "\\customerTest.xml";
		cust = (Customer) convertXmlFile2DataObject(pathFile, Customer.class);
		
		System.out.println(cust);
	}
	
	public static Object convertXmlString2DataObject(String xmlString, Class<?> cls) 
						throws JsonParseException, JsonMappingException, IOException{
		
		XmlMapper xmlMapper = new XmlMapper();
		Object object = xmlMapper.readValue(xmlString, cls);
		return object;
	}
	
	public static Object convertXmlFile2DataObject(String pathFile, Class<?> cls) 
			throws JsonParseException, JsonMappingException, IOException{

		XmlMapper xmlMapper = new XmlMapper();
		Object object = xmlMapper.readValue(new File(pathFile), cls);
		return object;
	}
}
