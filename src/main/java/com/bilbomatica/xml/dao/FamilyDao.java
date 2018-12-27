package com.bilbomatica.xml.dao;

import com.bilbomatica.xml.pojo.Family;
import com.bilbomatica.xml.pojo.Person;
import com.mongodb.DBCollection;
import org.mongojack.DBCursor;
import org.mongojack.JacksonDBCollection;

import java.net.UnknownHostException;
import java.util.ArrayList;

import static com.bilbomatica.xml.dao.MongoConector.getConnectionDbAndCollection;

public class FamilyDao {

    private static FamilyDao INSTANCE = null;

    private static final String DB = "publicaciones";
    private static final String COLLECTION = "familias";
    Family fo = new Family();

    public static synchronized FamilyDao getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FamilyDao();
        }
        return INSTANCE;
    }



    public ArrayList<Family> listarTodos() throws UnknownHostException {

        ArrayList<Family>  familias= new ArrayList<Family>();
        DBCollection collection = getConnectionDbAndCollection(DB, COLLECTION);

        JacksonDBCollection<Family, String> coll = JacksonDBCollection.wrap(collection, Family.class, String.class);
        // Busco todos los documentos de la colección y los imprimo
        try (DBCursor<Family> cursor = coll.find()) {
            while (cursor.hasNext()) {
                familias.add(cursor.next());

            }
        }

        return familias;
    }


}

