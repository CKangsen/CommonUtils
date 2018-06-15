package com.threetree.baseproject.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;


import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by cks on 18-4-26.
 */

public class NotificationUtil {

    public static void newSystemMessageNotify(Context context){

        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        String channelID = "";
        String channelName = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelID, channelName,NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(context, .class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int)(Math.random()*100)+9,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        int smallIcon ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            smallIcon = ;
        } else {
            smallIcon = ;
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,channelID);
        mBuilder.setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setOngoing(false)
                .setTicker(context.getString(R.string.))
                .setSmallIcon(smallIcon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.))
                .setContentTitle(context.getString(R.string.))
                .setContentText(context.getString(R.string.))
                .setContentIntent(pendingIntent)
                .setChannelId(channelID);
        Notification notification = mBuilder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        manager.notify(100, notification);
    }

    public static void newSystemMessageNotify(Context context ,String msg){

        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        String channelID = "";
        String channelName = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelID, channelName,NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(context, .class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int)(Math.random()*100)+9,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        int smallIcon ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            smallIcon = R.mipmap.;
        } else {
            smallIcon = R.mipmap.;
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,channelID);
        mBuilder.setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setOngoing(false)
                .setTicker(context.getString(R.string.))
                .setSmallIcon(smallIcon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.))
                .setContentTitle(context.getString(R.string.))
                .setContentText(msg)
                .setContentIntent(pendingIntent)
                .setChannelId(channelID);
        Notification notification = mBuilder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        manager.notify((int)(Math.random()*100)+11, notification);
    }
}
