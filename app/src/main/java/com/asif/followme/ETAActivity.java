package com.asif.followme;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.*;
import com.asif.followme.MyAccount.MyBoatsFragment;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.model.DataETA;
import com.asif.followme.util.AppConstants;
import com.asif.followme.util.AppUtils;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by user on 1/6/2018.
 */

public class ETAActivity extends Activity implements View.OnClickListener {
    public static String RESULT_IDS = "my_menu_ids";
    Context context;
    private LinearLayout btnLayout;
//    public TextView etaLabel, distanceLabel;
//    private TextView destinationSpinnerLabel;
    private TextView webViewLabel;
    private TextView autoCheckLabel;
    private AsyncTask<Void, Void, String> SharedPostAsyncTask;
    private Dialog sDialog;
//    private ProgressBar progressBar;
    private ETATask etaTask = null;
    private Spinner destinationSpinner;
    private Spinner destinationMethodSpinner;
    private LinearLayout autoDestLayout;
//    private LinearLayout manualDestLayout;
    private LinearLayout autoStartLayout,autoFinishLayout;
    private Button setDestBtn;
    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eta_form);
        context = this;
        setTitle("Choose Destination");
//        destinationSpinnerLabel = (TextView) findViewById(R.id.destination_spinner_label);
        autoCheckLabel = (TextView) findViewById(R.id.auto_check_label);
        destinationSpinner = (Spinner) findViewById(R.id.destination_spinner);
//        destinationMethodSpinner = (Spinner) findViewById(R.id.destination_method_spinner);
        autoDestLayout = (LinearLayout) findViewById(R.id.auto_destination_layout);
        autoStartLayout = (LinearLayout) findViewById(R.id.auto_start_layout);
        autoFinishLayout = (LinearLayout) findViewById(R.id.auto_finish_layout);
        webViewLabel = (TextView) findViewById(R.id.webview_label);
//        manualDestLayout = (LinearLayout) findViewById(R.id.manual_destination_layout);
//        manualDestLayout.setVisibility(View.GONE);
        setDestBtn = (Button) findViewById(R.id.set_dest_btn);
        setDestBtn.setOnClickListener(this);

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebAppInterface(context), "Android");
        webView.loadUrl(AppConstants.WEBVIEW_ETA_URL+"?id="+0);
//        webView.setVisibility(View.GONE);

        //       share_email = new EditText(new ContextThemeWrapper(this, R.style.App_EditTextDisable), null, 0);
