package com.bilbomatica.xml;

import com.bilbomatica.xml.dao.PersonDao;
import com.bilbomatica.xml.pojo.Customer;
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
import java.util.ArrayList;

public class xmlPersonReaderToMongo {


	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {



		final File carpeta = new File(System.getProperty("user.dir")+"//personas//");

		PersonDao personDao = new PersonDao();

		File[] archivos = new File[carpeta.listFiles().length];

		ArrayList<Person> personas = new ArrayList<Person>();

		personDao = PersonDao.getInstance();

		archivos = listarFicherosPorCarpeta(carpeta );

		for( File a : archivos){

			try {
				File archivo = new File(String.valueOf(a));
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
				Document document = documentBuilder.parse(archivo);

				document.getDocumentElement().normalize();
				System.out.println("Elemento raiz:" + document.getDocumentElement().getNodeName());
				NodeList listaEmpleados = document.getElementsByTagName("Person");

				for (int temp = 0; temp < listaEmpleados.getLength(); temp++) {

					Node nodo = listaEmpleados.item(temp);

					System.out.println("Elemento:" + nodo.getNodeName());
					System.out.println("Contenido del elemento:" + nodo.getNodeValue());

					if (nodo.getNodeType() == Node.ELEMENT_NODE) {

						Element element = (Element) nodo;
						Person p = new Person();

						p.set_id(element.getElementsByTagName("_id").item(0).getTextContent());
						p.setNombre(element.getElementsByTagName("nombre").item(0).getTextContent());
						p.setFamilyId(Integer.parseInt(element.getElementsByTagName("familyId").item(0).getTextContent()));
						p.setPersonId(Integer.parseInt(element.getElementsByTagName("selfId").item(0).getTextContent()));

						personDao.crear(p);

					}
				}

			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	private static File[] listarFicherosPorCarpeta(File carpeta) {

        File[]  archivos = new File[carpeta.listFiles().length];
        try {

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

        }catch (Exception e){
            e.printStackTrace();
        }
        return archivos;
	}





}
