package com.asif.followme.PublicBoats;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.asif.followme.R;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.util.AppUtils;
import com.asif.followme.util.SettingsPreferences;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class PublicMapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener,Toolbar.OnMenuItemClickListener {

    private GoogleMap myMap;
   // private AsyncTask<Void, Void, String> LocationAsyncTask;
    private GetPublicLocation publicLocationTask = null;

    Context context;
    private static final String TAG_STATUS = "status";
    private static final String TAG_DEVICES = "devices";
    private static final String TAG_NAME = "na";
    private static final String TAG_SPEED = "sp";
    private static final String TAG_LATITUDE = "la";
    private static final String TAG_LONGITUDE = "lo";
    private static final String TAG_HEADING = "he";
    private static final String TAG_DATE = "dt";
    private static final String TAG_CONTACT = "co";
    private static final String TAG_ISLAND = "fe";  //fence
    private static final String TAG_LOCATION = "loc";
    String status,devices,name_id,name_name,name_type,name_color,name_speed,
            name_fuel,name_lat,name_lon,name_heading,name_date,data_lat,data_lon,last_reported,name_battery,name_power,name_contact,name_island;
    private Polyline polyline;
    PolylineOptions polylineOptions;
    LatLng point;
    float speed=0;
    float h=0;
    long diff=0;
    ArrayList<LatLng> latlonPoints = new ArrayList<LatLng>();
    ArrayList<Float> speedPoints = new ArrayList<Float>();
    ArrayList<Marker> mMarkers = new ArrayList<Marker>();
    Marker marker,dragMarker;
//    SimpleDateFormat  readFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",java.util.Locale.getDefault());	//database
//    SimpleDateFormat  showFormat =  new SimpleDateFormat("dd-MM-yyyy @ HH:mm",java.util.Locale.getDefault());	//display format
    SimpleDateFormat  readFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	//database
    SimpleDateFormat  showFormat =  new SimpleDateFormat("dd-MM-yyyy @ HH:mm");	//display format
    SimpleDateFormat  time_h_s =  new SimpleDateFormat("HH:mm");	//display format
    JSONObject mObj = new JSONObject();
    String currentIsland ="";
    String currentDeviceName="";
    String alertMsg="";
    private GeoAlertTask setAlertTask = null;
    String selectedVesselTitle;
    LatLng selectedPoint;
    Double selectedSpeed;
    ProgressBar progressBar;
    ProgressBar timeProgress;
    private boolean mIsRunning;
    private int mInterval = 30000; // 30 seconds by default, can be changed later
    Handler handler;
    Runnable runnable;
    CountDownTimer timer;
    MenuItem etaItem;
    public static MenuItem etaPanel;
    public static TextView etaTimeLabel, etaIslandLabel;
    public static String etaIsland="";
    private TextView navSpeedView;
    public static LatLng etaDestPoint;
    public static LatLng etaNowPoint;
    public static Double etaNowSpeed;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_public_maps);
        setContentView(R.layout.content_public_maps);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        context = this;
        progressBar = (ProgressBar) findViewById(R.id.progressbars);
        progressBar.setVisibility(View.VISIBLE);
        timeProgress = (ProgressBar) findViewById(R.id.progressBar);

        Toolbar toolbarBottom = (Toolbar) findViewById(R.id.toolbar_bottom);
        toolbarBottom.inflateMenu(R.menu.public_map_bottom);
        etaItem = toolbarBottom.getMenu().findItem(R.id.action_eta);
        etaPanel = toolbarBottom.getMenu().findItem(R.id.eta_panel);
        etaTimeLabel = (TextView) findViewById(R.id.nav_eta_time);
        etaIslandLabel = (TextView) findViewById(R.id.nav_eta_island);
        etaItem.setVisible(false);
        if(SettingsPreferences.getPublicNavigationName(context).equalsIgnoreCase("nav_public_group")){
            toolbarBottom.setVisibility(View.GONE);
        }
        navSpeedView = (TextView) findViewById(R.id.nav_speed);
        navSpeedView.setText("0.0");

        toolbarBottom.setOnMenuItemClickListener(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        handler = new Handler();
        mIsRunning = true;
        //TODO enable start timer....
//        startTimer();
/*        runnable = new Runnable() {
            private long time = 0;

            @Override
            public void run()
            {
                if(!mIsRunning){
                    return;
                }
                //Log.d("TimerExample", "Time Up. Lets refresh Map... ");
                System.out.println("TimeUp.....");
                if(myMap!=null) {
                    publicLocationTask = new GetPublicLocation(false, false);
                    publicLocationTask.execute((Void) null);
                }
                handler.postDelayed(this, mInterval);
            }
        };
*/

    }
    @Override
    public void onStart() {
        super.onStart();
        // SettingsPreferences.setAccount(context, "public");
        //Boolean doRefresh;
        etaPanel.setVisible(false);
        etaTimeLabel.setText("-");
        etaIslandLabel.setText("-");
        String title;
        try{
            Bundle extras = getIntent().getExtras();
            title = extras.getString("title");
            this.setTitle(title);
        } catch (Exception e){
            title = ""; //default no refresh
        }
    }
    @Override
    public void onBackPressed() {
//            Intent myIntent		=	new Intent(PublicMapsActivity.this, PublicActivity.class);
//        myIntent.putExtra("reload", "0");
//            startActivity(myIntent);
            mIsRunning = false;
//            finish();
            super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsRunning = false;
        if(timer!=null)
        {
            timer.cancel();
        }


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;

        // Add a marker in Sydney and move the camera
        double lat = Double.parseDouble(SettingsPreferences.getCountryLat(context));
        double lon = Double.parseDouble(SettingsPreferences.getCountryLon(context));
        float zoom = SettingsPreferences.getCountryZoom(context);
        LatLng mapCenter = new LatLng(lat, lon);
     //   myMap.setMapType(SettingsPreferences.getMapType(this));
     //   int map_type = SettingsPreferences.getMap(context);
        int map_type = GoogleMap.MAP_TYPE_HYBRID;
        myMap.setMapType(map_type);
        myMap.getUiSettings().setZoomControlsEnabled(false);
        myMap.getUiSettings().setCompassEnabled(true);
        myMap.getUiSettings().setRotateGesturesEnabled(false);
        myMap.getUiSettings().setMapToolbarEnabled(false);
        myMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng position) {
            System.out.println("Map Clicked");
            clearPolyline();
            mIsRunning = true;
            handler.postDelayed(runnable, mInterval);
            }
        });
        myMap.setOnMarkerDragListener(this);
        //myMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCenter,zoom));
        publicLocationTask = new GetPublicLocation(true,true);
        publicLocationTask.execute((Void) null);
        handler.postDelayed(runnable, mInterval);

    }


    public void startTimer(){
        timeProgress.setVisibility(View.VISIBLE);
        timer = new CountDownTimer(30000, 1000) {//CountDownTimer(edittext1.getText()+edittext2.getText()) also parse it to long
            public void onTick(long millisUntilFinished) {
                int p =(int) (30-(millisUntilFinished / 1000));
                timeProgress.setProgress(p);
                System.out.println("onTick.....");
                //  mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
                //	 System.out.println("Counter: "+ millisUntilFinished / 1000);
            }

            public void onFinish() {
             //   Toast.makeText(getApplicationContext(),"Loading...",Toast.LENGTH_SHORT).show();
             //   System.out.println("Loading....");
//                GetPublicLocations(false,false);	// Params: show loading, move camera
                if(myMap!=null) {
                    publicLocationTask = new GetPublicLocation(false, false);
                    publicLocationTask.execute((Void) null);
                }
                timer.start();
            }
        }
                .start();

    }


    public void clearPolyline(){
        if(polyline!=null){
            polyline.remove();
            for (int i = 0; i < mMarkers.size(); i++) {
                if(mMarkers.get(i).getId().equalsIgnoreCase(dragMarker.getId())){
                    LatLng ll= latlonPoints.get(i);
                    dragMarker.setPosition(ll);
                    return;
                }
            }
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(SettingsPreferences.getCountry(context).equalsIgnoreCase("mv")) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.public_map_menu, menu);
            MenuItem etaAlert = menu.findItem(R.id.action_alert);
            if(SettingsPreferences.getPublicNavigationName(context).equalsIgnoreCase("nav_public_group")){
                etaAlert.setVisible(false);
            } else {
                etaAlert.setVisible(true);
            }
        }
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.public_map_menu,menu);
//        if(SettingsPreferences.getSelectedPublicAccount(context)==0){


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_alert) {
            if(currentIsland.equalsIgnoreCase("-")){
                alertMsg = "when "+currentDeviceName+" arrives next island";
            } else {
                alertMsg = "when "+currentDeviceName+" leaves "+currentIsland;
            }
            showNotificationDialog(alertMsg);
            return true;
        }
        if(id==R.id.action_refresh){

            publicLocationTask = new GetPublicLocation(true,true);
            publicLocationTask.execute((Void) null);

        }

        return super.onOptionsItemSelected(item);
    }

    private void showNotificationDialog(String msg) {
            new AlertDialog.Builder(PublicMapsActivity.this)
                    .setTitle("Move Notification")
                    .setMessage("Alert me "+msg)
                    .setCancelable(true)
                    .setNegativeButton("NO",null)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //setPublicNotifiction(PublicActivity.selected_vessel_id,currentIsland);
                            System.out.println("ID:"+SettingsPreferences.getSelectedItemID(context)+",IS:"+currentIsland);
                            setAlertTask = new GeoAlertTask(SettingsPreferences.getSelectedItemID(context), currentIsland);
                            setAlertTask.execute((Void) null);


                        }
                    })
                    .setIcon(R.drawable.ic_notifications_black_24dp)
                    .show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_eta:
                Intent mIntent = new Intent(context, PublicETAActivity.class);
                mIntent.putExtra("v_name", PublicBoatsFragment.selected_item_name);
                mIntent.putExtra("id", PublicBoatsFragment.selected_item_id);
                startActivity(mIntent);
                break;
        }
        return false;
    }

    public class GetPublicLocation extends AsyncTask<Void, Void, String> {

        private final Boolean showProgress;;
        private final Boolean moveCamera;

        GetPublicLocation(Boolean sp, Boolean mc) {
            showProgress = sp;
            moveCamera = mc;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        //    Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            try{
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.getPublicLocation(context, SettingsPreferences.getSelectedItemID(context));
            }  catch(Exception ex) {
                //finish();
            }
            return null;

        }

        @Override
        protected void onPostExecute(final String result) {
            publicLocationTask = null;
            progressBar.setVisibility(View.GONE);

            //    showProgress(false);

            System.out.println("My Location REsult: "+result);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
//					System.out.println("SID:"+jObj.getString("sid"));
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
                            JSONObject json_devices = jObj.getJSONObject(TAG_DEVICES);
                         //   myMap.clear();
                            if(myMap!=null) {
                                addMarkersToMap(json_devices, moveCamera);
                            }
                        //    if(jObj.getJSONArray("etaData").length() > 0) {
                        //        updateETA(jObj);
                        //    }

                        } else {
                            String errmsg = jObj.getString("error");
                            //AppUtils.showAlertDialog(context, "Error", errmsg);
                            Toast.makeText(getApplicationContext(), errmsg, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Unable to reach server. Please check your internet connection", Toast.LENGTH_LONG).show();
                //AppUtils.showAlertDialog(context, "Timeout", "Unable to reach Server. Please try again!");
            }








         /*   if (result=="") {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }

            */


        }

        @Override
        protected void onCancelled() {
            publicLocationTask = null;
            //showProgress(false);
        }
    }


    public void showETAOnToolbar(LatLng p, double speed){
        if(etaDestPoint!=null) {
            Double d = getDistance(etaDestPoint, p);
            Double t = d / (speed * 0.514444);

            //	System.out.println("Distance:"+d);
            //	System.out.println("Speed:"+selectedSpeed);
            //	System.out.println("Time:"+t);
            //	System.out.println("Duration:"+getDuration(t));
            Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
            calendar.add(Calendar.SECOND, t.intValue());
            etaTimeLabel.setText(time_h_s.format(calendar.getTime()));
            // System.out.println(time_h_s.format(calendar.getTime()));
        }
    }

    private void addMarkersToMap(JSONObject jsonData, Boolean doMoveCamera){
        System.out.println("JSONObject:"+jsonData);
        Iterator<String> iter = jsonData.keys();
        int count = jsonData.length();
        latlonPoints.clear();
        speedPoints.clear();
        mMarkers.clear();
        myMap.clear();
     /*   String mName;
        String mIsland;
        String mLat;
        String mLon;
        String mSpeed;
        String mIcon;
       // String mHeading;
        float nSpeed;
        */
        LatLngBounds.Builder builder = new LatLngBounds.Builder();


        while(iter.hasNext()) {
            String key = iter.next();
            try {
                //Object obj4 = json_devices.get(key);
                final JSONObject jObj = jsonData.getJSONObject(key);
                final String mName = jObj.getString(TAG_NAME);
                final String mSpeed= jObj.getString(TAG_SPEED);
                final float nSpeed = Float.parseFloat(mSpeed);
                final String mLat = jObj.getString(TAG_LATITUDE);
                final String mLon = jObj.getString(TAG_LONGITUDE);
                final String mIsland = jObj.getString(TAG_ISLAND);
                final String mHeading = jObj.getString(TAG_HEADING);
                final String mIcon = jObj.getString("ic");
                final String mContact = jObj.getString(TAG_CONTACT);
                final String mDate = jObj.getString(TAG_DATE);
                point = new LatLng(Double.parseDouble(mLat),Double.parseDouble(mLon));
                etaNowPoint = point;
                etaNowSpeed = Double.parseDouble(mSpeed);
                if(count == 1){
                    currentIsland = mIsland;
                    currentDeviceName = mName;
                    selectedPoint = point;
                    selectedSpeed = Double.parseDouble(mSpeed);
                    navSpeedView.setText(mSpeed);
                    showETAOnToolbar(etaNowPoint,etaNowSpeed);
                }

                if(Float.parseFloat(mSpeed) > 1 && mIsland.equalsIgnoreCase("-")){
                    etaItem.setVisible(true);
                } else {
                    etaItem.setVisible(false);
                }
                BitmapDescriptor bitmapDescriptor = null;
                switch(mIcon){
                    case "1":
                        bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                        break;
                    case "2":
                        bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.arrow_green);
                        break;
                    case "3":
                        bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                        break;
                    case "4":
                        bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.arrow_red);

                }
                if(nSpeed > 1){
                    marker = myMap.addMarker(new MarkerOptions().draggable(true).position(point).title(mName).icon(bitmapDescriptor).rotation(Float.parseFloat(mHeading)));
                } else {	//stop
                    marker = myMap.addMarker(new MarkerOptions().position(point).title(mName).icon(bitmapDescriptor));
                }
                latlonPoints.add(point);
                speedPoints.add(nSpeed);
                mMarkers.add(marker);

                builder.include(point);

                JSONObject infoObj = new JSONObject();
                String speed_info="Stopped";
                String locString;
                System.out.println("NSpeed:"+nSpeed+",MSpeed:"+mSpeed+",mHeading:"+mHeading);
                if(nSpeed > 1){
//                    speed_info=String.format("%.1f", mSpeed)+" knots @ "+String.format("%.0f", mHeading) +(char) 0x00B0;}
                      speed_info = mSpeed+" knots @ "+mHeading +(char) 0x00B0;
                }
                if(SettingsPreferences.getDegreeFormat(context)==0){
                    locString = AppUtils.getFormattedLocationInDegree(Double.parseDouble(mLat),Double.parseDouble(mLon));
                }  else {
                    locString = mLat+", "+mLon;
                }
                try{
                    infoObj.put(TAG_NAME, mName);
                    infoObj.put(TAG_ISLAND, mIsland);
                    infoObj.put(TAG_SPEED, speed_info);
                    infoObj.put(TAG_LOCATION,locString);
                    infoObj.put(TAG_DATE, last_reported);
                    infoObj.put(TAG_CONTACT,mContact);
                    mObj.put(marker.getId(), infoObj);
                } catch (JSONException e){
                    e.printStackTrace();

                }



                myMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker arg0) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker arg0) {

                        View v = getLayoutInflater().inflate(R.layout.info_window_position, null);
                        TextView vehicleName 	 = (TextView) v.findViewById(R.id.vname);
                        TextView vehicleLocation 	 = (TextView) v.findViewById(R.id.line0);
                        TextView vehicleSpeed 	 = (TextView) v.findViewById(R.id.line1);
                        TextView vehiclePosition = 	   (TextView) v.findViewById(R.id.line2);
//						TextView vehicleBattery =      (TextView) v.findViewById(R.id.line3);
                        TextView vehicleDateTime = (TextView) v.findViewById(R.id.line4);
                        TextView vehicleContact =  (TextView) v.findViewById(R.id.line5);

                        try {
                            JSONObject iObj = mObj.getJSONObject(arg0.getId());
                            System.out.println("iObj:"+iObj.toString());

                            vehicleName.setText(arg0.getTitle());
                            vehicleLocation.setText(iObj.getString("fe"));
                            vehicleSpeed.setText(iObj.getString("sp"));
                            vehiclePosition.setText(iObj.getString("loc"));
                            //	vehicleLon.setText      (lon);
                            //    vehicleDateTime.setText	(date);
                            //	vehicleBattery.setText  (battery);
                            vehicleContact.setText(iObj.getString("co"));
                        } catch (JSONException e){
                            e.printStackTrace();

                        }
                        return v;
                    }
                });



            } catch (JSONException e) {

            }
        }

        if(count > 1){
            LatLngBounds bounds = builder.build();
            myMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 300));
        } else {
            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point,12));
        }


    }


    private void addMarkersToMap_old(JSONObject json_data, Boolean doMoveCamera){
        Iterator<String> iter = json_data.keys();
        int count = json_data.length();
        latlonPoints.clear();
        speedPoints.clear();
        mMarkers.clear();

        selectedVesselTitle="";
        String icon ="";
/*		if(mMarkers!=null)
		{
		    for (int i = 0; i < mMarkers.size(); i++) {
		    	mMarkers.get(i).remove();
		    }
		}
*/

        myMap.clear();
        while(iter.hasNext())
        {
            String key = iter.next();
            try {
                //Object obj4 = json_devices.get(key);
                JSONObject obj4 = json_data.getJSONObject(key);
                name_name = obj4.getString(TAG_NAME);
                name_speed = obj4.getString(TAG_SPEED);
                name_lat = obj4.getString(TAG_LATITUDE);
                name_lon = obj4.getString(TAG_LONGITUDE);
                name_heading = obj4.getString(TAG_HEADING);
                name_date = obj4.getString(TAG_DATE);
//				name_battery = obj4.getString(TAG_VOLT);
//				name_power = obj4.getString(TAG_POWER);
                name_contact=obj4.getString(TAG_CONTACT);
                name_island = obj4.getString(TAG_ISLAND);
                icon = obj4.getString("ic");

                point = new LatLng(Double.parseDouble(name_lat),Double.parseDouble(name_lon));
                etaNowPoint = point;
                etaNowSpeed = Double.parseDouble(name_speed);
                if(count == 1){
                    currentIsland = name_island;
                    currentDeviceName = name_name;
                    selectedPoint = point;
                    selectedSpeed = Double.parseDouble(name_speed);
                    navSpeedView.setText(name_speed);
                    showETAOnToolbar(etaNowPoint,etaNowSpeed);
                }

                //System.out.println(point);
                //	LatLng p;
                speed=0;
                h=0;
                speed=Float.parseFloat(name_speed);
                h = Float.parseFloat(name_heading);
                if(speed > 1 && name_island.equalsIgnoreCase("-")){
                    etaItem.setVisible(true);
                } else {
                    etaItem.setVisible(false);
                }
                //   Time m = new Time();
                //   m.setToNow();
                //   long m1 = m.toMillis(true);
                //	diff=0;
                try {
//					    Date date = dbformat.parse(name_date);
//					    last_reported = dformat.format(date);
                    //SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //readFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date date = readFormat.parse(name_date);
                    //Date date = s.parse(name_date);
                    //last_reported = s.format(date);
                    last_reported = showFormat.format(date);
                    //  long m2 = date.getTime();
                    //   diff = m1-m2;
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            } catch (JSONException e) {
                // Something went wrong!
            }

            /*    if(polylineOptions!=null){
                    if(polyline!=null){
                        polyline.remove();
                    }
                    polylineOptions.add(point);
                    polyline = myMap.addPolyline(polylineOptions);
                }
               */

                BitmapDescriptor bitmapDescriptor = null;
                switch(icon){
                    case "1":
                        bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                        break;
                    case "2":
                        bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.arrow_green);
                        break;
                    case "3":
                        bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                        break;
                    case "4":
                        bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.arrow_red);

                }

                if(speed>1){
                    marker = myMap.addMarker(new MarkerOptions().draggable(true).position(point).title(name_name).snippet(name_name).icon(bitmapDescriptor).rotation(h));
                } else {	//stop
                    marker = myMap.addMarker(new MarkerOptions().position(point).title(name_name).snippet(name_name).icon(bitmapDescriptor));
                }
                latlonPoints.add(point);
                speedPoints.add(speed);
                mMarkers.add(marker);
                marker.showInfoWindow();

            /*

                JSONObject infoObj = new JSONObject();
                try{
                    infoObj.put(TAG_NAME, name_name);
                    infoObj.put(TAG_ISLAND, name_island);
                    infoObj.put(TAG_SPEED, name_speed);
                    infoObj.put(TAG_LONGITUDE, name_lon);
                    infoObj.put(TAG_LATITUDE, name_lat);
                    infoObj.put(TAG_HEADING, name_heading);
                    infoObj.put(TAG_DATE, last_reported);
//				infoObj.put(TAG_VOLT, name_battery);
//				infoObj.put(TAG_POWER, name_power);
                    infoObj.put(TAG_CONTACT, name_contact);
                    mObj.put(marker.getId(), infoObj);
                } catch (JSONException e){

                }

                myMap.setInfoWindowAdapter(new InfoWindowAdapter()
                {
                    @Override
                    public View getInfoWindow(Marker arg0)
                    {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker arg0)
                    {
                        String s=arg0.getId();
                        float speed =0;
                        String lat="";
                        String lon="";
                        String date = "";
                        double heading =0;
//						String[] battData = null;
//						String battery="";
//						String batt="";
//						String pow="";
//						String power ="/ DC Connected";
                        String contact="";
                        String pos="";
                        String locString ="";
                        String island = "";

                        View v = getLayoutInflater().inflate(R.layout.info_window_position, null);
                        TextView vehicleName 	 = (TextView) v.findViewById(R.id.vname);
                        TextView vehicleLocation 	 = (TextView) v.findViewById(R.id.line0);
                        TextView vehicleSpeed 	 = (TextView) v.findViewById(R.id.line1);
                        TextView vehiclePosition = 	   (TextView) v.findViewById(R.id.line2);
//						TextView vehicleBattery =      (TextView) v.findViewById(R.id.line3);
                        TextView vehicleDateTime = (TextView) v.findViewById(R.id.line4);
                        TextView vehicleContact =  (TextView) v.findViewById(R.id.line5);
                        //	TextView vehicleContact =  (TextView) v.findViewById(R.id.line6);

                        try{
                            JSONObject dd = mObj.getJSONObject(s);
                            //						speed = String.format("%.1f",dd.getString(TAG_SPEED));
                            speed=Float.parseFloat(dd.getString(TAG_SPEED));
                            island = dd.getString(TAG_ISLAND);
                            //speed = dd.getDouble(TAG_SPEED);
                            lat = dd.getString(TAG_LATITUDE);
                            lon = dd.getString(TAG_LONGITUDE);
                            date = dd.getString(TAG_DATE);
                            //	int sp = Integer.parseInt(dd.getString(TAG_HEADING));
                            heading = dd.getDouble(TAG_HEADING);
                            //	batt = dd.getString(TAG_VOLT);
                            //	pow = dd.getString(TAG_POWER);
                            contact=dd.getString(TAG_CONTACT);
                            if(SettingsPreferences.getDegreeFormat(context)==0){
                                locString = AppUtils.getFormattedLocationInDegree(Double.parseDouble(lat),Double.parseDouble(lon));
                            }  else {
                                locString = lat+", "+lon;
                            }

                        } catch (Exception e){

                        }
                        String speed_info="Stopped";
                        if(speed>1){speed_info=String.format("%.1f", speed)+" knots @ "+String.format("%.0f", heading) +(char) 0x00B0;}

                        vehicleName.setText		(arg0.getTitle());
                        vehicleLocation.setText(island);
                        vehicleSpeed.setText	(speed_info);
                        vehiclePosition.setText      (locString);
                        //	vehicleLon.setText      (lon);
                        vehicleDateTime.setText	(date);
                        //	vehicleBattery.setText  (battery);
                        vehicleContact.setText  (contact);
                        return v;
                    }
                });

                LatLngBounds.Builder bld = new LatLngBounds.Builder();
                for (int i = 0; i < mMarkers.size(); i++) {
                    LatLng ll = new LatLng(mMarkers.get(i).getPosition().latitude, mMarkers.get(i).getPosition().longitude);
                    bld.include(ll);
                }
                LatLngBounds bounds = bld.build();

                //			if(myMap.getCameraPosition().zoom==8){
                //		    System.out.println(moveCamera);
                if(doMoveCamera && myMap!=null){
                    if(count > 1){
                        myMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 300));
                    } else {
                        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point,12));
                    }
                }
                */


        }//each

    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        dragMarker = marker;
        mIsRunning = false; //lets pause during eat check, so that marker will interrupted
        if(selectedVesselTitle.equalsIgnoreCase("")){
            selectedVesselTitle=marker.getTitle();
        }
        //System.out.println("Marker " + marker.getId() + " DragStart");
        System.out.println("Markers Size:"+mMarkers.size());
        System.out.println("LatLonPoint Size:"+latlonPoints.size());
        for (int i = 0; i < mMarkers.size(); i++) {
            // System.out.println(mMarkers.get(i).getId());
            if(mMarkers.get(i).getId().equalsIgnoreCase(marker.getId())){
                //	 LatLng ll = mMarkers.get(i).getPosition();
                selectedPoint= latlonPoints.get(i);
                selectedSpeed= (double)speedPoints.get(i);
                //	 LatLng ll = new LatLng(mMarkers.get(i).getPosition().latitude, mMarkers.get(i).getPosition().longitude);
                //	 System.out.println("Found Marker");
                //	 System.out.println(selectedPoint);
                return;
            }
        }
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        if(polyline!=null){
            polyline.remove();
        }
        polylineOptions = new PolylineOptions();
        polylineOptions.add(selectedPoint);
        polylineOptions.add(marker.getPosition());
        //polylineOptions.color(Color.GREEN);
        if(name_color==null){
            name_color="FF0000";
        }

        polylineOptions.color(Color.parseColor("#"+name_color));
        polylineOptions.width(5);
        polyline = myMap.addPolyline(polylineOptions);

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        if(polyline!=null){
            polyline.remove();
        }
        polylineOptions = new PolylineOptions();
        polylineOptions.add(selectedPoint);
        polylineOptions.add(marker.getPosition());
        //polylineOptions.color(Color.GREEN);
        if(name_color==null){
            name_color="FF0000";
        }

        polylineOptions.color(Color.parseColor("#"+name_color));
        polylineOptions.width(5);
        polyline = myMap.addPolyline(polylineOptions);

        Location currLocation = new Location("this");

        //selectedSpeed = 20.0;
        Double d = getDistance(selectedPoint,marker.getPosition());
        Double t = d / (selectedSpeed*0.514444);

        //	System.out.println("Distance:"+d);
        //	System.out.println("Speed:"+selectedSpeed);
        //	System.out.println("Time:"+t);
        //	System.out.println("Duration:"+getDuration(t));
        Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
        calendar.add(Calendar.SECOND, t.intValue());
        //	System.out.println(sdf.format(calendar.getTime()));
        marker.setTitle(selectedVesselTitle+" / ETA: "+sdf.format(calendar.getTime()));
        Toast.makeText(getApplicationContext(), "Estimated Arrival Time: "+sdf.format(calendar.getTime()), Toast.LENGTH_LONG).show();