//        distanceLabel = (TextView) findViewById(R.id.distance_label);
//        etaLabel = (TextView) findViewById(R.id.eta_label);
//        progressBar = (ProgressBar)  findViewById(R.id.loading_spinner);
    }
    @Override
    public void onStart() {
        super.onStart();
        etaTask=new ETATask("auto",MyBoatsFragment.selected_item_id, 0);
        etaTask.execute();
//        autoStartLayout.setVisibility(View.VISIBLE);
//        autoDestLayout.setVisibility(View.VISIBLE);
//        autoFinishLayout.setVisibility(View.GONE);
       // autoDestLayout.setVisibility(View.GONE);
//        manualDestLayout.setVisibility(View.GONE);
//        webView.loadUrl(AppConstants.WEBVIEW_ETA_URL+"?id="+0);
        System.out.println("Is onStart Loading...............................................................");

/*        try{
            Bundle extras = getIntent().getExtras();
            String action = extras.getString("action");
//            setTitle(extras.getString("v_name"));



        } catch (Exception e){
            // System.out.println(e);
            // vname="";
        }
*/
    }
    @Override
    public void onClick(View view) {
        System.out.println("Clicked:"+view.getId());
        switch (view.getId() /*to get clicked view id**/) {
            case R.id.set_dest_btn:
                if(destinationSpinner.getCount()==0){
//                    destinationSpinner.setError
                }
//                autoDestLayout.setVisibility(View.GONE);
//                manualDestLayout.setVisibility(View.VISIBLE);
//                          autoCheckLabel.setText("Destination Unknown...");
//                            autoDestLayout.setVisibility(View.GONE);
//                            manualDestLayout.setVisibility(View.VISIBLE);
//                webView.loadUrl(AppConstants.WEBVIEW_ETA_URL+"?id="+0);
                  UpdateETA();
//                MyMapsActivity.etaPanel.setVisible(true);
                finish();
                break;
        }

    }
    public class WebAppInterface {
        Context mContext;
        WebAppInterface(Context ctx){
            this.mContext=ctx;
        }

        @JavascriptInterface
        public void sendData(String element, String data) {
            System.out.println("Received Webview Token Data:"+data);
            try{
                JSONObject etObj = new JSONObject(data);
                //String eta_lat = etObj.getString("xx");
                //String eta_lon = etObj.getString("yy");
                double lat = Double.parseDouble(etObj.getString("xx"));
                double lon = Double.parseDouble(etObj.getString("yy"));
                MyMapsActivity.etaDestPoint = new LatLng(lat,lon);

                MyMapsActivity.etaIsland = etObj.getString("name");
//                MyMapsActivity.etaIslandLabel.setText(etObj.getString("name"));
//                UpdateETA(eta_lat,eta_lon,etObj.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

                //Get the string value to process
            //departure = data;
            //System.out.println("Departure:"+departure);
        }
    }

    public class ETATask extends AsyncTask<Void, Void, String> {
        private String action;
        private final String device_id;
        private final int fence_id;

        ETATask(String action, String device_id, int fence_id) {
            this.action = action;
            this.fence_id = fence_id;
            this.device_id=device_id;
        }
        @Override
        protected void onPreExecute() {
            // swipeLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);
            // swipeLayout.setRefreshing(true);
            //sDialog= ProgressDialog.show(NoticeActivity.this, null, "Please wait...");
            //sDialog.show();
//            autoCheckLabel.setText("Checking Destination....");
//            autoDestLayout.setVisibility(View.VISIBLE);
            autoFinishLayout.setVisibility(View.GONE);
            autoStartLayout.setVisibility(View.VISIBLE);
            webViewLabel.setVisibility(View.GONE);
            webView.setVisibility(View.GONE);
//            manualDestLayout.setVisibility(View.GONE);
//            destinationSpinnerLabel.setText("Predicting Destination....");
//            destinationSpinner.setVisibility(View.GONE);
//            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... arg0) {
            try{
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.getETA(context,action,device_id,fence_id);
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onCancelled() {
            // if(sDialog.isShowing()){
            //     sDialog.dismiss();
            // }
//            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //if(sDialog.isShowing()){
            //     sDialog.dismiss();
            //}
//            progressBar.setVisibility(View.GONE);
//            swipeLayout.setRefreshing(false);
            //System.out.println(result);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        if (jObj.getString("status").equalsIgnoreCase("ok")) {
                            ArrayAdapter<DataETA> adapter = new ArrayAdapter<DataETA>(getBaseContext(), android.R.layout.simple_spinner_item, AppUtils.setETA(jObj, getBaseContext()));
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            destinationSpinner.setAdapter(adapter);
                            String eta_lat = adapter.getItem(0).getLat();
                            String eta_lon = adapter.getItem(0).getLon();
                            MyMapsActivity.etaIsland = adapter.getItem(0).getName();
                            MyMapsActivity.etaIslandLabel.setText(adapter.getItem(0).getName());

                            double lat = Double.parseDouble(adapter.getItem(0).getLat());
                            double lon = Double.parseDouble(adapter.getItem(0).getLon());
                            MyMapsActivity.etaDestPoint = new LatLng(lat,lon);
                            autoStartLayout.setVisibility(View.GONE);
                            autoFinishLayout.setVisibility(View.VISIBLE);

                            //MyMapsActivity.etaIsland = etObj.getString("name");
                            //MyMapsActivity.etaPoint = new LatLng(lat,lon);

                            //MyMapsActivity.etaIsland = etObj.getString("name");
//                MyMapsActivity.etaIslandLabel.setText(etObj.getString("name"));
//                UpdateETA(eta_lat,eta_lon,etObj.getString("name"));

//                            UpdateETA(eta_lat,eta_lon, adapter.getItem(0).getName());

                            destinationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    DataETA dataETA = (DataETA) parent.getSelectedItem();
                                    if(dataETA.getId()==0){     //OTHER
                                        autoDestLayout.setVisibility(View.GONE);
                                        webViewLabel.setVisibility(View.VISIBLE);
                                        webView.setVisibility(View.VISIBLE);

                                    } else {
                                        double lat = Double.parseDouble(dataETA.getLat());
                                        double lon = Double.parseDouble(dataETA.getLon());
                                        MyMapsActivity.etaDestPoint = new LatLng(lat, lon);
                                        MyMapsActivity.etaIsland = dataETA.getName();
                                    }
//                                    UpdateETA(dataETA.getLat(),dataETA.getLon(),dataETA.getName());
  //                                  MyMapsActivity.etaIslandLabel.setText(dataETA.getName());
                                    //v_type = vtypes.getId();
                                    //    Data_vtypes vtype = (Data_vtypes) parent.getSelectedItem();
                                    //    System.out.println("VTYPE: "+vtype);
                                    //           v_type = ((Data_vtypes) parent.getSelectedItem()).getId();
                                    //     Toast.makeText(context, "Country ID: "+country.getId()+",  Country Name : "+country.getName(), Toast.LENGTH_SHORT).show();
                                    //System.out.println("VTYPE=============================: "+vtype+"ID:"+v_type);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });


                        } else {
//                        //    System.out.println("Lets Show Manual Selections...............................................");
 //                           autoDestLayout.setVisibility(View.GONE);
                            autoDestLayout.setVisibility(View.GONE);
                            webViewLabel.setVisibility(View.VISIBLE);
                            webView.setVisibility(View.VISIBLE);
//                            manualDestLayout.setVisibility(View.VISIBLE);
//                            webView.loadUrl(AppConstants.WEBVIEW_ETA_URL+"?id="+0);
//                            manualDestLayout.setVisibility(View.VISIBLE);
//                          autoCheckLabel.setText("Destination Unknown...");
//                            autoDestLayout.setVisibility(View.GONE);
//                            manualDestLayout.setVisibility(View.VISIBLE);
//                            webView.loadUrl(AppConstants.WEBVIEW_ETA_URL+"?id="+0);
                            //webView.setVisibility(View.VISIBLE);
//                            destinationMethodSpinner.setSelection(1);
//                            destinationSpinnerLabel.setText("Destination Unknown...");
                            //  notice_input.setError(jObj.getString("error"));
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                        //	System.out.println("Result"+result);

                    }
                } catch (JSONException e) {
                    //loading.setText("Unable to reach Server...");
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            } else {
                //Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void UpdateETA(){
//        int eta_id = adapter.getItem(0).getId();
//        LatLng p2 = new LatLng(Double.parseDouble(lat),Double.parseDouble(lon));
//        MyMapsActivity.etaDestPoint = p2;
        System.out.println("etaDestPoint:"+MyMapsActivity.etaDestPoint+" +++++++++++++++++++++++++++++++++++++++");
        if(MyMapsActivity.etaDestPoint==null){
            return;
        }

        Double d = getDistance(MyMapsActivity.etaNowPoint,MyMapsActivity.etaDestPoint);
//        String nm = String.format("%.1f",d * 0.000539957)+" NM";
//        distanceLabel.setText(nm);
        Double t = d / (MyMapsActivity.etaNowSpeed * 0.514444);
        Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
        calendar.add(Calendar.SECOND, t.intValue());
        //	System.out.println(sdf.format(calendar.getTime()));
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//        etaLabel.setText(sdf.format(calendar.getTime()));
        MyMapsActivity.etaTimeLabel.setText(sdf.format(calendar.getTime()));
        MyMapsActivity.etaIslandLabel.setText(MyMapsActivity.etaIsland);
        MyMapsActivity.etaPanel.setVisible(true);

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
