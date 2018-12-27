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

		final File carpeta = new File(System.getProperty("user.dir")+"//familias//");

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

				//objeto principal
				for (int temp = 0; temp < listaEmpleados.getLength(); temp++) {
					Node nodo = listaEmpleados.item(temp);
					System.out.println("Elemento:" + nodo.getNodeName());
					System.out.println("Contenido del elemento:" + nodo.getTextContent());

					//Comprueba si el nodo es del tipo element, crt + click sobre Node para ver los tipos de nodos
					if (nodo.getNodeType() == Node.ELEMENT_NODE) {

						Element element = (Element) nodo;
						Family f = new Family();
						f.set_id(element.getElementsByTagName("_id").item(0).getTextContent());
						f.setNombre(element.getElementsByTagName("nombre").item(0).getTextContent());
						f.setFamilyId(Integer.parseInt(element.getElementsByTagName("familyId").item(0).getTextContent()));

						NodeList nodeList = document.getElementsByTagName("personas");
						System.out.println("personas: " + element.getElementsByTagName("personas").item(0).getTextContent());

						Person[] arrayPersonas = new Person[nodeList.getLength() - 1];//personas[personas1,persona2,personaN]
						//objeto anidado
						for(int i = 1 ; i < nodeList.getLength(); i++ ){

							Person p = new Person();
							//personas -> 0[personas1 -> 1,persona2 ->2,personaN->N] Queremos atacar al primer elemento del array
							//pero la primera pasada lo hace sobre el array global mostrando contenido del array
							Node n = nodeList.item(i);
							Element e = (Element) n;

							p.setNombre(e.getElementsByTagName("nombre").item(0).getTextContent());
							p.setPersonId(Integer.parseInt(e.getElementsByTagName("selfId").item(0).getTextContent()));
							p.set_id((e.getElementsByTagName("_id").item(0).getTextContent()));
							p.setFamilyId(Integer.parseInt(e.getElementsByTagName("familyId").item(0).getTextContent()));

							arrayPersonas[i-1] = p;

						}

						f.setPersonas(arrayPersonas);
						familyDao.crear(f);

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
}
