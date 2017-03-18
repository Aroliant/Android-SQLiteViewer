package a6un.sqliteviewer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jacob Samro on 18-Mar-17.
 */

public class Databases extends JSONArray {
    JSONArray databases = new JSONArray();

    public void add(String databaseName, int version) throws JSONException {
        JSONObject database = new JSONObject();
        database.put("name",databaseName);
        database.put("version",version);

        databases.put(database);

    }

    public JSONArray getDatabases(){
        return databases;
    }
}
