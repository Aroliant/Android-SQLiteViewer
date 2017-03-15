package a6un.sqliteviewer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Jacob Samro on 03-Mar-17.
 */
public class SQLiteService extends Service {

    static ArrayList<String> databases = new ArrayList<String>();
    static Context context;


    public SQLiteService(Context context,ArrayList<String> databases) {

        this.context = context;
        this.databases = databases;

        Log.d("DATABASES : " , databases.toString());

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