//        marker.setInfoWindowAnchor(-1,0);
        //	arg0.setTitle("Test");
//        marker.showInfoWindow();
//		myMap.addMarker(new MarkerOptions().position(selectedPoint).title("Estimated Arrival Time:"+sdf.format(calendar.getTime())));


    }


    public class GeoAlertTask extends AsyncTask<Void, Void, String> {

        private final String device_id;
        private final String fence_id;

        GeoAlertTask(String id, String fid) {
            device_id = id;
            fence_id = fid;
        }

        @Override
        protected String doInBackground(Void... arg0) {
            try{
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.setGeoAlert(context,device_id,fence_id);
                //GetText();
            }
            catch(Exception ex)
            {
                //finish();
            }
            return null;

        }

        @Override
        protected void onPostExecute(final String result) {
            setAlertTask = null;
            //showProgress(false);

            System.out.println("Login REsult: "+result);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
//					System.out.println("SID:"+jObj.getString("sid"));
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
                            AppUtils.showAlertDialog(context,"Alert Saved","You will be notified "+alertMsg);
                        } else {
                            String errmsg = jObj.getString("error");
                            //AppUtils.showAlertDialog(context, "Error", errmsg);
							Toast.makeText(getApplicationContext(), errmsg, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Unable to reach server. Please check your internet connection", Toast.LENGTH_LONG).show();
                //AppUtils.showAlertDialog(context, "Timeout", "Unable to reach Server. Please try again!");
            }








         /*   if (result=="") {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }

            */


        }

        @Override
        protected void onCancelled() {
            setAlertTask = null;
            //showProgress(false);
        }
    }
    public double getDistance(LatLng LatLng1, LatLng LatLng2) {
        double distance = 0;
        Location locationA = new Location("A");
        locationA.setLatitude(LatLng1.latitude);
        locationA.setLongitude(LatLng1.longitude);
        Location locationB = new Location("B");
        locationB.setLatitude(LatLng2.latitude);
        locationB.setLongitude(LatLng2.longitude);
        distance = locationA.distanceTo(locationB);
        return distance;

    }



}
