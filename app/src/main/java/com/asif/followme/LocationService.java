/**
	Copyright (c) 2010 CommonsWare, LLC
	Copyright (c) 2011 Birkett Enterprise Ltd
	
	Licensed under the Apache License, Version 2.0 (the "License"); you may
	not use this file except in compliance with the License. You may obtain
	a copy of the License at
		http://www.apache.org/licenses/LICENSE-2.0
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */

package com.asif.followme;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.*;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.Toast;
import com.asif.followme.util.ActionReceiver;
import com.asif.followme.util.AppConstants;
import com.asif.followme.util.SettingsPreferences;

import java.io.*;
import java.net.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Service providing the guts of the location polling
 * engine. Uses a WakeLock to ensure the CPU stays on while
 * the location lookup is going on. Handles both successful
 * and timeout conditions.
 * 
 * Those wishing to leverage this service should do so via
 * the LocationPoller class.
 */
public class LocationService extends Service {
public static final String BROADCAST_ACTION = "Hello World";
private static final int TWO_MINUTES = 1000 * 60 * 2;
	private static final String TAG = "Location Tag";
	//private final int POLL_TIME = 60000;
private long POLL_TIME;
private final int POLL_DISTANCE = 0;
//public LocationManager locationManager1,locationManager2;
public MyLocationListener locationListener;
public Location previousBestLocation = null;

Intent intent;
int counter = 0;


private Socket socket;
public static String subscriber_id,provider;
public static int speed, heading;
static DecimalFormat fiveDForm = new DecimalFormat("#.#####");
static DecimalFormat oneDForm = new DecimalFormat("#.#");
private static Socket nsocket; //Network Socket
static SocketAddress sockaddr;
public static String latitude;
	public static String longitude;
	public static String locationMedia;
	public static String cinr;
	public static String rss;
	public static String cid;
	public static String isp; //operator
	public static int battery;
    static SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
    public static PrintWriter outs;
    public static BufferedReader ins;
    public static Location location;
    public int networkUpdate;
    public String island;
	private Notification notification;
	private NotificationManager notificationManager;
	public static Context context;
	private AsyncTask<Void, Void, String> SocketAsyncTask;
	private Boolean gpsTimeOut = false;
	private Boolean isGPSDone;
	private Boolean isNetworkDone;
	private Boolean isInit = false;
	TelephonyManager        Tel;
	MyPhoneStateListener    myPhoneListener;
	private PowerManager.WakeLock wakeLock;
	private String ip;
	private int port;
	private long lastUpdated = 0;
	private int isCharging;
	
	
	private LocationManager locationManager; 
/*	
	private NotificationManager mNM;
	private Method mSetForeground;
	private Method mStartForeground;
	private Method mStopForeground;
	private Object[] mSetForegroundArgs = new Object[1];
	private Object[] mStartForegroundArgs = new Object[2];
	private Object[] mStopForegroundArgs = new Object[1];
	private static final Class<?>[] mSetForegroundSignature = new Class[] {
	    boolean.class};
	private static final Class<?>[] mStartForegroundSignature = new Class[] {
	    int.class, Notification.class};
	private static final Class<?>[] mStopForegroundSignature = new Class[] {
	    boolean.class};
*/

@Override
public void onCreate() {
    super.onCreate();
    intent = new Intent(BROADCAST_ACTION); 
	  subscriber_id=SettingsPreferences.getIMEI(this);
	  ip = SettingsPreferences.getTrackIP(this);
	  port = SettingsPreferences.getTrackPort(this);
	  //generateNotification(this, "Self Tracking Enabled");
	  showNotification();
	  networkUpdate=-1;
	  context = this;
  	myPhoneListener   = new MyPhoneStateListener();

	POLL_TIME = SettingsPreferences.getUpdateIntervalTime(context);
	try {
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return;
		}
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();
		Tel = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		Tel.listen(myPhoneListener ,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		isp = Tel.getNetworkOperator();
		this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
//		this.registerReceiver(this.mChargingInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		Tel.listen(myPhoneListener ,PhoneStateListener.LISTEN_CELL_LOCATION);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, POLL_TIME, POLL_DISTANCE, locationListener);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, POLL_TIME, POLL_DISTANCE, locationListener);
		Location lastKnownLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (lastKnownLocationGPS != null) {
			postLocation(lastKnownLocationGPS);
		} else {
			Location  lastKnownLocationOther=  locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
			if(lastKnownLocationOther != null){
				postLocation(lastKnownLocationOther);
			}
		}
	} catch (Exception ex) {
		Log.i(TAG, "fail to remove location listener, ignore", ex);
	}

	PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
	wakeLock= pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getCanonicalName());
	wakeLock.acquire();

}


