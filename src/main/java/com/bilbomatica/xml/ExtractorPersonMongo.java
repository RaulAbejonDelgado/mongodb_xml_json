package com.bilbomatica.xml;

import com.bilbomatica.xml.dao.PersonDao;
import com.bilbomatica.xml.pojo.Person;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ExtractorPersonMongo {

    private static final String DB = "publicaciones";
    private static final String COLLECTION = "persons";


    public static void main(String[] args) throws IOException {

        ArrayList<Person> personas = new ArrayList<Person>();

        PersonDao personaDao = null;

        personaDao = PersonDao.getInstance();

        personas = personaDao.listar();

        File directorio = new File(System.getProperty("user.dir")+"\\personas\\");

        directorio.mkdir();

        for(Person p : personas){
            // 1. Write Object to XML String
            String xmlPerson = write2XMLString(p);
            //String pathFilePerson = System.getProperty("user.dir") +"\\personas\\"+ p.getNombre()+".xml";
            String pathFilePerson = directorio + "\\" + p.getNombre()+".xml";

            write2XMLFile(p, pathFilePerson);
        }


    }

    /*
     * Convert Object to XML String
     */
    public static String write2XMLString(Object object)
            throws JsonProcessingException {

        XmlMapper xmlMapper = new XmlMapper();
        // use the line of code for pretty-print XML on console. We should remove it in production.
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

        return xmlMapper.writeValueAsString(object);
    }

    /*
     * Write Object to XML file
     */
    public static void write2XMLFile(Object object, String pathFile)
            throws JsonGenerationException, JsonMappingException, IOException {

        XmlMapper xmlMapper = new XmlMapper();
        // use the line of code for pretty-print XML on console. We should remove it in production.
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

        xmlMapper.writeValue(new File(pathFile), object);
    }

}
