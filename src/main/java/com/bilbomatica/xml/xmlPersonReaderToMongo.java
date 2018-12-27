package com.bilbomatica.xml;

import com.bilbomatica.xml.pojo.Customer;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;

public class xmlPersonReaderToMongo {
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {



		final File carpeta = new File(System.getProperty("user.dir")+"\\personas\\");

		File[] archivos = new File[carpeta.listFiles().length];

		archivos = listarFicherosPorCarpeta(carpeta );

		for( File a : archivos){
			System.out.println(a);
			try {
				File archivo = new File(String.valueOf(a));
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
				Document document = documentBuilder.parse(archivo);

				document.getDocumentElement().normalize();
				System.out.println("Elemento raiz:" + document.getDocumentElement().getNodeName());
				NodeList listaEmpleados = document.getElementsByTagName("Person");
				System.out.println(listaEmpleados);
				for (int temp = 0; temp < listaEmpleados.getLength(); temp++) {
					Node nodo = listaEmpleados.item(temp);
					System.out.println("Elemento:" + nodo.getNodeName());
					System.out.println("Elemento:" + nodo.getNodeValue());

					if (nodo.getNodeType() == Node.ELEMENT_NODE) {
						Element element = (Element) nodo;
						System.out.println("_id: " + element.getElementsByTagName("_id").item(0).getTextContent());
						System.out.println("Nombre: " + element.getElementsByTagName("nombre").item(0).getTextContent());
						System.out.println("familyId: " + element.getElementsByTagName("familyId").item(0).getTextContent());
						System.out.println("selfId: " + element.getElementsByTagName("selfId").item(0).getTextContent());
					}
				}

			}catch (Exception e){
				e.printStackTrace();
			}


		}


		/*
		 * 1. Convert String XML to Object
		 */
//		String xmlString = 	"<Customer>\r\n" +
//							"  <name>Mary</name>\r\n" +
//							"  <age>37</age>\r\n" +
//							"  <address>\r\n" +
//							"    <street>NANTERRE CT</street>\r\n" +
//							"    <postcode>77471</postcode>\r\n" +
//							"  </address>\r\n" +
//							"</Customer>\r\n";
//


//		Customer cust = (Customer) convertXmlString2DataObject(xmlString, Customer.class);
//
//		System.out.println(cust);
		// -> Customer[name=Mary, age=37, Address[street=NANTERRE CT, postcode=77471]]
		
		/*
		 * 2. Convert XML File to Object
		 */
//		String pathFile = System.getProperty("user.dir") + "\\customerTest.xml";
//		cust = (Customer) convertXmlFile2DataObject(pathFile, Customer.class);
//
//		System.out.println(cust);
	}

	private static File[] listarFicherosPorCarpeta(File carpeta) {
		File[] archivos = new File[carpeta.listFiles().length];
		int contador = 0;
		for (final File ficheroEntrada : carpeta.listFiles()) {
			if (ficheroEntrada.isDirectory()) {
				listarFicherosPorCarpeta(ficheroEntrada);
			} else {
				if(contador <  carpeta.listFiles().length){
					archivos[contador] =  ficheroEntrada;
					contador++;
				}
				System.out.println(ficheroEntrada.getName());
			}
		}
		return archivos;
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