@Override
public int onStartCommand(Intent intent, int flags, int startId) {
	System.out.println("Service onStart Command...");
//	POLL_TIME = 20000;
	//System.out.println("POLL TIME: "+POLL_TIME);
	// handler.postDelayed(scheduleLocation, 60000);
	//if(isInit == false){
	//	initGPS();
	//}
    return START_STICKY;
}

@Override
public IBinder onBind(Intent intent) {
    return null;
}

@Override
public void onDestroy() {
   // handler.removeCallbacks(sendUpdatesToUI);     
    super.onDestroy();
    Log.v("STOP_SERVICE", "DONE");
	locationManager.removeUpdates(locationListener);
	SettingsPreferences.setTrackMe(context,"off");
	this.unregisterReceiver(mBatInfoReceiver);

//	locationManager1.removeUpdates(listener); 
//	locationManager2.removeUpdates(listener); 
//	handler.removeCallbacks(showTime);
//	handler2.removeCallbacks(scheduleLocation);
//	isInit=false;
    stopForeground(true);
    wakeLock.release();
}  


/*private Runnable scheduleLocation = new Runnable() {
    public void run() {
        System.out.println("Schedule Start:"+counts+" Poll Time:"+POLL_TIME);
    	initGPS();
    	//handler2.postDelayed(this, POLL_TIME);
    }
}; 

private Runnable showTime = new Runnable() {
    public void run() {
     counts++;
     //System.out.println("TimeOut Count:"+counts+" Poll Time:"+POLL_TIME);
     if( counts > 12 ){	//6 x 5000 = 30 second
    	 System.out.println("Time OUT.....");
    	 gpsTimeOut=true;
    	 counts=0;
  	    locationManager1.removeUpdates(listener); 
 	    locationManager2.removeUpdates(listener); 
//    	 bestLocation = getCurrentLocation();
//    	 flagGetGPSDone = true;
//    	 flagNetworkDone = true;
      }         
    // bestLocation = getCurrentLocation();
     
	if (gpsTimeOut == false){
    	 handler.postDelayed(this, 5000);
	//}else{

    //	 postLocation(bestLocation);
    	// Job Done, Call next Step
         //    nextStep(bestLocation );
      //****************************************************************************************
     }
    }
}; 


private void initGPS(){
	System.out.println("Lets Init GPS...........");
	   locationManager1 = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	   locationManager2 = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	   listener = new MyLocationListener();        
	   locationManager1.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
	   locationManager2.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
		handler2.postDelayed(scheduleLocation, POLL_TIME);
		handler.postDelayed(showTime, 5000);
		isGPSDone=false;
		isNetworkDone=false;
		gpsTimeOut=false;
		isInit=true;
	
}
private void stopLocation(){
	locationManager1.removeUpdates(listener); 
	locationManager2.removeUpdates(listener); 
	handler.removeCallbacks(showTime);
	handler2.removeCallbacks(scheduleLocation);
	isInit=false;
	
}
*/
/*	private void initGPS(){
		System.out.println("Init GPS....");
		myLocationManager01 = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		startAllUpdate();
		handler.postDelayed(showTime, 5000);
		handler2.postDelayed(scheduleLocation, POLL_TIME);
		 
		flagGPSEnable = myLocationManager01.isProviderEnabled("gps");
		flagNetworkEnable = myLocationManager01.isProviderEnabled("network");
		if(!flagGPSEnable){
			flagGetGPSDone = true;
		}else{
			flagGetGPSDone = false;
		}
		if(!flagNetworkEnable){
			flagNetworkDone = true;
		}else{
			flagNetworkDone = false;
		}
		bestLocation  = null;
		counts    = 0;
	} 
*/ 

	//Turn on the  GPS NETWORK update
