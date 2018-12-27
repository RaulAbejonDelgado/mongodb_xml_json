package com.bilbomatica.xml.dao;

import com.bilbomatica.xml.pojo.Coment;
import com.bilbomatica.xml.pojo.Family;
import com.bilbomatica.xml.pojo.Person;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

import java.net.UnknownHostException;
import java.util.ArrayList;

import static com.bilbomatica.xml.dao.MongoConector.getConnectionDbAndCollection;

public class ComentarioDao {

    private static ComentarioDao INSTANCE = null;

    private static final String DB = "publicaciones";
    private static final String COLLECTION = "comentarios";
    private static PersonDao personDao = null ;
    private static FamilyDao familyDao = null;

    public static synchronized ComentarioDao getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ComentarioDao();
        }
        return INSTANCE;
    }

    public ArrayList<Coment> listarTodos() throws UnknownHostException {
        ArrayList<Coment>  comentarios= new ArrayList<Coment>();

        DBCollection collection = getConnectionDbAndCollection(DB, COLLECTION);


        // Busco todos los documentos de la colección y los imprimo
        try (com.mongodb.DBCursor cursor = collection.find()) {
            while (cursor.hasNext()) {

                comentarios.add(deMongoaJava((BasicDBObject) cursor.next()));
                System.out.println(comentarios);

            }
        }

        return comentarios;
    }

    //Transformo un objecto que me da MongoDB a un Objecto Java
    private Coment deMongoaJava(BasicDBObject toDBObjectComentario) {
        Family familia = new Family();
        Person persona = new Person();
        Coment c = new Coment();
        c.set_id(toDBObjectComentario.getString("_id"));
        c.setComentarioId(toDBObjectComentario.getInt("comentarioId"));
        c.setTexto(toDBObjectComentario.getString("texto"));

        BasicDBObject familiObject = new BasicDBObject();
        BasicDBObject personObject = new BasicDBObject();
        familiObject = (BasicDBObject) toDBObjectComentario.get("familia");

        personObject = (BasicDBObject) toDBObjectComentario.get("persona");

        familia.setFamilyId((familiObject.getInt("familyId")));
        familia.setNombre(familiObject.getString("nombre"));
        familia.set_id(familiObject.getString("_id"));

        System.out.println(familiObject.getString("_id"));

        persona.set_id(personObject.getString("_id"));
        persona.setPersonId(personObject.getInt("selfId"));

        c.setFamilia(familia);
        c.setPersona(persona);

        return c;
    }







}
