package com.bilbomatica.xml;

import com.bilbomatica.xml.dao.ComentarioDao;
import com.bilbomatica.xml.dao.FamilyDao;
import com.bilbomatica.xml.pojo.Coment;
import com.bilbomatica.xml.pojo.Family;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ExtractorComentariosMongo {

    private static final String DB = "publicaciones";
    private static final String COLLECTION = "comentarios";


    public static void main(String[] args) throws IOException {

        ArrayList<Coment> comentarios = new ArrayList<Coment>();

        ComentarioDao comentarioDao = null;

        comentarioDao = ComentarioDao.getInstance();

        comentarios = comentarioDao.listarTodos();

        File directorio = new File(System.getProperty("user.dir")+"//comentarios//");

        directorio.mkdir();

        for(Coment c : comentarios){
            // 1. Write Object to XML String
            String xmlPerson = write2XMLString(c);
            String pathFilePerson = directorio + "//comentario_" + c.getComentarioId()+".xml";

            write2XMLFile(c, pathFilePerson);
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
