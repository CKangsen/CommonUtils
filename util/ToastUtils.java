package com.threetree.baseproject.util;

import android.content.Context;
import android.widget.Toast;


/**
 * Toast Tools
 *
 * Created by long on 2016/1/27.
 * 防止连击
 */
public class ToastUtils {

    private static Toast toast;

    public static void show(Context context, String message){
        if (toast == null) {
            toast = Toast.makeText(context,
                    message,
                    4*1000);
        } else {
            toast.setText(message);
        }
        toast.show();
    }


    public static void showLong(Context context, String message){
        if (toast == null) {
            toast = Toast.makeText(context,
                    message,
                    4*1000);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * tangtt 使用application 作为Toast的Context
     * @param message
     */
    public static void show(String message) {
        if (toast == null) {
            toast = Toast.makeText(App.getMyApplication(),
                    message,
                    4*1000);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * tangtt 使用application 作为Toast的Context
     * @param message
     */
    public static void showLong(String message){
        if (toast == null) {
            toast = Toast.makeText(App.getMyApplication(),
                    message,
                    4*1000);
        } else {
            toast.setText(message);
        }
        toast.show();
    }
}
