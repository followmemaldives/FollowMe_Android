package com.asif.followme;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.asif.followme.MyAccount.MyActivity;
import com.asif.followme.util.ActionReceiver;
import com.asif.followme.util.AppConstants;
import com.asif.followme.util.AppUtils;
import com.asif.followme.util.SettingsPreferences;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class FireMessagingService extends FirebaseMessagingService {
    public static Ringtone r;
    public static NotificationManager notificationManager;
    public FireMessagingService() {
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...
        Context context = this;
//        System.out.println("Remote MEssage:"+remoteMessage.toString());
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        String token = FirebaseInstanceId.getInstance().getToken();
//        System.out.println("A message received from Token: "+token);
        SimpleDateFormat dbformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",java.util.Locale.getDefault());	//database
        SimpleDateFormat  dformat =  new SimpleDateFormat("HH:mm",java.util.Locale.getDefault());	//display format
        String mtime;
        String msg;
        String message;
     //   int device_id = Integer.parseInt(remoteMessage.getData().get("device_id"));
    //    System.out.println("Device ID:"+device_id);
        try {
            mtime = remoteMessage.getData().get("mtime");
            dbformat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = dbformat.parse(mtime);
            mtime = dformat.format(date);
            msg = remoteMessage.getData().get("msg");
            message = mtime+" "+msg;
            //sendNotification(context,remoteMessage, message);
            sendNotification3(context,remoteMessage,message);

        } catch (Exception e) {
            mtime="";
            e.printStackTrace();
        }




       // Log.d("Log", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.d("Log", "Message data payload: " + remoteMessage.getData());

//            if (/* Check if data needs to be processed by long running job */ false) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
//            } else {
                // Handle message within 10 seconds
//                handleNow();
        //        if(!SettingsPreferences.getUserName(context).equalsIgnoreCase("")){	//avoid receiving notification when logged out
                    //AppUtils.displayMultipleNotification(context,message, AppConstants.NOTIFICATION_ID,alertSound,alertVibrate,alarm_type);
//                    sendNotification(context,remoteMessage, message);
        //        }
//            }

//        }

        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d("Log", "Message Notification Body: " + remoteMessage.getNotification().getBody());
 //       }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void sendNotification3(Context context,RemoteMessage remoteMessage, String message) {
        Log.i("FCM",message);
        int id = AppConstants.NOTIFICATION_ID;
        int alert_public = 0;
        int alert_type = 1;
        boolean alertSound = true;
        try {
            alert_type = Integer.parseInt(remoteMessage.getData().get("type"));
            alert_public = Integer.parseInt(remoteMessage.getData().get("public"));
        } catch (Exception e) {
            e.printStackTrace();

        }

        if (alert_public == 1) {
            alertSound = true;
        } else {   // check user settings below...
            if (alert_type == 2) {
                alertSound = SettingsPreferences.getSoundMinor(context);
            } else if (alert_type == 3) {
                alertSound = SettingsPreferences.getSoundMajor(context);
            } else if (alert_type == 4) {    //hire notification
                System.out.println("********************************************************");
                System.out.println("Alert Type:" + alert_type);
                //alertSound =   (SettingsPreferences.getAlertMajor(context) & 2) == 2 ? true : false;
                //alertVibrate = (SettingsPreferences.getAlertMajor(context) & 4) == 4 ? true : false;
                alertSound = SettingsPreferences.getSoundHire(context);
                System.out.println("AlertSound:" + alertSound);
                SettingsPreferences.setHireAlertCount(context);
            } else {
                //alertSound =   (SettingsPreferences.getAlertMove(context) & 2) == 2 ? true : false;
                alertSound = SettingsPreferences.getSoundMove(context);
                //alertVibrate = (SettingsPreferences.getAlertMove(context) & 4) == 4 ? true : false;
            }
        }

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        //       Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
        inboxStyle.setBigContentTitle("FollowMe Tracking Service");
        //  inboxStyle.setSummaryText(message);
        //   inboxStyle.setBigContentTitle(message);
        JSONArray jarray = SettingsPreferences.setNotifications(context, message);
        int n = 0;
        for (int i = 0; i < jarray.length(); i++) {
            try {
                if (!jarray.getString(i).equalsIgnoreCase("")) {
                    inboxStyle.addLine(jarray.getString(i));
                    n++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        NotificationManager notificationManager;
        NotificationCompat.Builder mBuilder;
        if (alertSound) {
            Log.i("GCM", "WITH SOUND");
            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                mBuilder = new NotificationCompat.Builder(this, AppConstants.CHANNEL_HIGH);
                mBuilder
                        .setContentTitle("FollowMe Tracking Service")
                        .setContentText(message)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_large))
                        .setSmallIcon(R.drawable.icon_small)
                        .setPriority(Notification.DEFAULT_ALL)
                        .setLights(Color.BLUE, 1000, 1000)
                        .setSound(defaultSoundUri)
                        .setStyle(inboxStyle)
                        .setNumber(n);
            notificationManager = AppUtils.getManager1(context);
            //AppUtils.getManager1(context).notify(101, mBuilder.build());

        } else {
            Log.i("GCM", "NO SOUND");
                mBuilder = new NotificationCompat.Builder(this, AppConstants.CHANNEL_LOW);
                mBuilder
                        .setContentTitle("FollowMe Tracking Service")
                        .setContentText(message)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_large))
                        .setSmallIcon(R.drawable.icon_small)
                        .setStyle(inboxStyle)
                        .setNumber(n);
            notificationManager = AppUtils.getManager2(context);
            //AppUtils.getManager2(context).notify(101, mBuilder.build());

        }
        Intent resultIntent = new Intent(context, MyActivity.class);
        Intent clearIntent = new Intent(context, ActionReceiver.class);
        clearIntent.setAction("a_notification_from_server");
        clearIntent.putExtra("notification_id", id);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(HomeActivity.class);

        /* Adds the Intent that starts the Activity to the top of the stack */
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), id, clearIntent, 0);
       mBuilder.setContentIntent(resultPendingIntent);
       mBuilder.setDeleteIntent(pendingIntent);


        //           NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
       notificationManager.notify(AppConstants.NOTIFICATION_ID, mBuilder.build());

    }



    private void sendNotification2(Context context,RemoteMessage remoteMessage, String message) {
        int id = AppConstants.NOTIFICATION_ID;
        int alert_public = 0;
        int alert_type = 1;
        try {
            alert_type = Integer.parseInt(remoteMessage.getData().get("type"));
            // alert_public = Integer.parseInt(remoteMessage.getData().get("public"));
        } catch (Exception e) {
            e.printStackTrace();

        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            System.out.println("Oreo...");
            //Notification.Builder nb = getChannelNotification2("Test Title", message);
                Notification.Builder mBuilder = new Notification.Builder(this, AppConstants.CHANNEL_HIGH);
                mBuilder
                        .setContentTitle("FollowMe Tracking Service")
                        .setContentText(message)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_large))
                        .setSmallIcon(R.drawable.icon_small);


            AppUtils.getManager1(context).notify(101, mBuilder.build());

        } else {


            Log.i("GCM", message);
 /*       NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

*/
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            //       Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
            inboxStyle.setBigContentTitle("FollowMe Tracking Service");
            //  inboxStyle.setSummaryText(message);
            //   inboxStyle.setBigContentTitle(message);
            JSONArray jarray = SettingsPreferences.setNotifications(context, message);
            int n = 0;
            for (int i = 0; i < jarray.length(); i++) {
                try {
                    if (!jarray.getString(i).equalsIgnoreCase("")) {
                        inboxStyle.addLine(jarray.getString(i));
                        n++;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            Boolean alertSound = true;
            Log.i("GCM", "A");
            if (alert_public == 1) {
                alertSound = true;
            } else {   // check user settings below...
                if (alert_type == 2) {
                    if (SettingsPreferences.getAlertMinor(context) == false) {
                        return;
                    }
                    alertSound = SettingsPreferences.getSoundMinor(context);
                } else if (alert_type == 3) {
                    if (SettingsPreferences.getAlertMajor(context) == false) {
                        return;
                    }
                    alertSound = SettingsPreferences.getSoundMajor(context);
                } else if (alert_type == 4) {    //hire notification
                    System.out.println("********************************************************");
                    System.out.println("Alert Type:" + alert_type);
                    //alertSound =   (SettingsPreferences.getAlertMajor(context) & 2) == 2 ? true : false;
                    //alertVibrate = (SettingsPreferences.getAlertMajor(context) & 4) == 4 ? true : false;
                    alertSound = SettingsPreferences.getSoundHire(context);
                    System.out.println("AlertSound:" + alertSound);
                    SettingsPreferences.setHireAlertCount(context);
                } else {
                    if (SettingsPreferences.getAlertMove(context) == false) {
                        return;
                    }
                    //alertSound =   (SettingsPreferences.getAlertMove(context) & 2) == 2 ? true : false;
                    alertSound = SettingsPreferences.getSoundMove(context);
                    //alertVibrate = (SettingsPreferences.getAlertMove(context) & 4) == 4 ? true : false;
                }
            }

            Log.i("GCM", "B");

            NotificationCompat.Builder mBuilder;
            if (alertSound) {
                Log.i("GCM", "WITH SOUND");
            /*    mBuilder = new NotificationCompat.Builder(this, AppConstants.NOTIFICATION_CHANNEL_HIGH);
                mBuilder
                        .setContentTitle("FollowMe Tracking Service")
                        .setContentText(message)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_large))
                        .setSmallIcon(R.drawable.icon_small)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setLights(Color.BLUE, 1000, 1000)
                        .setStyle(inboxStyle)
                        .setNumber(n);
                        */
            } else {
                Log.i("GCM", "NO SOUND");
             /*   mBuilder = new NotificationCompat.Builder(this, AppConstants.NOTIFICATION_CHANNEL_LOW);
                mBuilder
                        .setContentTitle("FollowMe Tracking Service")
                        .setContentText(message)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_large))
                        .setSmallIcon(R.drawable.icon_small)
                        .setStyle(inboxStyle)
                        .setNumber(n);
                        */
            }
            //NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, AppConstants.NOTIFICATION_CHANNEL_HIGH)
/*                mBuilder
                .setContentTitle("FollowMe Tracking Service")
                .setContentText(message)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_large))
                .setSmallIcon(R.drawable.icon_small)
                .setPriority(Notification.PRIORITY_HIGH)
                .setLights(Color.BLUE,1000, 1000)
                .setStyle(inboxStyle)
                .setNumber(n);
                */
/*        try {
            mBuilder.setContentText(jarray.getString(0));
            mBuilder.setTicker(jarray.getString(0));
        } catch (Exception e) {

        }
*/

            Intent resultIntent = new Intent(context, MyActivity.class);
            Intent clearIntent = new Intent(context, ActionReceiver.class);
            clearIntent.setAction("a_notification_from_server");
            clearIntent.putExtra("notification_id", id);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(HomeActivity.class);

            /* Adds the Intent that starts the Activity to the top of the stack */
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), id, clearIntent, 0);
 //           mBuilder.setContentIntent(resultPendingIntent);
 //           mBuilder.setDeleteIntent(pendingIntent);


 //           NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
 //           notificationManager.notify(AppConstants.NOTIFICATION_ID, mBuilder.build());
        }

    }

    /*
    private void sendNotification(Context context, RemoteMessage remoteMessage, String message) {
//        RemoteMessage.Notification notification = remoteMessage.getNotification();
//        Intent intent = new Intent(this, HomeActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                PendingIntent.FLAG_ONE_SHOT);
        int id = AppConstants.NOTIFICATION_ID;
        int alert_public =0;
        int alert_type = 1;
        try {
            alert_type = Integer.parseInt(remoteMessage.getData().get("type"));
            alert_public = Integer.parseInt(remoteMessage.getData().get("public"));
        } catch (Exception e) {
            e.printStackTrace();

        }
        //String message = remoteMessage.getData().get("message");
       NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
 //       Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
        inboxStyle.setBigContentTitle("FollowMe Tracking Service");
        JSONArray jarray= SettingsPreferences.setNotifications(context, message);
        int n=0;
        for (int i = 0; i < jarray.length(); i++) {
            try {
                if(!jarray.getString(i).equalsIgnoreCase("")){
                    inboxStyle.addLine(jarray.getString(i));
                    n++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Boolean alertSound = true;
        Boolean alertVibrate = false;
        if(alert_public == 1){
            alertSound = true;
            alertVibrate = true;
        } else {   // check user settings below...
            if(alert_type == 2){
                if(SettingsPreferences.getAlertMinor(context)== false){
                    return;
                }
                //alertSound =   (SettingsPreferences.getAlertMinor(context) & 2) == 2 ? true : false;
                //alertVibrate = (SettingsPreferences.getAlertMinor(context) & 4) == 4 ? true : false;
                alertSound = SettingsPreferences.getSoundMinor(context);
            } else
            if(alert_type == 3) {
                if(SettingsPreferences.getAlertMajor(context)==false){
                    return;
                }
                //alertSound =   (SettingsPreferences.getAlertMajor(context) & 2) == 2 ? true : false;
                //alertVibrate = (SettingsPreferences.getAlertMajor(context) & 4) == 4 ? true : false;
                alertSound = SettingsPreferences.getSoundMajor(context);
            } else
            if(alert_type == 4){    //hire notification
                System.out.println("********************************************************");
                System.out.println("Alert Type:"+alert_type);
                    //alertSound =   (SettingsPreferences.getAlertMajor(context) & 2) == 2 ? true : false;
                    //alertVibrate = (SettingsPreferences.getAlertMajor(context) & 4) == 4 ? true : false;
                    alertSound = SettingsPreferences.getSoundHire(context);
                    System.out.println("AlertSound:"+alertSound);
                    SettingsPreferences.setHireAlertCount(context);
            } else {
                if(SettingsPreferences.getAlertMove(context)==false){
                    return;
                }
                //alertSound =   (SettingsPreferences.getAlertMove(context) & 2) == 2 ? true : false;
                alertSound = SettingsPreferences.getSoundMove(context);
                //alertVibrate = (SettingsPreferences.getAlertMove(context) & 4) == 4 ? true : false;
            }
        }
        System.out.println("SOUND:"+alertSound);
        alertVibrate = alertSound;

        Notification note;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_LOW = "followme_low";
        String NOTIFICATION_CHANNEL_HIGH = "followme_high";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel;
           // if(alertSound) {
                notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_HIGH, "FollowMe Tracking Service", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.BLUE);
           // } else{
           //     notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_LOW, "FollowMe Tracking Service", NotificationManager.IMPORTANCE_LOW);
           // }

            // Configure the notification channel.
            notificationChannel.setDescription("FollowMe Tracking Service");
//            notificationChannel.setVibrationPattern(new long[]{0, 500, 500, 500});
//            notificationChannel.enableVibration(alertSound);
            notificationManager.createNotificationChannel(notificationChannel);

        }
        NotificationCompat.Builder mBuilder;
        if(alertSound) {
            mBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_HIGH);
        } else {
            mBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_LOW);
        }

        mBuilder.setContentTitle("FollowMe Tracking Service")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_large))
                .setSmallIcon(R.drawable.icon_small)
                .setPriority(Notification.PRIORITY_HIGH)
                .setLights(Color.BLUE,1000, 1000)
                .setStyle(inboxStyle)
                .setNumber(n);

        try {
            mBuilder.setContentText(jarray.getString(0));
            mBuilder.setTicker(jarray.getString(0));
        } catch (Exception e) {

        }




        Intent resultIntent = new Intent(context, MyActivity.class);
        Intent clearIntent = new Intent(context, ActionReceiver.class);
        clearIntent.setAction("a_notification_from_server");
        clearIntent.putExtra("notification_id", id);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(HomeActivity.class);


        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), id, clearIntent, 0);
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setDeleteIntent(pendingIntent);
        if(alert_type == 3){
            //System.out.println("A: Major Alarm");
            if(r==null){
                //System.out.println("B: r=null");
                if(alertSound){
                    // System.out.println("C: AlartSound true");
                    mBuilder.addAction(R.drawable.ic_action_alarms, "Stop Alarm", pendingIntent);
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                    r = RingtoneManager.getRingtone(context, notification);
                    r.play();
                } else {
                    //System.out.println("C2: AlartSound false");
                    //System.out.println("C3: AlartSound:"+alertSound);
                    //int aS =   SettingsPreferences.getAlertMajor(context);
                    //  System.out.println("C4: AlartSound Vallue:"+aS);

                }
            } else if(!r.isPlaying() && alertSound){
                // System.out.println("D: r is not Playing and AlartSound true");
                mBuilder.addAction(R.drawable.ic_action_alarms, "Stop Alarm", pendingIntent);
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                r = RingtoneManager.getRingtone(context, notification);
                r.play();
            }
            note = mBuilder.build();
            if(alertSound){note.defaults |= Notification.DEFAULT_VIBRATE;}

        } else {
            note = mBuilder.build();
            if(alertSound){note.defaults |= Notification.DEFAULT_SOUND;}
            if(alertSound){note.defaults |= Notification.DEFAULT_VIBRATE;}
        }

//        notificationManager =  (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

//	      mNotificationManager.notify(id, mBuilder.build());
        notificationManager.notify(id, note);







        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.icon_large))
                .setSmallIcon(R.drawable.icon_small)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("message"))
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());

    }

*/


    }


