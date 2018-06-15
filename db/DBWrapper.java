package com.threetree.baseproject.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteTransactionListener;


public class DBWrapper {

    private SQLiteDatabase mDatabase;
    private Context mContext;

    DBWrapper(final Context context, final SQLiteDatabase db){
        mContext = context;
        mDatabase = db;
    }

    public Cursor query(String table, String[] columns, String selection,
                        String[] selectionArgs, String groupBy, String having,
                        String orderBy){
        Cursor query = mDatabase.query(table,columns,selection,selectionArgs,groupBy,having,orderBy);
        return query;
    }

    public long insert(String table, String nullColumnHack, ContentValues values){
        long row = mDatabase.insert(table,nullColumnHack,values);
        return row;
    }

    public long insertWithOnConflict(String table, String nullColumnHack, ContentValues values, int conflictAlgorithm){
        long row = mDatabase.insertWithOnConflict(table,nullColumnHack,values,conflictAlgorithm);
        return row;
    }

    public long delete(String table, String whereClause, String[] whereArgs){
        long row = mDatabase.delete(table,whereClause,whereArgs);
        return row;
    }

    public long update(String table, ContentValues values, String whereClause, String[] whereArgs){
        long row = mDatabase.update(table,values,whereClause,whereArgs);
        return row;
    }

    public void beginTransaction(){
        mDatabase.beginTransaction();
    }

    public void beginTransactionWithListener(SQLiteTransactionListener sqLiteTransactionListener){
        mDatabase.beginTransactionWithListener(sqLiteTransactionListener);
    }

    public void endTransaction(){
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }
}