/*	public void startAllUpdate(){
	 myLocationManager01.requestLocationUpdates("gps", 0, 0, mLocationListener01);
	 myLocationManager01.requestLocationUpdates("network", 0, 0, mLocationListener02);    
	}

	//Turn of  GPS NETWORK update
	public void stopAllUpdate(){
		System.out.println("Stop All Updates...");
	 myLocationManager01.removeUpdates(mLocationListener01);
	 myLocationManager01.removeUpdates(mLocationListener02);    
	} 
	public final LocationListener mLocationListener01 = new LocationListener(){
	    public void onLocationChanged(Location location){
	     OnGPSChange();
	    }
	    public void onProviderDisabled(String provider){}
	    public void onProviderEnabled(String provider){}
	    public void onStatusChanged(String provider, int status, Bundle extras){}
	}; 

	public final LocationListener mLocationListener02 = new LocationListener(){
	    public void onLocationChanged(Location location){
	     OnNetworkChange();
	    }
	    public void onProviderDisabled(String provider){}
	    public void onProviderEnabled(String provider){}
	    public void onStatusChanged(String provider, int status, Bundle extras){}
	}; 

	private void OnGPSChange(){
		System.out.println("GPS Changed...");
	 flagGetGPSDone = true;
	 flagNetworkDone = true;
	 stopAllUpdate(); 
	}

	private void OnNetworkChange(){
		System.out.println("Network Changed...");
	 flagNetworkDone = true;
	 myLocationManager01.removeUpdates(mLocationListener02); 
	} 

	private Location getCurrentLocation(){
		System.out.println("Let get current Locataion....");
		Location retLocation = null;
		if ((flagGetGPSDone==true && flagNetworkDone==true)){
			System.out.println("Both Done or Timeout....");
			culocationGPS = myLocationManager01.getLastKnownLocation("gps");
			culocationNetwork = myLocationManager01.getLastKnownLocation("network");
			if (culocationGPS == null && culocationNetwork == null){
				retLocation = myLocationManager01.getLastKnownLocation("passive");
				if(retLocation == null){
					retLocation = new Location("passive");
				}
			}else{
				//culocationGPS culocationNetwork
				if(isBetterLocation(culocationGPS,culocationNetwork)){
					retLocation = culocationGPS;
				}else{
					retLocation = culocationNetwork;
				}    
			}
			stopAllUpdate(); 
		}
		return retLocation;   
	} 


*/



protected boolean isBetterLocation(Location location, Location currentBestLocation) {
	System.out.println("isBetterLocation, INTERVAL:"+POLL_TIME);
    if (currentBestLocation == null) {
        // A new location is always better than no location
    	System.out.println("isBetter: currentBestLocation is null");
        return true;
    }

    // Check whether the new location fix is newer or older
    long timeDelta = location.getTime() - currentBestLocation.getTime();
    boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
    boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
    boolean isNewer = timeDelta > 0;

    // If it's been more than two minutes since the current location, use the new location
    // because the user has likely moved
    if (isSignificantlyNewer) {
    	System.out.println("isBetter: isSignificantlyNewer");
       return true;
    // If the new location is more than two minutes older, it must be worse
    } else if (isSignificantlyOlder) {
    	System.out.println("isBetter: isSignificantlyOlder");
        return false;
    }

    // Check whether the new location fix is more or less accurate
    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
    System.out.println("AccuracyDelta: "+accuracyDelta);
    boolean isLessAccurate = accuracyDelta > 0;
    boolean isMoreAccurate = accuracyDelta < 0;
    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

    // Check if the old and new location are from the same provider
    boolean isFromSameProvider = isSameProvider(location.getProvider(),
            currentBestLocation.getProvider());

    // Determine location quality using a combination of timeliness and accuracy
    if (isMoreAccurate) {
    	System.out.println("isBetter E?");
        return true;
    } else if (isNewer && !isLessAccurate) {
    	System.out.println("isBetter F?");
        return true;
    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
    	System.out.println("isBetter G?");
       return true;
    }
    return false;
}

/** Checks whether two providers are the same */
private boolean isSameProvider(String provider1, String provider2) {
    if (provider1 == null) {
      return provider2 == null;
    }
    return provider1.equals(provider2);
}

