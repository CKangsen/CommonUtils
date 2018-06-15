package com.threetree.baseproject.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {


    private Context mApplicationContext;
    private final Object mDatabaseWrapperLock = new Object();
    private DBWrapper mDBWrapper;

    /**
     *
     *  Version 1
     *  */
    private static final int DATABASE_VERSION = 1;



    private static final String DATABASE_CREATE = "CREATE TABLE " ;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
        mApplicationContext= context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    public DBWrapper getDatabase() {

        synchronized (mDatabaseWrapperLock) {
            if (mDBWrapper == null) {
                mDBWrapper = new DBWrapper(mApplicationContext, getWritableDatabase());
            }
            return mDBWrapper;
        }
    }
}
