package com.asif.followme;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.asif.followme.MyAccount.MyBoatsFragment;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.util.AppUtils;
import com.asif.followme.util.SettingsPreferences;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MyMapsActivity extends AppCompatActivity
        implements OnMapReadyCallback ,GoogleMap.OnMarkerDragListener, View.OnClickListener, Toolbar.OnMenuItemClickListener {

    private GoogleMap myMap;
    private AsyncTask<Void, Void, String> LocationAsyncTask;
    Context context;
    private static final String TAG_STATUS = "status";
    private static final String TAG_DEVICES = "devices";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "na";
    private static final String TAG_SPEED = "sp";
    private static final String TAG_LATITUDE = "la";
    private static final String TAG_LONGITUDE = "lo";
    private static final String TAG_HEADING = "he";
    private static final String TAG_DATE = "dt";
    private static final String TAG_CONTACT = "co";
    private static final String TAG_ISLAND = "fe";  //fence
    private static final String TAG_PRIVATE = "pv";
    private Polyline tailLine,traceLine,dragLine;
    PolylineOptions tailLineOptions,traceLineOptions,dragLineOptions;
    double speed=0;
    float angle = 0;
   // float h=0;
    long diff=0;
    ArrayList<LatLng> latlonPoints = new ArrayList<LatLng>();
    ArrayList<Double> speedPoints = new ArrayList<Double>();
    ArrayList<Marker> mMarkers = new ArrayList<Marker>();
    JSONObject markersInfo = new JSONObject();
    JSONObject tailObject = new JSONObject();
    Marker marker,dragMarker;
//    SimpleDateFormat dbformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",java.util.Locale.getDefault());	//database
//    SimpleDateFormat  dformat =  new SimpleDateFormat("dd-MM-yyyy @ HH:mm",java.util.Locale.getDefault());	//display format
    SimpleDateFormat dbformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	//database
    SimpleDateFormat  dformat =  new SimpleDateFormat("dd-MM-yyyy @ HH:mm");	//display format
    SimpleDateFormat  time_h_s =  new SimpleDateFormat("HH:mm");	//display format
    JSONObject mObj = new JSONObject();
    String currentIsland ="";
    String currentDeviceName="";
    String alertMsg="";
    ToggleButton toggleFollow,toggleShowRoute;
    private TextView navSpeedView;
    ProgressBar timeProgress;
    public int mYear;
    public int mMonth;
    public int mDay;
    public int nMonth;
    public static String tripLogDate;
    public String dateAction;
    JSONObject json_devices = null;
    JSONObject json_name = null;
    JSONArray json_data = null;
    JSONObject json_data_object = null;
    private static final String TAG_NAME_LIST = "name";
    private static final String TAG_DATA_LIST = "data";
    private static final String TAG_TYPE = "ty";
    private static final String TAG_COLOR = "co";
    private static final String TAG_FUEL = "fu";
    float distance;
    LinkedList<LatLng> points;
    private LatLngBounds.Builder bounds;
    String status,devices,name_id,name_name,name_type,name_color,name_speed,name_land,
            name_fuel,name_lat,name_lon,name_heading,name_date,data_lat,data_lon,last_reported,name_battery,name_power,name_contact;
    ProgressDialog dialog;
    Marker markerOptions;
    LatLng selectedPoint;
//    public static LatLng etaPoint;
    Double selectedSpeed;
    String selectedVesselTitle;
    FloatingActionButton fab;
    private int mInterval = 30000; // 5 seconds by default, can be changed later
    private Handler mHandler;
    private boolean mIsRunning = false;
    ProgressBar progressBar;
    private View screenView;
    Handler handler;
    Runnable runnable;
    private String isNeighbours ="0";
    private Boolean isTail = false;
    private String selected_title, selected_id;
    CountDownTimer timer;
    Boolean firstLoad;
//    private static MenuItem neighbourSwitchItem;
    ToggleButton neighbourToggle, tailToggle;
    Boolean isTraced = false;
    String did;
    HashMap<String,Marker> hashMapMarker = new HashMap<>();
    private int MyAccountType =0;
    public static LatLng etaDestPoint;
    public static LatLng etaNowPoint;
    public static Double etaNowSpeed;
    public static TextView etaTimeLabel, etaIslandLabel;
    public static String etaIsland="";
    public String selected_item_name="";


    //My account
    private GetMyLocation myLocationTask = null;
    LatLng point;
    MenuItem etaItem;
    public static MenuItem etaPanel;
    //JSONObject json_devices = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_maps);
//        setContentView(R.layout.content_my_maps);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        context = this;
        progressBar = (ProgressBar) findViewById(R.id.progressbars);
        progressBar.setVisibility(View.VISIBLE);
        timeProgress = (ProgressBar) findViewById(R.id.time_progressBar);
        firstLoad = true;
        fab = (FloatingActionButton) findViewById(R.id.fab_route_clear);
        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTraced = false;
                clearPolyline(traceLine);
               // if(polyline!=null){
               //     polyline.remove();
               // }
                fab.setVisibility(View.INVISIBLE);

                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
               //         .setAction("Action", null).show();
            }
        });

       Toolbar toolbarBottom = (Toolbar) findViewById(R.id.toolbar_bottom);
       toolbarBottom.inflateMenu(R.menu.my_map_bottom);
       etaItem = toolbarBottom.getMenu().findItem(R.id.action_eta);
       etaPanel = toolbarBottom.getMenu().findItem(R.id.eta_panel);
       etaTimeLabel = (TextView) findViewById(R.id.nav_eta_time);
       etaIslandLabel = (TextView) findViewById(R.id.nav_eta_island);
       etaItem.setVisible(false);
