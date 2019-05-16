package com.asif.followme.Flight;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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
import com.asif.followme.PublicBoats.PublicETAActivity;
import com.asif.followme.R;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.util.SettingsPreferences;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FlightMapsActivity extends AppCompatActivity implements OnMapReadyCallback,Toolbar.OnMenuItemClickListener {

    private GoogleMap myMap;
   // private AsyncTask<Void, Void, String> LocationAsyncTask;
    private GetFlightLocation FlightLocationTask = null;

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

    String flight_no;


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



    }
    @Override
    public void onStart() {
        super.onStart();
        try{
            Bundle extras = getIntent().getExtras();
            flight_no = extras.getString("flight_no");
            this.setTitle(flight_no);
        } catch (Exception e){
           // title = ""; //default no refresh
        }
        //flight_no = "JJ3157";
    }
    @Override
    public void onBackPressed() {
//            Intent myIntent		=	new Intent(FlightMapsActivity.this, PublicActivity.class);
//        myIntent.putExtra("reload", "0");
//            startActivity(myIntent);
//            mIsRunning = false;
            finish();
            super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();


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
            //clearPolyline();
           // mIsRunning = true;
            //handler.postDelayed(runnable, mInterval);
            }
        });
        //myMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCenter,zoom));
        FlightLocationTask = new GetFlightLocation(flight_no);
        FlightLocationTask.execute((Void) null);
        //handler.postDelayed(runnable, mInterval);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(SettingsPreferences.getCountry(context).equalsIgnoreCase("mv")) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.public_map_menu, menu);

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


        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_eta:
                Intent mIntent = new Intent(context, PublicETAActivity.class);
               // mIntent.putExtra("v_name", PublicActivity.selected_item_name);
               // mIntent.putExtra("id", PublicActivity.selected_item_id);
                startActivity(mIntent);
                break;
        }
        return false;
    }

    public class GetFlightLocation extends AsyncTask<Void, Void, String> {

        private final String flight_no;

        GetFlightLocation(String flight_no) {
            this.flight_no = flight_no;
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
                return parser.getFlightLocation(context, flight_no);
            }  catch(Exception ex) {
                //finish();
            }
            return null;

        }

        @Override
        protected void onPostExecute(final String result) {
            FlightLocationTask = null;
            progressBar.setVisibility(View.GONE);

            //    showProgress(false);

            System.out.println("My Location REsult: "+result);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONArray jArray = new JSONArray(result);

                            if(myMap!=null) {
                                addMarkersToMap(jArray);
                            }

                } catch (JSONException e) {
                    try {
                        JSONObject jObj = new JSONObject(result);
                        if (jObj.has("error")) {
                            Toast.makeText(context, "No Records Found", Toast.LENGTH_LONG).show();

                        }
                    } catch (JSONException e1) {
                        Toast.makeText(context, "Unable to reach server. Please try again", Toast.LENGTH_LONG).show();
                        e1.printStackTrace();
                    }                }

            } else {
                Toast.makeText(getApplicationContext(), "Unable to reach server. Please check your internet connection", Toast.LENGTH_LONG).show();
                //AppUtils.showAlertDialog(context, "Timeout", "Unable to reach Server. Please try again!");
            }



        }

        @Override
        protected void onCancelled() {
            FlightLocationTask = null;
            //showProgress(false);
        }
    }




    private void addMarkersToMap(JSONArray jArray){
//        Iterator<String> iter = json_data
//        int count = json_data.length();
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.arrow_green);

        try {
            //for (int i = 0; i < jArray.length(); i++) {
                //DataFlightSchedule data = new DataFlightSchedule();
                JSONObject jObj = jArray.getJSONObject(0);
                //JSONObject depObject = jObj.getJSONObject("departure");
                JSONObject geoObject = jObj.getJSONObject("geography");
                //JSONObject airlineObject = jObj.getJSONObject("airline");
                //JSONObject flightObject = jObj.getJSONObject("flight");
                double lat = Double.parseDouble(geoObject.getString("latitude"));
                double lon = Double.parseDouble(geoObject.getString("longitude"));
                float dir = Float.parseFloat(geoObject.getString("direction"));



                point = new LatLng(lat,lon);
                String name="One";

//                marker = myMap.addMarker(new MarkerOptions().draggable(true).position(point).title(name_name).icon(bitmapDescriptor).rotation(h));
                marker = myMap.addMarker(new MarkerOptions().position(point).title(name_name).icon(bitmapDescriptor).rotation(dir));

                myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point,10));

              //  array.add(data);

            //}

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }


}
