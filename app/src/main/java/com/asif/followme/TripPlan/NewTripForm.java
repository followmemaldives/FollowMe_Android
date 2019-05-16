package com.asif.followme.TripPlan;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.*;
import com.asif.followme.R;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.model.NameValuePairs;
import com.asif.followme.util.AppConstants;
import com.asif.followme.util.AppUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class NewTripForm extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = NewTripForm.class.toString();
//    private getNameValueTask mNameValueTask = null;
    private saveTripTask mTripTask = null;
    private getTripInfoTask mTripInfoTask = null;
    Context context;
    TextView datePicker,timePicker;
    EditText seatsQty;
    private Spinner vesselSpinner,tripStatusSpinner,routeSpinner;
    MultiAutoCompleteTextView multiAutoCompleteTextView;
    Button btnTripSave;
    public EditText tripRemarks;
    String destinations ="[]";
    int selectedBoat =0;
    int trip_id=0;
    int route_id =0;
    int first_load =0;
    WebView webView;
    LinearLayout parent_layout;
    TextView boatSelectLabel,destinationsLabel;
    SimpleDateFormat Datesdf = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat Timesdf = new SimpleDateFormat("HH:mm");
    LinearLayout errorLayout, parentLayout;
    private TextView errorTextView;

    //Calendar calendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_new_form);
        // butter knife
        context = this;
        parent_layout = (LinearLayout) findViewById(R.id.parent_layout);
        vesselSpinner = (Spinner) findViewById(R.id.vessel_spinner);
        tripStatusSpinner = (Spinner) findViewById(R.id.trip_status_spinner);
        datePicker = (TextView) findViewById(R.id.date_picker);
        timePicker = (TextView) findViewById(R.id.time_picker);
        tripRemarks = (EditText) findViewById(R.id.trip_remarks);
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");
        boatSelectLabel = (TextView) findViewById(R.id.boat_select_label);
        destinationsLabel = (TextView) findViewById(R.id.destinations_label);
        seatsQty = (EditText) findViewById(R.id.seats_input);
        routeSpinner = (Spinner) findViewById(R.id.route_spinner);

        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        errorTextView = (TextView) findViewById(R.id.error_text_view);
        parentLayout = (LinearLayout) findViewById(R.id.parent_layout);

        //multiAutoCompleteTextView = (MultiAutoCompleteTextView)findViewById(R.id.multiAutoCompleteTextViewEmail);
       // multiAutoCompleteTextView.setPadding(15,15,15,15);
        //multiAutoCompleteTextView.setBackgroundColor(getResources().getColor(R.color.colorBlue));
        //multiAutoCompleteTextView.setTextColor(getResources().getColor(R.color.colorGreen));
        btnTripSave = (Button) findViewById(R.id.btn_trip_save);
        btnTripSave.setOnClickListener(this);
        routeSpinner.setOnItemSelectedListener(this);
        datePicker.setOnClickListener(this);
        timePicker.setOnClickListener(this);
   /*     routeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DataRouteInfo dataRouteInfo = (DataRouteInfo) parent.getSelectedItem();
                route_id = dataRouteInfo.getRouteId();
                System.out.println("Route ID:"+route_id);
                webView.loadUrl(AppConstants.TRIP_PLAN_WEBVIEW_URL+"?trip="+0+"&route="+route_id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });
*/
    }


    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        Boolean reload = false;
        trip_id = 0;
        try{
            Bundle extras = getIntent().getExtras();
            trip_id = extras.getInt("trip_id");

        } catch (Exception e){
        }
        System.out.println("TRIP ID:"+trip_id);
        webView.loadUrl(AppConstants.TRIP_PLAN_WEBVIEW_URL+"?trip="+trip_id+"&route="+route_id);
        if(trip_id > 0) {   //edit
            setTitle("Edit Trip Plan");
            parent_layout.setVisibility(View.GONE);
            mTripInfoTask = new getTripInfoTask(trip_id,"read");
            mTripInfoTask.execute((Void) null);
        } else {
            setTitle("New Trip Plan");
            mTripInfoTask = new getTripInfoTask(trip_id,"read");
            mTripInfoTask.execute((Void) null);
        }


    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        System.out.println(v.getId());
        System.out.println("OnClick");
        switch (v.getId()) {
            case R.id.btn_trip_save:
                saveTrip();
                break;
            case R.id.date_picker:
                showDatePicker();
                break;
            case R.id.time_picker:
                showTimePicker();
                break;

        }
    }

    public void onItemSelected(AdapterView<?> parent, View arg1, int pos,long id) {
        if(first_load > 0) {
            DataRouteInfo dataRouteInfo = (DataRouteInfo) parent.getSelectedItem();
            route_id = dataRouteInfo.getRouteId();
            System.out.println("Route ID:" + route_id);
            webView.loadUrl(AppConstants.TRIP_PLAN_WEBVIEW_URL + "?trip=" + 0 + "&route=" + route_id);
        }
        first_load++;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void saveTrip(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        String tripDate = datePicker.getText().toString();
        String tripTime = timePicker.getText().toString();
        //selectedBoat -already set
        //destinations - already set
        int tripStatus = tripStatusSpinner.getSelectedItemPosition()+1;
        String remarks = tripRemarks.getText().toString();
        if(tripDate.equalsIgnoreCase("")){
            datePicker.setError("Please Select Trip Date");
            return;
        }
        if(tripTime.equalsIgnoreCase("")){
            timePicker.setError("Please Select Departure Time");
            return;
        }
        if(vesselSpinner.getSelectedItem().equals("None")){
            showError(boatSelectLabel,"You do not have any boat with Trip Plan permission");
            return;
        }
        Log.d("saveTrip","Destinations:"+destinations);
        try {
            JSONArray destObject = new JSONArray(destinations);
            if(destObject.length() < 2){
                showError(destinationsLabel,"Please enter Departure and Arrival Islands");
                //Log.d("saveTrip","Please enter Departure and Arrival Islands");
                return;
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Please enter Destinations", Toast.LENGTH_LONG).show();
            return;
        }
        String tripDateTime = tripDate+" "+tripTime;
        mTripTask = new saveTripTask(tripDateTime, "save", trip_id, selectedBoat, destinations, remarks, tripStatus);
        mTripTask.execute((Void) null);

    }

/*    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        //Log.v(TAG, "The choosen one " + date.getTime());
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }
*/

    public void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        if(!datePicker.getText().equals("")){
            try {
                Date date = Datesdf. parse(datePicker.getText().toString());
                calendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                String sDate = Datesdf.format(calendar.getTime());
                datePicker.setText(sDate);
                datePicker.setError(null);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();
    }

    public void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        if(!timePicker.getText().equals("")){
            try {
                Date date = Timesdf. parse(timePicker.getText().toString());
                calendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                calendar.set(Calendar.MINUTE, selectedMinute);
                String sTime = Timesdf.format(calendar.getTime());
                timePicker.setText(sTime);
                timePicker.setError(null);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
    }
/*
    public void showDateTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        if(!date_picker.getText().equals("")){
            try {
                Date date2 = sdf. parse(date_picker.getText().toString());
                calendar.setTime(date2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        String sDate = sdf.format(calendar.getTime());
                        date_picker.setText(sDate);
                //        String dd = date.get(Calendar.DAY_OF_MONTH)+"-"+(date.get(Calendar.MONTH)+1)+"-"+date.get(Calendar.YEAR)+" "+date.get(Calendar.HOUR_OF_DAY)+":"+date.get(Calendar.MINUTE);
                //        date_picker.setText(dd);
                        date_picker.setError(null);
                        //Log.v(TAG, "The choosen one " + date.getTime());
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();
    }

    */

    private void showError(TextView v,String error){
        v.setError(error);
        v.requestFocus();
    }

/*
    public class getNameValueTask extends AsyncTask<Void, Void, String> {
        String pair;
        getNameValueTask(String pair) {
            this.pair = pair;
        }

        @Override
        protected String doInBackground(Void... arg0) {
            // TODO: attempt authentication against a network service.

            try {
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.getSpinnerOptions(context, pair);
            } catch (Exception ex) {
                //finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String result) {
            mNameValueTask = null;
            //showProgress(false);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {


                            //processed at background
                            ArrayAdapter<NameValuePairs> adapter = new ArrayAdapter<NameValuePairs>(getBaseContext(), android.R.layout.simple_spinner_item, AppUtils.setNameValuePairs(jObj, getBaseContext()));
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            vesselSpinner.setAdapter(adapter);
                            if(adapter.getItem(0).getId()==0){
                                boatSelectLabel.setError("You do not have any boat with Trip Plan permission");
                                //showError(boatSelectLabel,"You do not have any boat with Trip Plan permission");
                            }


                            //ArrayAdapter<NameValuePairs> adapter2 = new ArrayAdapter<NameValuePairs>(getBaseContext(), android.R.layout.simple_spinner_item, AppUtils.setNameValuePairs(jObj.getJSONObject("islands"), getBaseContext()));
                            //multiAutoCompleteTextView.setAdapter(adapter2);
                            //multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

                           // String emailArr[] = getResources().getStringArray(R.array.atoll_list);
//                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, emailArr);
//                            multiAutoCompleteTextView.setAdapter(arrayAdapter);
//                            multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());


                              //	deviceTypeSpinner.setSelection(((ArrayAdapter<String>)deviceTypeSpinner.getAdapter()).getPosition(jObj.getString("vtype")));
                            //	int spinnerPosition = adapter.getPosition(((ArrayAdapter<String>)mySpinner.getAdapter()).getPosition(myString));
                            // vesselTypeSpinner.setSelection(jObj.getInt("vtype")-1); //not the correct way
                            //	Data_vtypes country =  jObj.getInt("vtype");
                            vesselSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    NameValuePairs pair_id = (NameValuePairs) parent.getSelectedItem();
                                    selectedBoat = pair_id.getId();
                                 //   v_type = vtypes.getId();
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
                            String errmsg = jObj.getString("error");
                            AppUtils.showAlertDialog(context, "Error", errmsg);
//							Toast.makeText(getApplicationContext(), errmsg, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection 1", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection 2", Toast.LENGTH_LONG).show();
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Unable to reach server. Please check your internet connection 2", Toast.LENGTH_LONG).show();
                //AppUtils.showAlertDialog(context, "Timeout", "Unable to reach Server. Please try again!");
            }


        }

        @Override
        protected void onCancelled() {
            mNameValueTask = null;
            //showProgress(false);
        }
    }
*/
    public class WebAppInterface {
        Context mContext;

        WebAppInterface(Context ctx){
            this.mContext=ctx;
        }


        @JavascriptInterface
        public void sendData(String element, String data) {
            //Get the string value to process
            destinations=data;
            System.out.println("Destinations:"+destinations);
        }
    }


    public class saveTripTask extends AsyncTask<Void, Void, String> {
        String date;
        int boat;
        String dest;
        String remarks;
        int trip_id;
        int trip_status;
        String action;
        saveTripTask(String date, String action, int trip_id, int boat, String dest, String remarks, int status) {
            this.date = date;
            this.boat = boat;
            this.dest = dest;
            this.remarks = remarks;
            this.trip_status = status;
            this.action = action;
            this.trip_id = trip_id;
        }

        @Override
        protected String doInBackground(Void... arg0) {
            // TODO: attempt authentication against a network service.

            try {
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.saveTripPlans(context, action, trip_id, date, boat, dest, remarks, trip_status);
            } catch (Exception ex) {
                //finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String result) {
            mTripTask = null;
            //showProgress(false);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {

                            Intent intent = getIntent();
                            intent.putExtra("action", "reload");
                            setResult(RESULT_OK,intent );
                            finish();

                        } else {
                            String errmsg = jObj.getString("error");
                            AppUtils.showAlertDialog(context, "Error", errmsg);
//							Toast.makeText(getApplicationContext(), errmsg, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection 1", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection 2", Toast.LENGTH_LONG).show();
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Unable to reach server. Please check your internet connection 2", Toast.LENGTH_LONG).show();
                //AppUtils.showAlertDialog(context, "Timeout", "Unable to reach Server. Please try again!");
            }


        }

        @Override
        protected void onCancelled() {
            mTripTask = null;
            //showProgress(false);
        }
    }

    public class getTripInfoTask extends AsyncTask<Void, Void, String> {
        int trip_id;
        String action;
        getTripInfoTask(int trip_id, String action) {
            this.trip_id = trip_id;
            this.action = action;
        }

        @Override
        protected String doInBackground(Void... arg0) {
            // TODO: attempt authentication against a network service.

            try {
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.getTripInfo(context, trip_id, action);
            } catch (Exception ex) {
                //finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String result) {
            mTripInfoTask = null;
            //showProgress(false);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
                            parent_layout.setVisibility(View.VISIBLE);
                            errorLayout.setVisibility(View.GONE);
                           // List<DataBoats> data = AppUtils.drawTripBoats(jObj.getJSONArray("boats"), context);
                            ArrayAdapter<DataBoats> boatsAdapter = new ArrayAdapter<DataBoats>(getBaseContext(), android.R.layout.simple_spinner_item, AppUtils.setBoatData(jObj.getJSONArray("boats"), getBaseContext()));
                            boatsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            vesselSpinner.setAdapter(boatsAdapter);
                            if(boatsAdapter.getCount()==0){
                                errorLayout.setVisibility(View.VISIBLE);
                                parentLayout.setVisibility(View.GONE);
                                errorTextView.setText("You dont have any device with correct permission for Trip Plan.\nPlease update permission for your boats under\nMY DEVICES > Share & Permissions ");
                            }

                            ArrayAdapter<NameValuePairs> tripStatusAdapter = new ArrayAdapter<NameValuePairs>(getBaseContext(), android.R.layout.simple_spinner_item, AppUtils.setPairs(jObj.getJSONArray("tstatus"), getBaseContext()));
                            tripStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            tripStatusSpinner.setAdapter(tripStatusAdapter);

                            ArrayAdapter<DataRouteInfo> routesAdapter = new ArrayAdapter<DataRouteInfo>(getBaseContext(), android.R.layout.simple_spinner_item, AppUtils.setRouteData(jObj.getJSONArray("routeData"), getBaseContext()));
                            routesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            routeSpinner.setAdapter(routesAdapter);

                            int trip_status = 0;

                            //if(action.equals("read")){  //read data for edit form update
                            //} else {
                                datePicker.setText(jObj.getString("date"));
                                timePicker.setText(jObj.getString("time"));
                                tripRemarks.setText(jObj.getString("remarks"));
                                int device_id = jObj.getInt("device_id");
                                trip_status = jObj.getInt("trip_status");
                                for (int position=0; position < boatsAdapter.getCount(); position++) {
                                    if (boatsAdapter.getItem(position).getId() == device_id) {
                                        //selectedBoat = boatsAdapter.getItem(position).getId();
                                        vesselSpinner.setSelection(position);
                                    }
                                }

                                tripStatusSpinner.setSelection(trip_status-1);
                            //}


                            vesselSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    DataBoats pair_id = (DataBoats) parent.getSelectedItem();
                                    selectedBoat = pair_id.getId();
                                    String na = pair_id.getName();
                                    String seats = String.valueOf(pair_id.getSeats());
                                    System.out.println("Name:"+na+",Seats:"+seats);
                                    seatsQty.setText(seats);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });

                        } else {
                            errorLayout.setVisibility(View.VISIBLE);
                            parent_layout.setVisibility(View.GONE);
                            String errmsg = jObj.getString("error");
                            AppUtils.showAlertDialog(context, "Error", errmsg);
//							Toast.makeText(getApplicationContext(), errmsg, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection 1", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection 2", Toast.LENGTH_LONG).show();
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Unable to reach server. Please check your internet connection 2", Toast.LENGTH_LONG).show();
                //AppUtils.showAlertDialog(context, "Timeout", "Unable to reach Server. Please try again!");
            }


        }

        @Override
        protected void onCancelled() {
//            mNameValueTask = null;
            mTripInfoTask = null;
            //showProgress(false);
        }
    }


}