/*
public static Thread performOnBackgroundThread(final Runnable runnable) {
    final Thread t = new Thread() {
        @Override
        public void run() {
            try {
                runnable.run();
            } finally {

            }
        }
    };
    t.start();
    return t;
}

*/



public class MyLocationListener implements LocationListener
{

    public void onLocationChanged(final Location loc)
    {
    	System.out.println("Location Changed..................");
        if(isBetterLocation(loc, previousBestLocation)) {
            loc.getLatitude();
            loc.getLongitude();   
    	    if(loc.getProvider().equalsIgnoreCase("gps")){
    	    	//isGPSDone=true;
    	    	//handler.removeCallbacks(showTime);
    	    	//locationManager1.removeUpdates(listener); 
    	    	System.out.println("GPS Location Update & Posted");
//    	    	if(System.currentTimeMillis() > lastUpdated+POLL_TIME) {
					lastUpdated = System.currentTimeMillis();
					provider = "2";
					postLocation(loc);
					networkUpdate = 0;
//				}
    	    } else {
    	    	isNetworkDone=true;
    	    	System.out.println("Network Location Update");
    	    	provider="1";
				if(System.currentTimeMillis() > lastUpdated+POLL_TIME) {
//    	    	if(networkUpdate!=0){	// if first time or gps too late
    	    		System.out.println("Network Location Posted.........");
          	  		postLocation(loc);
    	    	} else {
        	    	System.out.println("Network Location Ignored as recently updted by GPS............");
    	    	}
    	    //	locationManager2.removeUpdates(listener); 
    	    	networkUpdate++;
    	    }

      	  //	postLocation(loc);

          //  intent.putExtra("Latitude", loc.getLatitude());
          //  intent.putExtra("Longitude", loc.getLongitude());     
          //  intent.putExtra("Provider", loc.getProvider());                 
          //  sendBroadcast(intent);     
/*
            final Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());         
            String Text = "";
            try {
                List<Address> addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);                
                island = addresses.get(0).getAddressLine(1); 

            } catch (Exception e) {
                e.printStackTrace();
                island="";
                //Text = "My current location is: " +"Latitude = " + loc.getLatitude() + ", Longitude = " + loc.getLongitude(); 
                
            }
            //System.out.println("My Location:"+Text);
            */
            //Toast.makeText( getApplicationContext(), "Location polled to server", Toast.LENGTH_SHORT).show();
        }                               
    }

