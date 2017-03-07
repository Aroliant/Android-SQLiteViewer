package a6un.sqliteviewer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Jacob Samro on 03-Mar-17.
 */
public class SQLiteService extends Service {

    static String databases[];


    private SQLiteService(String databases[]) {

        this.databases = databases;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
