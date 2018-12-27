package com.bilbomatica.xml.dao;

import com.bilbomatica.xml.pojo.Person;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
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

    public boolean crear(Person p) throws UnknownHostException {

        boolean resul = false;
        Person pe = new Person();

        BasicDBObject dBObjectPerson;

        DBCollection collection = getConnectionDbAndCollection(DB, COLLECTION);

        long numDocumentos = collection.getCount();
        p.setPersonId((int) (numDocumentos + 1));

        dBObjectPerson = toDBObjectPerson(p);

        WriteResult wr = collection.insert(dBObjectPerson);

        pe = obtenerPorId(p.getselfId());
        p.set_id(pe.get_id());


        if(numDocumentos < collection.getCount()){

            resul = true;

        }

        return resul;

    }

    private BasicDBObject toDBObjectPerson(Person p) {

        // Creamos una instancia BasicDBObject
        BasicDBObject dBObjectPerson = new BasicDBObject();



        dBObjectPerson.append("personId", p.getselfId());

        dBObjectPerson.append("nombre", p.getNombre());

        dBObjectPerson.append("familyId", p.getFamilyId() );

        return dBObjectPerson;
    }

    public Person obtenerPorId(int id) throws UnknownHostException {
        Person p = new Person();

        DBCollection collection = getConnectionDbAndCollection(DB, COLLECTION);
        JacksonDBCollection<Person, String> coll = JacksonDBCollection.wrap(collection, Person.class, String.class);

        BasicDBObject query = new BasicDBObject();
        query.put("personId", id);

        try (DBCursor<Person> cursor = coll.find(query)) {
            while (cursor.hasNext()) {

                p = cursor.next();

            }
        }
        p.toString();

        return p;
    }


}