    public void onProviderDisabled(String provider)
    {
        Toast.makeText( getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();
    }


    public void onProviderEnabled(String provider)
    {
        Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
    }


    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

}


public void postLocation(Location loc){
	  System.out.println("PostLocation................");
// 	writeToFile("Lets Start Thread");
		location=loc;
	    latitude=fiveDForm.format(loc.getLatitude());
	    longitude=fiveDForm.format(loc.getLongitude());
	    //speed = oneDForm.format(loc.getSpeed());
		speed = Math.round(loc.getSpeed());
	    //heading = oneDForm.format(loc.getBearing());
		heading = Math.round(loc.getBearing());
	    if(loc.getProvider().equalsIgnoreCase("gps")){
	    	provider="2";
	    } else {
	    	provider="1";
	    }
	    SocketTask();
	    previousBestLocation=loc;
//	   ClientThread p = new ClientThread();
//	   p.start();
//		new Thread(new ClientThread()).start();
//		ClientThread.start()
//		postData asyncTask = new postData();
//		asyncTask.execute("location");
/*		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		    asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ("location"));
		else
		    asyncTask.execute("location");
*/		
	}


private void SocketTask() {
	SocketAsyncTask = new AsyncTask<Void, Void, String>()    {

	@Override
	protected String doInBackground(Void... arg0) {
		int retryCounter = 0;
		int threshold=2;
		while (retryCounter < threshold) {
			System.out.println("While Retry, Counter:"+retryCounter);
	        try {
				retryCounter++;
				//	while( !Thread.interrupted() ) {
				//  	AppUtils.writeToFile("Runing Thread");
//	            InetAddress serverAddr = InetAddress.getByName(AppConstants.DOMAIN);
//				InetAddress serverIP = InetAddress.getByName(ip);

//				socket = new Socket(ip, port);
				//  System.out.println("Socket Connected...");
				//  socket = new Socket();
				//  socket.connect(new InetSocketAddress(serverAddr, AppConstants.PORT), 5000);
				Socket socket = new Socket();
				socket.connect(new InetSocketAddress(ip, port), 5000);
				s.setTimeZone(TimeZone.getTimeZone("GMT"));
				String date_time = s.format(new Date());
				socket.setSoTimeout(5000);
				socket.setKeepAlive(false);
				String outData = "A," + subscriber_id + ",location," + date_time + "," + latitude + "," + longitude + "," + speed + "," + heading + "," + cid + "," + cinr + "," + rss + "," + isp + "," + provider + "," + battery +"," + isCharging + ";\n";
				System.out.println("Data:" + outData);
				outs = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
				//            ins = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				outs.println(outData);
				socket.close();
				outs.close();
				retryCounter = 10;    //success, abort
				//AppUtils.writeToFile("Saved");
				//postThread.interrupt();
				// String msg = ins .readLine();
				// if(msg==null) msg="null";
				//       AppUtils.writeToFile(msg);
				//   		Thread.interrupted();
				//	}

			} catch (SocketTimeoutException etimeout){
	        	etimeout.printStackTrace();
	        } catch (UnknownHostException e1) {
	        	System.out.println(e1.toString());
	        	//AppUtils.writeToFile(e1.toString());
	            e1.printStackTrace();
	        } catch (IOException e1) {
	        	System.out.println(e1.toString());
	        	//System.out.println("IOException,Unable to open Socket????");
	//    		System.out.println("Lets Sleep this Thread");
	        /*	try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					//System.out.println("Interrupted:"+e);
					e.printStackTrace();
				}
			*/
	        	//AppUtils.writeToFile(e1.toString());
	            e1.printStackTrace();
	        } finally {
	        	//postThread = null;
	        }
		}
        return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		SocketAsyncTask =null;

	}
	};
	SocketAsyncTask.execute(null, null, null);
}




private void showNotification() {
    Intent notificationIntent = new Intent(this, HomeActivity.class);
    //Intent clearIntent = new Intent(this, ActionReceiver.class);
    //clearIntent.putExtra("notification_id", AppConstants.NOTIFICATION_SERVICE_ID);
   notificationIntent.setAction(AppConstants.MAIN_ACTION);
    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, AppConstants.NOTIFICATION_SERVICE_ID, clearIntent, 0);

    Intent previousIntent = new Intent(this, ActionReceiver.class);
    previousIntent.putExtra("notification_id", AppConstants.NOTIFICATION_SERVICE_ID);
    previousIntent.setAction(AppConstants.STOP_SERVICE_ACTION);
   // PendingIntent ppreviousIntent = PendingIntent.getService(this, 0, previousIntent, 0);
    PendingIntent ppreviousIntent = PendingIntent.getBroadcast(this, AppConstants.NOTIFICATION_SERVICE_ID, previousIntent, PendingIntent.FLAG_UPDATE_CURRENT);


    Bitmap icon = BitmapFactory.decodeResource(getResources(),
            R.drawable.icon_large);

    Notification notification = new NotificationCompat.Builder(this)
            .setContentTitle("FollowMe Tracking Service")
            .setTicker("FollowMe Tracking Service")
            .setContentText("Self Tracking Enabled")
            .setSmallIcon(R.drawable.icon_small)
            .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build();
    startForeground(AppConstants.NOTIFICATION_SERVICE_ID, notification);

}

private class MyPhoneStateListener extends PhoneStateListener {
    /* Get the Signal strength from the provider, each tiome there is an update */
    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength){
       super.onSignalStrengthsChanged(signalStrength);
       cinr=String.valueOf(signalStrength.getGsmSignalStrength());
       rss=String.valueOf(signalStrength.getCdmaDbm());
    }

    public void onCellLocationChanged(CellLocation cellLocation){
        super.onCellLocationChanged(cellLocation);
            cid = String.valueOf(((GsmCellLocation) cellLocation).getCid());
     }

  };/* End of private Class */

	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context ctxt, Intent intent) {
			int batt = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
			battery = Math.round(batt / 10)*10;
			int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
			if(status == BatteryManager.BATTERY_STATUS_CHARGING || 	status == BatteryManager.BATTERY_STATUS_FULL){
				isCharging = 1;
			} else {
				isCharging = 0;
			}

		}
	};
}