//        etaItem = toolbarBottom.findViewById(R.id.action_eta);
//       MyAccountType = SettingsPreferences.getMyNavigationName(context);
       if(SettingsPreferences.getMyNavigationName(context).equalsIgnoreCase("nav_my_groups")){
           toolbarBottom.setVisibility(View.GONE);
       }
//        neighbourSwitchItem = toolbarBottom.getRootView().findViewById(R.id.my_switch_item);
        neighbourToggle = (ToggleButton) findViewById(R.id.switch_neighbour);
        tailToggle = (ToggleButton) findViewById(R.id.switch_tail);
        navSpeedView = (TextView) findViewById(R.id.nav_speed);
        navSpeedView.setText("0.0");

               neighbourToggle.setOnClickListener(this);
               tailToggle.setOnClickListener(this);
               toolbarBottom.setOnMenuItemClickListener(this);
  //       switchNeighbour = (ToggleButton) neighbourSwitchItem.getActionView().findViewById(R.id.switch_live);
 //       switchNeighbour.setOnClickListener(this);


/*        toolbarBottom.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                System.out.println(item.getItemId());
                switch(item.getItemId()){
                    case R.id.switch_neighbour:
                        System.out.println("Clicked.......................................");
                        break;
                    case R.id.neighbour_switch_item:
                        System.out.println("-------------------------------------------------");
                        break;
                    case R.id.tool_neighbours:
                        System.out.println("Tool_Neigbours-------------------------------------------------");
                        break;
                }
                return true;
            }
        });
*/

        /*
        // No nav bar for map

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.my_maps_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.my_maps_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        */

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        nMonth= mMonth+1;


//        if(findViewById(R.id.ad_layout).getHeight() > 0){
//            screenView = findViewById(R.id.ad_layout);
 //       } else {
 //           screenView = findViewById(R.id.adView);
//        }
//        screenView = myMap;



        handler = new Handler();
        mIsRunning = true;
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
                    myLocationTask = new GetMyLocation(false, false, isNeighbours);
                    myLocationTask.execute((Void) null);
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
        mIsRunning = true;
        startTimer();
        String title;
        try{
            Bundle extras = getIntent().getExtras();
            title = extras.getString("title");
            //selected_id = extras.getString("selected_id");
            selected_item_name = title;
            selected_title = title;
            this.setTitle(title);
        } catch (Exception e){
            title = ""; //default no refresh
        }
