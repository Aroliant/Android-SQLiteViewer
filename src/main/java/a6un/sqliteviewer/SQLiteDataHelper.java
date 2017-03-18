package a6un.sqliteviewer;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Random;

/**
 * Created by Jacob Samro on 18-Mar-17.
 */

public class SQLiteDataHelper extends SQLiteOpenHelper {

    public static String CREATE_TEST_TABLE = "CREATE TABLE posts (id INT PRIMARY KEY UNIQUE, title TEXT)";

    public SQLiteDataHelper(Context context, String dbName, int verison) {
        super(context, dbName , null, verison);
    }


    public void onCreate(SQLiteDatabase db) {

    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static void createTestDB(SQLiteDatabase db){

        try{

            db.execSQL(SQLiteDataHelper.CREATE_TEST_TABLE);

            String[] articles= {"Create ", "Edit ", "Add",};
            String[] noun= {"new ", "a ", "the"};
            String[] verb= {"Device ", "Cat ", "Rain"};

            Random rand = new Random();

            for (int i = 0; i < 10 ; i++){
                ContentValues values = new ContentValues();
                values.put("id", i);
                String title = articles[rand.nextInt(2)] + noun[rand.nextInt(2)] + verb[rand.nextInt(2)];
                values.put("title", title);

                db.insert("posts", null, values);

            }

            db.endTransaction();

        }catch (Exception e){
            e.printStackTrace();
        }


    }


}
