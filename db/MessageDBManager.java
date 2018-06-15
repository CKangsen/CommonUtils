package com.threetree.baseproject.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE;

/**
 * Created by cks on 18-4-20.
 */

public class MessageDBManager {

    public static final String TAG = MessageDBManager.class.getSimpleName();
    public static final int QUERY_MSG_COUNT = 10;

    private DBWrapper mDBWrapper;
    private volatile static MessageDBManager DBManagerInstance;
    private Context mContext;

    private ThreadPoolExecutor mThreadPoolExecutor;


    MessageDBManager(Context context) {
        mDBWrapper = new DBHelper(context).getDatabase();
        mContext=context;
        mThreadPoolExecutor = new ThreadPoolExecutor(20, Integer.MAX_VALUE,
                30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(Integer.MAX_VALUE));
    }

    public static MessageDBManager getInstance(Context context) {
        if (null == DBManagerInstance) {
            synchronized (MessageDBManager.class) {
                if (null == DBManagerInstance) {
                    DBManagerInstance = new MessageDBManager(context);
                }
            }
        }
        return DBManagerInstance;
    }

    public void insertMessage() {
        try {
            mThreadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {

                }
            });
        } catch (Exception e){
        }

    }


}
