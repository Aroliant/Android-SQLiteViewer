package a6un.sqliteviewer;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Random;

/**
 * Created by Jacob Samro on 03-Mar-17.
 */

public class CreateTestDatabase extends SQLiteOpenHelper {



    private static final String DATABASE_NAME = "test";
    private static final int DATABASE_VERSION = 1;



    public CreateTestDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_POSTS_TABLE = "CREATE TABLE posts(id INTEGER PRIMARY KEY, title TEXT)";

        db.execSQL(CREATE_POSTS_TABLE);


        String[] articles= {"Create", "Edit", "Add",};
        String[] noun= {"new", "a", "the"};
        String[] verb= {"Device", "Cat", "Rain"};

        Random rand = new Random();

        for (int i = 0; i < 10 ; i++){
            ContentValues values = new ContentValues();
            values.put("id", i);
            String title = articles[rand.nextInt(2)] + noun[rand.nextInt(2)] + verb[rand.nextInt(2)];
            values.put("title", title);

            db.insertOrThrow("posts", null, values);
            db.setTransactionSuccessful();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {

            db.execSQL("DROP TABLE IF EXISTS posts");
            onCreate(db);
        }
    }


}

