package com.bilbomatica.xml.dao;

import com.bilbomatica.xml.pojo.Person;
import com.mongodb.DBCollection;
import org.mongojack.DBCursor;
import org.mongojack.JacksonDBCollection;

import java.net.UnknownHostException;
import java.util.ArrayList;

import static com.bilbomatica.xml.dao.MongoConector.getConnectionDbAndCollection;

public class PersonDao {

    private static PersonDao INSTANCE = null;
    private static PersonDao personaDao = null;
    ArrayList<Person> personas = null;

    private static final String DB = "publicaciones";
    private static final String COLLECTION = "persons";


    public PersonDao() {
        super();

    }

    public static synchronized PersonDao getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PersonDao();
        }
        return INSTANCE;
    }

    public ArrayList<Person> listar() throws UnknownHostException {

        personas = new ArrayList<Person>();
        DBCollection collection = getConnectionDbAndCollection(DB, COLLECTION);

        JacksonDBCollection<Person, String> coll = JacksonDBCollection.wrap(collection, Person.class, String.class);
        // Busco todos los documentos de la colección y los imprimo
        try (DBCursor<Person> cursor = coll.find()) {
            while (cursor.hasNext()) {
                personas.add(cursor.next());

            }
        }
        System.out.println(personas);

        return personas;


    }


}
