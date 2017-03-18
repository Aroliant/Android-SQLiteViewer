package a6un.sqliteviewer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Jacob Samro on 03-Mar-17.
 */
public class SQLiteService extends Service{

    static JSONArray databases = new Databases();
    static Context context;
    static boolean isRunning = false;



    public SQLiteService(Context context,JSONArray databases) {

        this.context = context;
        this.databases = databases;

        isRunning = true;

    }

    private JSONArray getDatabases(){
        return  this.databases;
    }

    public JSONArray getTables(String databaseName) throws Exception{

        JSONArray tables = new JSONArray();

        SQLiteDataHelper sqLiteDataHelper = new SQLiteDataHelper(this.context,databaseName,getVersion(databaseName));

        SQLiteDatabase db = sqLiteDataHelper.getWritableDatabase();

        String query = "SELECT name FROM sqlite_master WHERE type='table'";

        Cursor cursor = db.rawQuery(query,null);

        while (cursor.moveToNext()){
            tables.put(cursor.getString(cursor.getColumnIndex("name")));
        }

        return tables;
    }

    public JSONArray Cursor2JSON(Cursor cursor) {

        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        rowObject.put(cursor.getColumnName(i),
                                cursor.getString(i));
                    } catch (Exception e) {
                        Log.d("READ ERR", e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }

        cursor.close();
        return resultSet;

    }

    public JSONObject getData(String databaseName, String tableName, int offset) throws Exception{

        JSONObject retVal = new JSONObject();
        JSONArray data;
        JSONArray fields = new JSONArray();

        SQLiteDataHelper sqLiteDataHelper = new SQLiteDataHelper(this.context,databaseName,getVersion(databaseName));

        SQLiteDatabase db = sqLiteDataHelper.getWritableDatabase();

        String query = "SELECT * FROM "+ tableName;

        Cursor cursor = db.rawQuery(query,null);

        data = Cursor2JSON(cursor);

        for(int i = 0 ; i < cursor.getColumnCount(); i++){
            fields.put(cursor.getColumnName(i));
        }

        retVal.put("data",data);
        retVal.put("fields",fields);



        return  retVal;

    }


    // Helper Functions

    private int getVersion(String databaseName) throws JSONException{
        for(int i =0 ; i < this.databases.length() ; i++){
            if(this.databases.getJSONObject(i).getString("name").matches(databaseName)){
                return this.databases.getJSONObject(i).getInt("version");
            }
        }
        return 1;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    public void initiate(String host) throws Exception{

        final Socket socket = IO.socket(host);
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {

                socket.emit("connected", "type:service");

            }

        }).on("get:databases", new Emitter.Listener() {

            @Override
            public void call(Object... args) {

                socket.emit("service:send:databases",getDatabases().toString());

            }

        }).on("get:tables", new Emitter.Listener() {

            @Override
            public void call(Object... args) {

                try {

                 JSONObject data = (JSONObject) args[0];

                  socket.emit("service:send:tables",getTables(data.getString("database")).toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }).on("get:table:data", new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {

                        try {

                            JSONObject data = (JSONObject) args[0];

                            socket.emit("service:send:data",getData(data.getString("database"),data.getString("table"),0).toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {}

        });
        socket.connect();
    }
}
