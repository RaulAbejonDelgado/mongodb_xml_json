package com.bilbomatica.xml.dao;

import com.bilbomatica.xml.pojo.Family;
import com.bilbomatica.xml.pojo.Person;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
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


    public boolean crear(Family f) throws UnknownHostException {
        boolean resul = false;
        Family fe = new Family();

        BasicDBObject dBObjectFamily;

        DBCollection collection = getConnectionDbAndCollection(DB, COLLECTION);

        long numDocumentos = collection.getCount();
        f.setFamilyId((int) (numDocumentos + 1));
        f.toString();
        dBObjectFamily = toDBObjectToJavaCreate(f);

        WriteResult wr = collection.insert(dBObjectFamily);


        if(numDocumentos < collection.getCount()){

            resul = true;

        }

        return resul;

    }


    private BasicDBObject toDBObjectToJavaCreate(Family f) {

        // Creamos una instancia BasicDBObject
        BasicDBObject dBObjectFamily = new BasicDBObject();

        dBObjectFamily.append("familyId", f.getFamilyId());

        dBObjectFamily.append("nombre", f.getNombre());

        BasicDBObject dBObjectPersona = new BasicDBObject();

        //creo array del tamaño de las personas y si viene null creamos hueco para 1 elemento
        BasicDBObject[] arrayPersonas = new BasicDBObject[f.getPersonas() != null ? f.getPersonas().length  : 1];

        //Si en la creacion de la familia no se espcifica persona se crea uno a null
        //Para evitar errores en la lectura con estructura distinta
        if(f.getPersonas() == null ){

            dBObjectPersona.append("nombre", "");
            dBObjectPersona.append("selfId",0);
            dBObjectPersona.append("familyId",0);
            arrayPersonas[0] = dBObjectPersona;
            dBObjectFamily.append("personas",  arrayPersonas);

        }else{

            for(int i = 0 ; i < f.getPersonas().length; i ++ ){
                dBObjectPersona.append("nombre", f.getPersonas()[i].getNombre());
                dBObjectPersona.append("selfId",f.getPersonas()[i].getselfId());
                dBObjectPersona.append("familyId",f.getFamilyId());

                arrayPersonas[i] = dBObjectPersona ;

            }

            dBObjectFamily.append("personas", arrayPersonas);

        }

        return dBObjectFamily;
    }




}

