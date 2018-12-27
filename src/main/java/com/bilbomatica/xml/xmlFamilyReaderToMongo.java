package com.bilbomatica.xml;

import com.bilbomatica.xml.dao.FamilyDao;
import com.bilbomatica.xml.pojo.Family;
import com.bilbomatica.xml.pojo.Person;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class xmlFamilyReaderToMongo {


	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {



		final File carpeta = new File(System.getProperty("user.dir")+"\\familias\\");

		FamilyDao familyDao = new FamilyDao();

		File[] archivos = new File[carpeta.listFiles().length];


		familyDao = FamilyDao.getInstance();

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
				NodeList listaEmpleados = document.getElementsByTagName("Family");
				System.out.println(listaEmpleados);

				for (int temp = 0; temp < listaEmpleados.getLength(); temp++) {
					Node nodo = listaEmpleados.item(temp);
					System.out.println("Elemento:" + nodo.getNodeName());
					System.out.println("Elemento:" + nodo.getNodeValue());

					if (nodo.getNodeType() == Node.ELEMENT_NODE) {
						Element element = (Element) nodo;
//						System.out.println("_id: " + element.getElementsByTagName("_id").item(0).getTextContent());
//						System.out.println("Nombre: " + element.getElementsByTagName("nombre").item(0).getTextContent());
//						System.out.println("familyId: " + element.getElementsByTagName("familyId").item(0).getTextContent());
//						System.out.println("personas: " + element.getElementsByTagName("personas").item(0).getTextContent());
						Family f = new Family();
						f.set_id(element.getElementsByTagName("_id").item(0).getTextContent());
						f.setNombre(element.getElementsByTagName("nombre").item(0).getTextContent());
						f.setFamilyId(Integer.parseInt(element.getElementsByTagName("familyId").item(0).getTextContent()));
						//f.setPersonas(element.getElementsByTagName("personas").item(1));
						//todo mirar ingresar personas
						NodeList nodeList = document.getElementsByTagName("personas");
						System.out.println("personas: " + element.getElementsByTagName("personas").item(0).getTextContent());
						//System.out.println(document.getElementsByTagName("personas"));
						ArrayList<Person> personasXml = new ArrayList<Person>();
						for(int i = 0 ; i < nodeList.getLength(); i++ ){

							System.out.println(element.getElementsByTagName("personas").item(1).getTextContent().replace("\n","").trim());
//							System.out.println("personas: " +"-" +element.getElementsByTagName("personas").item(i).getNodeName()+ " - "+ element.getElementsByTagName("personas").item(i).getTextContent());
//							System.out.println("personas: " +"-" +element.getElementsByTagName("personas").item(i).getChildNodes().item(i).getNextSibling());
//							personasXml.add((
//									new Person()
//							))
						}
						//p.setPersonId(Integer.parseInt(element.getElementsByTagName("selfId").item(0).getTextContent()));
						familyDao.crear(f);
						//personas.add(p);


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

//	public static Object convertXmlString2DataObject(String xmlString, Class<?> cls)
//						throws JsonParseException, JsonMappingException, IOException{
//
//		XmlMapper xmlMapper = new XmlMapper();
//		Object object = xmlMapper.readValue(xmlString, cls);
//		return object;
//	}
//
//	public static Object convertXmlFile2DataObject(String pathFile, Class<?> cls)
//			throws JsonParseException, JsonMappingException, IOException{
//
//		XmlMapper xmlMapper = new XmlMapper();
//		Object object = xmlMapper.readValue(new File(pathFile), cls);
//		return object;
//	}
}
