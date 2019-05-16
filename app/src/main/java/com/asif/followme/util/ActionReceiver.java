/**
 * 
 */
package com.asif.followme.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.asif.followme.FireMessagingService;

/***
/**
 * The receiver to get sms receive action and boot completed
 */
public class ActionReceiver extends BroadcastReceiver {
	String number = "";

	@Override
	public void onReceive(final Context context, Intent intent) {
		String action = intent.getAction();
		//System.out.println(action);
		if(action.equalsIgnoreCase(AppConstants.STOP_SERVICE_ACTION)){
			//System.out.println("Need to stop Self tracking??");
			SettingsPreferences.setTrackMe(context, "OFF");
//enable this			context.stopService(new Intent(context, LocationService.class));
		} else {
		//Log.i("Notification","Cleared");
		int notificationId = intent.getExtras().getInt("notification_id");
		//String action = intent.getAction();
		if(notificationId==AppConstants.NOTIFICATION_ID){
			//System.out.println("Lets Clear "+notificationId);
			SettingsPreferences.clearNotifications(context);
			if(FireMessagingService.notificationManager!=null) FireMessagingService.notificationManager.cancel(notificationId);

		}
		}
		//String action = intent.getAction();
		//System.out.println(action);
	}
	
}