//        if(timer!=null){
//           timer.start();
//        }
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
    @Override
    public void onResume() {
        super.onResume();
        mIsRunning = true;
//        startTimer();
        if(timer!=null){
            timer.start();
        }
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.my_maps_drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
            isTraced = false;
            finish();
/*            Intent myIntent		=	new Intent(MyMapsActivity.this, MyActivity.class);
            myIntent.putExtra("reload", "0");
            myIntent.putExtra("sender", "map");
            startActivity(myIntent);
            mIsRunning = false;
            isTraced = false;
            finish();
            super.onBackPressed();
            */
  //      }
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
        //int map_type = SettingsPreferences.getMap(context);
        int map_type = GoogleMap.MAP_TYPE_HYBRID;
        myMap.setMapType(map_type);
        myMap.getUiSettings().setZoomControlsEnabled(false);
        myMap.getUiSettings().setCompassEnabled(true);
        myMap.getUiSettings().setRotateGesturesEnabled(false);
        myMap.getUiSettings().setMapToolbarEnabled(false);
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCenter,zoom));
        myMap.setOnMarkerDragListener(this);

        myMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng position) {
                System.out.println("Map Clicked");
                clearPolyline(dragLine);
                mIsRunning = true;
                handler.postDelayed(runnable, mInterval);

            }
        });
     //   isNeighbours="1"; //for testing
        myLocationTask = new MyMapsActivity.GetMyLocation(true,true, isNeighbours);
        myLocationTask.execute((Void) null);
        handler.postDelayed(runnable, mInterval);

        //myMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
    }
    public void startTimer(){
        timeProgress.setVisibility(View.VISIBLE);
        timer = new CountDownTimer(30000, 1000) {//CountDownTimer(edittext1.getText()+edittext2.getText()) also parse it to long
            public void onTick(long millisUntilFinished) {
                int p =(int) (30-(millisUntilFinished / 1000));
                timeProgress.setProgress(p);
                //  mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
                //	 System.out.println("Counter: "+ millisUntilFinished / 1000);
            }

            public void onFinish() {
                //   Toast.makeText(getApplicationContext(),"Loading...",Toast.LENGTH_SHORT).show();
                //   System.out.println("Loading....");
//                GetPublicLocations(false,false);	// Params: show loading, move camera
                if(myMap!=null) {
                    myLocationTask = new GetMyLocation(false, true, isNeighbours);
                    myLocationTask.execute((Void) null);
                }
                timer.start();
            }
        }
                .start();

    }

    public void clearPolyline(Polyline polyline){
        System.out.println("clearPolyline xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        if(polyline!=null){
            polyline.remove();
            for (int i = 0; i < mMarkers.size(); i++) {
                if(dragMarker!=null) {
                    if (mMarkers.get(i).getId().equalsIgnoreCase(dragMarker.getId())) {
                        LatLng ll = latlonPoints.get(i);
                        dragMarker.setPosition(ll);
                        return;
                    }
                }
            }
        }
    }
    public void clearMarkers(){
            for (int i = 0; i < mMarkers.size(); i++) {
                mMarkers.get(i).remove();
            }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_maps, menu);
        MenuItem trace = menu.findItem(R.id.tool_trace);
 //       etaItem = menu.findItem(R.id.action_eta);
        //Toolbar toolbarBottom = (Toolbar) findViewById(R.id.toolbar_bottom);
        //toolbarBottom.inflateMenu(R.menu.my_map_bottom);
        System.out.println("onCreateOptionMenu: Nav Name:"+SettingsPreferences.getMyNavigationName(context));
        if(SettingsPreferences.getMyNavigationName(context).equalsIgnoreCase("nav_my_vessels")){
            trace.setVisible(true);
        } else {
            trace.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        System.out.println("ID:"+id);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id==R.id.tool_trace){
            dateAction="route";
            mIsRunning = false;
            traceLineOptions = new PolylineOptions();
            DatePickerDialog dialog = new DatePickerDialog(context, new MyMapsActivity.mDateSetListener(), mYear, mMonth, mDay);
            dialog.show();

        }
        if(id==R.id.tool_logs){
            dateAction="alarmlogs";
            mIsRunning = false;
            Intent intent		=	new Intent(context, LogsActivity.class);
            startActivity(intent);

         //   DatePickerDialog dialog = new DatePickerDialog(MyMapsActivity.this, new MyMapsActivity.mDateSetListener(), mYear, mMonth, mDay);
         //   dialog.show();

        }
        if(id==R.id.tool_guard_logs){
            dateAction="tab1";
            mIsRunning = false;
            Intent intent		=	new Intent(context, GuardLogsActivity.class);
            startActivity(intent);
        }
        if(id==R.id.tool_neighbours){
            isNeighbours = "1";
            if(myMap!=null) {
                myLocationTask = new GetMyLocation(false, true, isNeighbours);
                myLocationTask.execute((Void) null);
            }

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_neighbour:
                if(neighbourToggle.isChecked()){
                    isNeighbours = "1";
                    firstLoad = true;
                    Toast.makeText(context, "Show nearby devices", Toast.LENGTH_LONG).show();
                } else {
                    isNeighbours = "0";
                }
                if(myMap!=null) {
                    myLocationTask = new GetMyLocation(false, true, isNeighbours);
                    myLocationTask.execute((Void) null);
                }
                break;
            case R.id.switch_tail:
                if(tailToggle.isChecked()){
                    isTail =true;
                    Toast.makeText(context, "Plot route as Boat move", Toast.LENGTH_LONG).show();
                } else {
                    isTail = false;
                    if(tailLine!=null) {
                        tailLine.remove();
                    }
                }
                break;
/*            case R.id.action_trace:
                dateAction="route";
                mIsRunning = false;
                DatePickerDialog dialog = new DatePickerDialog(context, new MyMapsActivity.mDateSetListener(), mYear, mMonth, mDay);
                dialog.show();
                break;*/
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_trace:
                dateAction="route";
                mIsRunning = false;
                DatePickerDialog dialog = new DatePickerDialog(context, new MyMapsActivity.mDateSetListener(), mYear, mMonth, mDay);
                dialog.show();
                break;
            case R.id.action_eta:
                Intent mIntent = new Intent(context, ETAActivity.class);
                mIntent.putExtra("v_name", MyBoatsFragment.selected_item_name);
                mIntent.putExtra("id", MyBoatsFragment.selected_item_id);
                startActivity(mIntent);
                break;
        }
        return false;
    }



    public void addTail(LatLng from, LatLng to, String color_name){
        System.out.println("addTail xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        System.out.println("FROM/TO:"+from.toString()+" , "+ to.toString());
        tailLineOptions = new PolylineOptions();
        tailLineOptions.add(from);
        tailLineOptions.add(to);
        //polylineOptions.color(Color.GREEN);
        tailLineOptions.color(Color.parseColor("#"+color_name));
        tailLineOptions.width(5);
        tailLine = myMap.addPolyline(tailLineOptions);

    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        dragMarker = marker;
        mIsRunning = false; //lets pause during eat check, so that marker will interrupted

//		dragMarkerLatLng = marker.getPosition();
//		dragMarkerOptions = marker.g
        if(selectedVesselTitle.equalsIgnoreCase("")){
            selectedVesselTitle=marker.getTitle();
        }
        //System.out.println("Marker " + marker.getId() + " DragStart");
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
        // System.out.println("Marker " + marker.getId() + " Drag@" + marker.getPosition());
        if(dragLine!=null){
            dragLine.remove();
        }
        dragLineOptions = new PolylineOptions();
        dragLineOptions.add(selectedPoint);
        dragLineOptions.add(marker.getPosition());
        //polylineOptions.color(Color.GREEN);
        if(name_color==null){
            name_color="FF0000";
        }

        dragLineOptions.color(Color.parseColor("#"+name_color));
        dragLineOptions.width(5);
        dragLine = myMap.addPolyline(dragLineOptions);
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        if(dragLine!=null){
            dragLine.remove();
        }
        dragLineOptions = new PolylineOptions();
        dragLineOptions.add(selectedPoint);
        dragLineOptions.add(marker.getPosition());
        //polylineOptions.color(Color.GREEN);
        if(name_color==null){
            name_color="FF0000";
        }

        dragLineOptions.color(Color.parseColor("#"+name_color));
        dragLineOptions.width(5);
        dragLine = myMap.addPolyline(dragLineOptions);

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

        //	arg0.setTitle("Test");
//        marker.showInfoWindow();
//		myMap.addMarker(new MarkerOptions().position(selectedPoint).title("Estimated Arrival Time:"+sdf.format(calendar.getTime())));

    }
    public String getDuration(double sec){
        int seconds = (int) sec % 60 ;
        int minutes = (int) ((sec / 60) % 60);
        int hours   = (int) ((sec / (60*60)) % 24);


        return hours+":"+minutes+":"+seconds;
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

    public static Double getDistanceBetween(long etaNowLat, long etaNowLon, long etaDestLat, long etaDestLon) {
        // if (etaDestLat == null || etaDestLon == null)
        //     return null;
        float[] result = new float[1];
        Location.distanceBetween(etaNowLat, etaNowLon, etaDestLat, etaDestLon, result);
        return (double) result[0];
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

    private void addMarkersToMap(JSONObject json_data, Boolean doMoveCamera, final String isNeighbours){
        System.out.println("addMarkersToMap()");
        System.out.println(json_data);
        Iterator<String> iter = json_data.keys();
        int count = json_data.length();
        latlonPoints.clear();
        speedPoints.clear();
        String icon="";
        String last_reported ="";
        String name_name ="";
        String name_island = "";
        String name_speed ="";
        String name_lon ="";
        String name_lat="";
        String name_heading ="";
        String name_contact ="";
        String name_date ="";
        String name_id ="";
        LatLng centerPoint = null;
        Boolean centerBoat = false;
        int name_public =0;
        float angle = 0;
        selected_id = SettingsPreferences.getSelectedItemID(context);

//        LatLng point = null;

        selectedVesselTitle="";
        markersInfo = new JSONObject();
//        String icon ="";

 //       clearMarkers();
    if(!isTraced && !isTail) {
        //remove markers, polyline and overlays
        myMap.clear();
    } else {
        //keep trace lines, remove markers only
        clearMarkers();
 /*       int z = mMarkers.size();
        System.out.println("The Size is xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx:"+z);
		if(mMarkers!=null){
		    for (int i = 0; i < mMarkers.size(); i++) {
		        System.out.println("Lets Remove the marker XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXxx");
		        System.out.println(mMarkers.get(i));
		        Marker m = mMarkers.get(i);

		    	m.remove();
		    }
		}
		*/
    }

        mMarkers.clear();

        System.out.println("Count:"+count);
        while(iter.hasNext())
        {
            String key = iter.next();
            try {
                //Object obj4 = json_devices.get(key);
                JSONObject obj4 = json_data.getJSONObject(key);
                name_id = obj4.getString("id");
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
                name_public = obj4.getInt(TAG_PRIVATE);
                //System.out.println("Neighbout:"+isNeighbours+",Selected ID:"+selected_id+",Name ID:"+name_id+"=================================================================");
                //System.out.println("Lat:"+name_lat+",Lon:"+name_lon);

                double lat = Double.parseDouble(name_lat);
                double lon = Double.parseDouble(name_lon);
                point = new LatLng(lat,lon);
                etaNowPoint = point;
                etaNowSpeed = Double.parseDouble(name_speed);

                if(tailObject.has(name_id)){
                    if(isTail) {
                        JSONArray kk = tailObject.getJSONArray(name_id);
                        LatLng p1 = new LatLng(Double.parseDouble(kk.get(0).toString()), Double.parseDouble(kk.get(1).toString()));
                        addTail(p1, point, "FF0000");
                    }
                }
                JSONArray jj = new JSONArray();
                jj.put(0,name_lat);
                jj.put(1,name_lon);
                tailObject.put(name_id,jj);
                //tailObject.put("point",point);
                if(centerPoint == null){
                    centerPoint = point;
                }

                if(isNeighbours.equalsIgnoreCase("1") && name_id.equalsIgnoreCase(selected_id)){
                    System.out.println("Name id:"+name_id+", Selected ID:"+selected_id+", Center POINT");
                    centerPoint=point;
                    centerBoat = true;
                } else {
                    System.out.println("Name id:"+name_id+", Selected ID:"+selected_id);
                    centerBoat = false;
                }
                if(count > 1) {
                    if (centerBoat) {
                        navSpeedView.setText(name_speed);
                        showETAOnToolbar(etaNowPoint,etaNowSpeed);
                    }
                } else {
                    navSpeedView.setText(name_speed);
                    showETAOnToolbar(etaNowPoint,etaNowSpeed);
                }
                    //System.out.println(point);
                //	LatLng p;
                speed=0;
                //angle=0.0;
                speed=Float.parseFloat(name_speed);
                if(speed > 1 && name_island.equalsIgnoreCase("-")){
                    etaItem.setVisible(true);
                   // etaPanel.setVisible(true);
                } else {
                    etaItem.setVisible(false);
                   // etaPanel.setVisible(false);
                }
                angle = Float.parseFloat(name_heading);
                try {
                   // dbformat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date date = dbformat.parse(name_date);
                    last_reported = dformat.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                System.out.println("Something went wrong!");
            }

            //if(toggleShowRoute.isChecked() && polylineOptions!=null){
            if(isTraced){
                //if(polyline!=null){
                //    polyline.remove();
                //}
                if(traceLine!=null){
                    traceLine.remove();
                }
                traceLineOptions.add(point);
                traceLine = myMap.addPolyline(traceLineOptions);
            }


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
                    break;
                case "5":   //neighbours , stopped
                    bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN);
                    break;
                case "6":
                    bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.arrow_cyan);
                    break;

            }
                if (speed > 1) {
                    marker = myMap.addMarker(new MarkerOptions().draggable(true).position(point).title(name_name).icon(bitmapDescriptor).rotation(angle));
                    // some calculations to offset for infowindow position
                    //double angle = h;
                    double x = Math.sin(-angle * Math.PI / 180) * 0.5 + 0.5;
                    double y = -(Math.cos(-angle * Math.PI / 180) * 0.5 - 0.5);
                    marker.setInfoWindowAnchor((float)x, (float)y);
                } else {    //stop
                    marker = myMap.addMarker(new MarkerOptions().position(point).title(name_name).icon(bitmapDescriptor));
                }
            //marker.setInfoWindowAnchor(0.6f,-0.2f);
                JSONObject infoObj = new JSONObject();
                try{
                    infoObj.put(TAG_NAME, name_name);
                    infoObj.put(TAG_ISLAND, name_island);
                    infoObj.put(TAG_SPEED, name_speed);
                    infoObj.put(TAG_LONGITUDE, name_lon);
                    infoObj.put(TAG_LATITUDE, name_lat);
                    infoObj.put(TAG_HEADING, name_heading);
                    infoObj.put(TAG_DATE, last_reported);
                    infoObj.put(TAG_PRIVATE,name_public);
//				infoObj.put(TAG_VOLT, name_battery);
//				infoObj.put(TAG_POWER, name_power);
                    infoObj.put(TAG_CONTACT, name_contact);
                    mObj.put(marker.getId(), infoObj);
                   markersInfo.put(marker.getId(),infoObj);
                } catch (JSONException e){

                }
                marker.setTag(infoObj);
                myMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter()
                {
                    @Override
                    public View getInfoWindow(Marker arg0)
                    {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker arg0)
                    {
                        String mid=arg0.getId();
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
                        String id="";
                        int pvt = 0;

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
                            JSONObject dd = mObj.getJSONObject(mid);
                            //						speed = String.format("%.1f",dd.getString(TAG_SPEED));
                            speed=Float.parseFloat(dd.getString(TAG_SPEED));
                            island = dd.getString(TAG_ISLAND);
                            //speed = dd.getDouble(TAG_SPEED);
                            lat = dd.getString(TAG_LATITUDE);
                            lon = dd.getString(TAG_LONGITUDE);
                            date = dd.getString(TAG_DATE);
                            //	int sp = Integer.parseInt(dd.getString(TAG_HEADING));
                            heading = dd.getDouble(TAG_HEADING);
                            id=dd.getString("id");
                            //	batt = dd.getString(TAG_VOLT);
                            //	pow = dd.getString(TAG_POWER);
                            pvt = dd.getInt(TAG_PRIVATE);
                            contact=dd.getString(TAG_CONTACT);
                            System.out.println("Lat:"+lat+",Lon:"+lon);
                            if(SettingsPreferences.getDegreeFormat(context)==0){
                                locString = AppUtils.getFormattedLocationInDegree(Double.parseDouble(lat),Double.parseDouble(lon));
                                locString = lat+", "+lon;
                            }  else {
                                locString = lat+", "+lon;
                            }

                        } catch (Exception e){
                            locString = lat+", "+lon;
                           // System.out.println(e.toString());
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

                        if(isNeighbours.equalsIgnoreCase("0") || arg0.getTitle().equalsIgnoreCase(selected_title)){
                            return v;
                        } else {
                            Object j = arg0.getTag();
                            int pv =0;
                            System.out.println("TAG:"+j.toString());
                            try {
                                JSONObject jsonObject = new JSONObject(j.toString());
                                pv = jsonObject.getInt("pv");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(pv==0) {
                                arg0.setTitle("");  //hide title for neighbours
                                return null;
                            } else {
                                return v;
                            }
                           // return v;
                        }
                    }
                });


            latlonPoints.add(point);
            speedPoints.add(speed);
            mMarkers.add(marker);




            //			if(myMap.getCameraPosition().zoom==8){
            //		    System.out.println(moveCamera);




        }//each
        LatLngBounds.Builder bld = new LatLngBounds.Builder();
        for (int i = 0; i < mMarkers.size(); i++) {
            LatLng ll = new LatLng(mMarkers.get(i).getPosition().latitude, mMarkers.get(i).getPosition().longitude);
            bld.include(ll);
        }
        LatLngBounds bounds = bld.build();
       // firstLoad=false;
        if(isNeighbours.equalsIgnoreCase("0")) {
//            System.out.println("X xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            System.out.println("Do Move Camera:"+doMoveCamera+", Center Point:"+centerPoint);
            if(doMoveCamera && myMap!=null) {
                if (count > 1) {    //Group View
                    myMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 300));
//                    myMap.animateCamera(CameraUpdateFactory.newLatLng(centerPoint));
                } else {
//                    System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
//                    myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 12));
                    myMap.animateCamera(CameraUpdateFactory.newLatLng(centerPoint));
                }
            }
        } else {
//            System.out.println("Neighbout:"+isNeighbours+",Selected ID:"+selected_id+",Name ID:"+name_id+"----------------------------------------------------------");
           // if(name_id.equalsIgnoreCase(selected_id)) {
                //myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centerPoint, 12));
                if(firstLoad) {
                    System.out.println("First Load, Count:"+count);
                    if(count > 1) {
//                        System.out.println("A %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                        myMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,300));
                        myMap.animateCamera(CameraUpdateFactory.newLatLng(centerPoint));
                    } else {
//                        System.out.println("B %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centerPoint, 10));
//                        myMap.animateCamera(CameraUpdateFactory.newLatLng(centerPoint));
                    }
                } else {
//                    System.out.println("C %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                    myMap.animateCamera(CameraUpdateFactory.newLatLng(centerPoint));
                }
          //  }

        }
        firstLoad = false;

    }


    public class GetMyLocation extends AsyncTask<Void, Void, String> {

        private final Boolean showProgress;;
        private final Boolean moveCamera;
        private final String neighbour;

        GetMyLocation(Boolean sp, Boolean mc, String nb) {
            showProgress = sp;
            moveCamera = mc;
            neighbour = nb;
        }
        @Override
        protected void onPreExecute() {
             super.onPreExecute();
            //Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_LONG).show();

        }

        @Override
        protected String doInBackground(Void... arg0) {
            try{
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.getMyLocation(context, SettingsPreferences.getSelectedItemID(context), neighbour);
            }
            catch(Exception ex)
            {
                //finish();
            }
            return null;

        }

        @Override
        protected void onPostExecute(final String result) {
            myLocationTask = null;
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
 //                           myMap.clear();
                            if(myMap!=null) {
                                addMarkersToMap(json_devices, moveCamera, isNeighbours);
                                //addNeighboursToMap(json_devices, moveCamera);
                            }
                         /*   if(toggleFollow.isChecked()){
                                if(SettingsPreferences.getSelectedDevice(context).equalsIgnoreCase(SettingsPreferences.getMyDevice(context))){
                                } else {
                                    if(timer==null)	startTimer();
                                }
                            }
                         */
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
            myLocationTask = null;
            //showProgress(false);
        }
    }

    class mDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            int m=mMonth+1;
            // getCalender();
            tripLogDate = Integer.toString(mYear)+'-'+m+'-'+mDay;

            //      v.setText(new StringBuilder()
            // Month is 0 based so add 1
            //             .append(mMonth + 1).append("/").append(mDay).append("/")
            //            .append(mYear).append(" "));
            //   System.out.println(v.getText().toString());
            if(dateAction.equalsIgnoreCase("triplogs")){
                //Intent intent		=	new Intent(context, TripLogActivityxxx.class);
                //startActivity(intent);
            } else if(dateAction.equalsIgnoreCase("alarmlogs")){
                Intent intent		=	new Intent(context, LogsActivity.class);
                startActivity(intent);

            } else if(dateAction.equalsIgnoreCase("heat")){
//                HeatTask heatTask=new HeatTask();
//                heatTask.execute();
            } else {
                TraceTask traceTask=new TraceTask();
                traceTask.execute();
            }
        }
        public void onShow(){
            System.out.println("Showing");
        }
    }


    class TraceTask extends AsyncTask<Void, Void, String>    {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        //    dialog= ProgressDialog.show(getBaseContext(), null, "Loading Travelled Route...");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            String d = SettingsPreferences.getSelectedItemID(context);
            System.out.println("Device:"+d);
            System.out.println("TripDate:"+tripLogDate);
            try{
                //	System.out.println("Device id:"+GlobalData.getItemId(context));
                ContentParser parser = new ContentParser(context);
                return parser.getTrace(context,d,tripLogDate);
            } catch(Exception ex) {
                //finish();
				System.out.println(ex);
            }
            return null;
//			fetchDataFromJSON2();
//			return null;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);

            //System.out.println(result);
           // if(dialog.isShowing()){
            //    dialog.dismiss();
           // }
            if (!TextUtils.isEmpty(result)) {
                JSONObject jObj;
                System.out.println(result);
                try {
                    jObj = new JSONObject(result);
                    if (jObj.has(TAG_STATUS)) {
//                        status = jObj.getString(TAG_STATUS);
                        if(jObj.getString(TAG_STATUS).equalsIgnoreCase("ok")){
                            json_devices = jObj.getJSONObject(TAG_DEVICES);
                            points = new LinkedList<LatLng>();
                            bounds = new LatLngBounds.Builder();
                            for (int j = 0; j < json_devices.length(); j++)
                            {
                                json_name = json_devices.getJSONObject(TAG_NAME_LIST);
                                //		System.out.println("Fetch2:"+json_name+"aaaaaaaaaaaaaa2");
                                for (int k = 0; k < json_name.length(); k++)
                                {
                                    name_id = json_name.getString(TAG_ID);
                                    name_name = json_name.getString(TAG_NAME);
                                    name_type = json_name.getString(TAG_TYPE);
                                    name_color = json_name.getString(TAG_COLOR);
                                    name_speed = json_name.getString(TAG_SPEED);
                                    name_fuel = json_name.getString(TAG_FUEL);
                                    name_lat = json_name.getString(TAG_LATITUDE);
                                    name_lon = json_name.getString(TAG_LONGITUDE);
                                    name_heading = json_name.getString(TAG_HEADING);
                                    name_date = json_name.getString(TAG_DATE);
                                    name_land = json_name.getString("ld");
                                }

                                json_data = json_devices.getJSONArray(TAG_DATA_LIST);
                                //					System.out.println(json_data.length()+".................JSON LEN");
                                if(j==0)
                                {
                                    //LatLng p2=null;
                                    distance=0;
                                    double lat2 =0;
                                    double lon2 = 0;
                                    for (int l = 0; l < json_data.length(); l++)
                                    {
                                        json_data_object = json_data.getJSONObject(l);
                                        //for (int i = 0; i < json_data_object.length(); i++){
                                        data_lat = json_data_object.getString(TAG_LATITUDE);
                                        data_lon = json_data_object.getString(TAG_LONGITUDE);
                                        double lat1=Double.parseDouble(data_lat);
                                        double lon1=Double.parseDouble(data_lon);
                                        //				datalatitudeArrayList.add(data_lat);
                                        //				datalongitudeArrayList.add(data_lon);
                                        Location locationA = new Location("A");
                                        locationA.setLatitude(lat1);
                                        locationA.setLongitude(lon1);

//													LatLng p = new LatLng(Double.parseDouble(data_lat),Double.parseDouble(data_lon));
                                        LatLng p = new LatLng(lat1,lon1);
                                        points.add(p);
                                        //	position[i] = new LatLng(Double.parseDouble(lat[i]),Double.parseDouble(lon[i]));
                                        bounds.include(p);
                                        if(lat2!=0){
                                            Location locationB = new Location("B");
                                            locationB.setLatitude(lat2);
                                            locationB.setLongitude(lon2);
                                            distance+= locationA.distanceTo(locationB);
                                            //	System.out.println("Distance: "+distance);
                                        }
                                        lat2=lat1;
                                        lon2=lon1;
                                    }
                                }
                            }
                            drawTraceRoute();
                        } else {
//								AppUtils.showAlertDialog(context, "Error", jObj.getString("error"));
                            Toast.makeText(getApplicationContext(),jObj.getString("error"),Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"Unable to fetch data from Server",Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(),"Unable to fetch data from Server",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void drawTraceRoute(){
        try{
            isTraced = true;
            //if(dialog.isShowing() && dialog!=null) {
            LatLng p = new LatLng(Double.parseDouble(name_lat),Double.parseDouble(name_lon));
            System.out.println("LAT:"+name_lat);
            angle = Float.parseFloat(name_heading);
            //dialog.dismiss();
//            myMap.clear();
//            mMarkers.clear();
            //	System.out.println("Point Count"+points.size()+".................");
            //	if(statuss.equals("ok")){
            if(points.size()>1){
                myMap.clear();
                mMarkers.clear();
                traceLineOptions = new PolylineOptions();
                traceLineOptions.addAll(points);
                //polylineOptions.color(Color.GREEN);
                if(name_color==null){
                    name_color="000000";
                }

                traceLineOptions.color(Color.parseColor("#"+name_color));
                traceLineOptions.width(4);
                traceLine = myMap.addPolyline(traceLineOptions);


                long m1 = Calendar.getInstance().getTimeInMillis();
                long m2 =0;
                long diff=0;
                try {
                    Date date = dbformat.parse(name_date);
                    last_reported = dformat.format(date);
                    m2 = date.getTime();
                    diff = m1-m2;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                System.out.println("Now:"+m1+", Server:"+m2+" , Date:"+name_date);

                myMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter()
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
                        //	double speed =0;
                        double s0=Float.parseFloat(name_speed);
                        //double heading = Float.parseFloat(name_heading);
                        angle = Float.parseFloat(name_heading);
                        System.out.println("Angle.........................................................:"+angle);
                        //String pos=name_lat+", "+name_lon;

                        String locString="";
                        if(SettingsPreferences.getDegreeFormat(context)==0){
                            locString = AppUtils.getFormattedLocationInDegree(Double.parseDouble(name_lat),Double.parseDouble(name_lon));
                        }  else {
                            locString = name_lat+", "+name_lon;
                        }
                        double d0;
                        String distance_unit;
                        String speed_unit;
                        if(name_land.equalsIgnoreCase("0")){	//sea , nm
                            distance_unit="NM";
                            speed_unit="knots";
                            speed=s0;
                            d0 = (distance/1000) * 0.539957;
                        } else {	//land in km
                            distance_unit="km";
                            speed_unit="km/h";
                            speed=s0 * 1.852;
                            d0 = (distance/1000);
                        }
                        String d = String.format("%.2f", d0);
                        String f = String.format("%.0f", d0*Float.parseFloat(name_fuel));
                        //	int f = d0*(int)(name_fuel);
                        View v = getLayoutInflater().inflate(R.layout.info_window_trace, null);
                        TextView vehicleName 	 = (TextView) v.findViewById(R.id.vname);
                        TextView vehicleSpeed 	 = (TextView) v.findViewById(R.id.line1);
                        TextView vehicleDistance 	 = (TextView) v.findViewById(R.id.line2);
                        TextView vehicleFuel 	 = (TextView) v.findViewById(R.id.line3);
                        TextView vehiclePosition = (TextView) v.findViewById(R.id.line4);
                        //	TextView vehicleLon =      (TextView) v.findViewById(R.id.line5);
                        //	TextView vehicleDateTime = (TextView) v.findViewById(R.id.line5);

                        String speed_info="Stopped";
                        if(speed>1){speed_info=String.format("%.1f", speed)+" "+speed_unit+" @ "+String.format("%.0f", angle) +(char) 0x00B0;}
                        // if(speed>0){speed_info=speed+" knots @ "+heading +(char) 0x00B0;}
                        vehicleName.setText		(name_name);
                        vehicleSpeed.setText	(speed_info);
                        vehicleDistance.setText	(d+" "+distance_unit);
                        vehicleFuel.setText	    (f+" litres");
                        vehiclePosition.setText      (locString);
                        //	vehiclePosition.setText      (name_lat+", "+name_lon);
                        //	vehicleLon.setText      (name_lon);
                        //	vehicleDateTime.setText	("Last Reported  : "+last_reported);
                        return v;
                    }
                });
                BitmapDescriptor bitmapDescriptor;
                if(speed>1){
                    if(diff>300000){	//5 minutes
                        //				markerOptions=myMap.addMarker(new MarkerOptions().position(position[0]).title("").icon(bitmapDescriptor));
                        bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.arrow_red);
                    } else {
                        bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.arrow_green);
                    }
                    marker=myMap.addMarker(new MarkerOptions().position(p).title("").icon(bitmapDescriptor).rotation(angle));
                } else {	//stop
                    if(diff>180000){	//3 minutes
                        bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                    } else {
                        bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                    }
                    marker=myMap.addMarker(new MarkerOptions().position(p).title("").icon(bitmapDescriptor));
                }
                myMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(),50));
                fab.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getApplicationContext(),"No Trace Records Available",Toast.LENGTH_LONG).show();
            }
            //	}

        } catch (final IllegalArgumentException e) {
            // Handle or log or ignore
        } catch (final Exception e) {
            // Handle or log or ignore
        } finally {
            dialog = null;
        }
    }

}
