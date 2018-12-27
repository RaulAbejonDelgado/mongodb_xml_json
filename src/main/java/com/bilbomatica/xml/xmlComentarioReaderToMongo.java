package com.bilbomatica.xml;

import com.bilbomatica.xml.dao.ComentarioDao;
import com.bilbomatica.xml.dao.FamilyDao;
import com.bilbomatica.xml.pojo.Coment;
import com.bilbomatica.xml.pojo.Customer;
import com.bilbomatica.xml.pojo.Family;
import com.bilbomatica.xml.pojo.Person;
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

public class xmlComentarioReaderToMongo {

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {

		final File carpeta = new File(System.getProperty("user.dir")+"//comentarios//");


		ComentarioDao comentarioDao = new ComentarioDao();

		File[] archivos = new File[carpeta.listFiles().length];

		comentarioDao = ComentarioDao.getInstance();

		archivos = listarFicherosPorCarpeta(carpeta );

		for( File a : archivos){
			System.out.println(a);
			try {
				File archivo = new File(String.valueOf(a));
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
				Document document = documentBuilder.parse(archivo);
				//String pathFile = String.valueOf(a);
				document.getDocumentElement().normalize();

				NodeList listaEmpleados = document.getElementsByTagName("Coment");

				NodeList personas = document.getElementsByTagName("personas");
				Node nodoP = personas.item(0);
				Element elementPersona = (Element) nodoP;
				//objeto principal
				for (int temp = 0; temp < listaEmpleados.getLength(); temp++) {
					Node nodo = listaEmpleados.item(temp);
					System.out.println("Elemento:" + nodo.getNodeName());
					System.out.println("Contenido del elemento:" + nodo.getTextContent());

					//Comprueba si el nodo es del tipo element, crt + click sobre Node para ver los tipos de nodos
					if (nodo.getNodeType() == Node.ELEMENT_NODE) {

						Element element = (Element) nodo;
						Family f = new Family();
						String xmlString = 	"<Coment>\r\n" +
								"  <familia>\r\n"+
										"<_id>"+ element.getElementsByTagName("_id").item(0).getTextContent()+ "</_id>\r\n"
										+"<familyId>" + element.getElementsByTagName("familyId").item(0).getTextContent()+  "</familyId>\r\n"
										+"<nombre>"+ element.getElementsByTagName("nombre").item(0).getTextContent() +"</nombre>\r\n"
									+"</familia>\r\n"
									+"<texto>"+element.getElementsByTagName("texto").item(0).getTextContent() +"</texto>\r\n"
									+"<persona>\r\n"
										+"<_id>" + " " +"</_id>\r\n"
										+"<nombre>"+element.getElementsByTagName("nombre").item(1).getTextContent()+ "</nombre>\r\n"
										+"<selfId>"+ element.getElementsByTagName("selfId").item(0).getTextContent()+"</selfId>\r\n"
										+"<familyId>"+ element.getElementsByTagName("familyId").item(1).getTextContent()+"</familyId>\r\n"
									+"</persona>"
									+"<comentarioId>"+element.getElementsByTagName("comentarioId").item(0).getTextContent()+"</comentarioId>>\r\n"
								+"</Coment>\r\n";

						System.out.println(xmlString);

						Coment c = (Coment) convertXmlString2DataObject(xmlString, Coment.class);
						//c = (Coment) convertXmlFile2DataObject(pathFile, Coment.class);
						comentarioDao.crear(c);

					}
				}

			}catch (Exception e){
				e.printStackTrace();
			}
		}
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
