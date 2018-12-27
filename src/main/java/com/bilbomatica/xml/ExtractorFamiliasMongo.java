package com.bilbomatica.xml;

import com.bilbomatica.xml.dao.FamilyDao;
import com.bilbomatica.xml.dao.PersonDao;
import com.bilbomatica.xml.pojo.Family;
import com.bilbomatica.xml.pojo.Person;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ExtractorFamiliasMongo {

    private static final String DB = "publicaciones";
    private static final String COLLECTION = "familias";


    public static void main(String[] args) throws IOException {

        ArrayList<Family> familias = new ArrayList<Family>();

        FamilyDao familyDao = null;

        familyDao = FamilyDao.getInstance();

        familias = familyDao.listarTodos();

        File directorio = new File(System.getProperty("user.dir")+"\\familias\\");

        directorio.mkdir();

        for(Family f : familias){
            // 1. Write Object to XML String
            String xmlPerson = write2XMLString(f);
            //String pathFilePerson = System.getProperty("user.dir") +"\\personas\\"+ p.getNombre()+".xml";
            String pathFilePerson = directorio + "\\" + f.getNombre()+".xml";

            write2XMLFile(f, pathFilePerson);
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
