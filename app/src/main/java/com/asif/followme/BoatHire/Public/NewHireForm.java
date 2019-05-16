package com.asif.followme.BoatHire.Public;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import com.asif.followme.util.AppConstants;
import com.asif.followme.util.AppUtils;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class NewHireForm extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = NewHireForm.class.toString();
    private saveHireTask mHireTask = null;
//    private getTripInfoTask mTripInfoTask = null;
    Context context;
    TextView date_picker, time_picker;
    TextView boat_type_label,date_picker_label, time_picker_label,webview_label,webview2_label;
    private Spinner hireStatusSpinner;
    MultiAutoCompleteTextView multiAutoCompleteTextView;
//    Calendar date;
    Button btnHireSave;
    public EditText hireRemarks;
    String destinations ="[]";
    String departure = "[]";
    String operators = "[]";
    private String calendarDate,calendarTime;
//    int selectedBoat =0;
    int hire_id=0;
    WebView webView,webView2,webView_operator;
    LinearLayout parent_layout;
    private getDeviceTypeTask mTypeTask = null;
    public int v_type;
    private getHireInfoTask mHireInfoTask = null;
    ProgressDialog dialog;
    FlexboxLayout layoutCheckbox;
    JSONArray vData;
    JSONObject boatTypeData = new JSONObject();
    DecimalFormat mFormat= new DecimalFormat("00");
    private LinearLayout destinationBasedLayout,timeBasedLayout;
    private Spinner charterTypeSpinner,timeSelectionSpinner,hireOperatorSpinner;
    ArrayList<String> timeValueList = new ArrayList<>();
    private int durationIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_hire_form);
        // butter knife
        context = this;
        parent_layout = (LinearLayout) findViewById(R.id.parent_layout);
        hireStatusSpinner = (Spinner) findViewById(R.id.hire_status_spinner);
        layoutCheckbox = (FlexboxLayout) findViewById(R.id.layout_checkbox);
        layoutCheckbox.setFlexDirection(FlexDirection.ROW);
        destinationBasedLayout = (LinearLayout) findViewById(R.id.destination_based_layout);
        timeBasedLayout = (LinearLayout) findViewById(R.id.time_based_layout);
        charterTypeSpinner = (Spinner) findViewById(R.id.charter_type_spinner);
        timeSelectionSpinner = (Spinner) findViewById(R.id.time_selection_spinner);
        hireOperatorSpinner = (Spinner) findViewById(R.id.hire_operator_spinner);

        hireOperatorSpinner.setEnabled(false);  //disable it. This feature not developed

        //View view = layoutCheckbox.getChildAt(0);
        //FlexboxLayout.LayoutParams lp = (FlexboxLayout.LayoutParams) view.getLayoutParams();
        //lp.order = -1;
        //lp.flexGrow = 2;
        //view.setLayoutParams(lp);


        dialog = new ProgressDialog(NewHireForm.this);
        date_picker = (TextView) findViewById(R.id.date_picker);
        date_picker_label = (TextView) findViewById(R.id.date_picker_label);
        time_picker_label = (TextView) findViewById(R.id.time_picker_label);
        webview_label = (TextView) findViewById(R.id.webview_label);
        webview2_label = (TextView) findViewById(R.id.webview2_label);
        boat_type_label = (TextView) findViewById(R.id.boat_type_label);
        time_picker = (TextView) findViewById(R.id.time_picker);
        hireRemarks = (EditText) findViewById(R.id.hire_remarks);
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");
        webView2 = (WebView) findViewById(R.id.webview2);
        webView2.getSettings().setJavaScriptEnabled(true);
        webView2.addJavascriptInterface(new WebAppInterface2(this), "Android");
        webView_operator = (WebView) findViewById(R.id.webview_operator);
        webView_operator.getSettings().setJavaScriptEnabled(true);
        webView_operator.addJavascriptInterface(new WebAppInterfaceOperators(this), "Android");
        //multiAutoCompleteTextView = (MultiAutoCompleteTextView)findViewById(R.id.multiAutoCompleteTextViewEmail);
       // multiAutoCompleteTextView.setPadding(15,15,15,15);
        //multiAutoCompleteTextView.setBackgroundColor(getResources().getColor(R.color.colorBlue));
        //multiAutoCompleteTextView.setTextColor(getResources().getColor(R.color.colorGreen));
        btnHireSave = (Button) findViewById(R.id.btn_hire_save);
        btnHireSave.setOnClickListener(this);
        charterTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeCharterType(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });

    }


    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        Boolean reload = false;
        hire_id = 0;
        try{
            Bundle extras = getIntent().getExtras();
            hire_id = extras.getInt("hire_id");

        } catch (Exception e){
        }
        System.out.println("HIRE ID:"+hire_id);
//      webView.loadData("Loading...", "text/html", "UTF-8");
        webView.loadUrl(AppConstants.BOAT_HIRE_WEBVIEW_DEPT_URL+"?id="+hire_id);
        webView2.loadUrl(AppConstants.BOAT_HIRE_WEBVIEW_URL+"?id="+hire_id);
        webView_operator.loadUrl(AppConstants.BOAT_OPERATORS_WEBVIEW_URL+"?id="+hire_id);
        if(hire_id > 0) {   //edit
            setTitle("Edit Boat Charter");
            //parent_layout.setVisibility(View.GONE);
            mHireInfoTask = new getHireInfoTask(hire_id);
            mHireInfoTask.execute((Void) null);
        } else {
            setTitle("Boat Charter");
//            mNameValueTask = new getNameValueTask("my_trip_boats");
//            mNameValueTask.execute((Void) null);
            mTypeTask = new getDeviceTypeTask();
            mTypeTask.execute((Void) null);

        }


    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        System.out.println(v.getId());
        System.out.println("OnClick");
        switch (v.getId()) {
            case R.id.btn_hire_save:
                saveBoatHire();
               // showDateTimePicker();
            //    break;

        }
    }

    private void changeCharterType(int type){
        if(type==0){
            date_picker_label.setText("Trip Date");
            time_picker_label.setText("Departure Time");
            destinationBasedLayout.setVisibility(View.VISIBLE);
            timeBasedLayout.setVisibility(View.GONE);
        } else {
            date_picker_label.setText("Charter Date");
            time_picker_label.setText("Start Time");
            destinationBasedLayout.setVisibility(View.GONE);
            String[]  array_name = null;
//            String[]  array_values = null;
            switch(type){
                case 2:
                    array_name= getResources().getStringArray(R.array.day_selection_array);
//                    array_values= getResources().getStringArray(R.array.day_values_array);
                    break;
                case 3:
                    array_name= getResources().getStringArray(R.array.month_selection_array);
//                    array_values= getResources().getStringArray(R.array.month_values_array);
                    break;
                default:
                    array_name= getResources().getStringArray(R.array.hour_selection_array);
//                    array_values= getResources().getStringArray(R.array.hour_values_array);
                    break;
            }
            ArrayList<String> namesList= new ArrayList<String>(Arrays.asList(array_name));
//            timeValueList= new ArrayList<String>(Arrays.asList(array_values));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, namesList);
            timeSelectionSpinner.setAdapter(adapter);
            timeSelectionSpinner.setSelection(durationIndex);
            timeBasedLayout.setVisibility(View.VISIBLE);

//            ArrayAdapter<Data_vtypes> adapter = new ArrayAdapter<Data_vtypes>(getBaseContext(), android.R.layout.simple_spinner_item, AppUtils.setVesselTypes(jObj, getBaseContext()));
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        }
    }
    private void showError(TextView v,String error){
        v.setError(error);
        v.requestFocus();
    }

    public void saveBoatHire() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
       // String tripDate = date_picker.getText().toString();
 //       String tripDate = calendarDate;
        String tripDate = date_picker.getText().toString();
        String depTime = time_picker.getText().toString();
        String tripDateTime = tripDate+' '+time_picker.getText().toString();
        System.out.println("Trip DATE:"+tripDate);
        System.out.println("Dep Time:"+tripDateTime);
        int hireStatus = hireStatusSpinner.getSelectedItemPosition()+1;
        int hireType = charterTypeSpinner.getSelectedItemPosition()+1;
//        int hireDurationIndex = timeSelectionSpinner.getSelectedItemPosition();
        String hireDurationText = "";
//        int hireDuration = 0;
//        int durationIndex = 0;
        System.out.println("Boat Type:"+boatTypeData+",Length:"+boatTypeData.length());
        if(hireType>1){
            hireDurationText = timeSelectionSpinner.getSelectedItem().toString();
            durationIndex = timeSelectionSpinner.getSelectedItemPosition();
//            hireDuration = Integer.parseInt(timeValueList.get(hireDurationIndex));
        }
        if(boatTypeData.length()==0){
            showError(boat_type_label,"Please Choose a Boat Type");
            return;
        } else {
            showError(boat_type_label,null);
        }

        //selectedBoat -already set
        //destinations - already set
//        int tripStatus = tripStatusSpinner.getSelectedItemPosition()+1;
        String remarks = hireRemarks.getText().toString();
        if(tripDate.equalsIgnoreCase("")){
            showError(date_picker_label,"Please Select Trip Date");
            return;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String strToday = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            try {
                Date strDate = sdf.parse(tripDate);
                Date today = sdf.parse(strToday);
                //System.out.println("Today:"+today);
                //System.out.println("Set Date:"+strDate);
                if(hire_id == 0) {
                    if (today.after(strDate)) {
                        showError(date_picker_label, "Enter Today or earlier Date");
                        return;
                    } else {
                        showError(date_picker_label, null);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
                showError(date_picker_label,"Invalid Date");
                return;
            }
          //  final Calendar today = Calendar.getInstance();
        }

        if(depTime.equalsIgnoreCase("")){
            showError(time_picker_label,"Please Select Depature Time");
            //time_picker_label.setError("Please Select Departure Time");
            //time_picker_label.requestFocus();
            return;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            String strToday = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(new Date());
            System.out.println("String  Today:"+strToday);
            try {
                System.out.println("DepTime:"+tripDateTime);
                Date strDate = sdf.parse(tripDateTime);
                Date today = sdf.parse(strToday);
                System.out.println("Now Time:"+today);
                System.out.println("Set Time:"+strDate);
                if (today.after(strDate)) {
                    //time_picker_label.setError("Time is old");
                    showError(time_picker_label,"Time is old");
                    return;
                } else {
                    showError(time_picker_label, null);
                }
            } catch (Exception e){
                e.printStackTrace();
                showError(time_picker_label,"Invaid Time");
                //time_picker_label.setError("Invalid Time");
                return;
            }
            //  final Calendar today = Calendar.getInstance();
        }
        Log.d("saveTrip","Depature:"+departure);
        Log.d("saveTrip","Destinations:"+destinations);
        try {
            JSONArray deptObject = new JSONArray(departure);
            if(hireType ==1) {
                JSONArray destObject = new JSONArray(destinations);
                for (int i = 0; i < destObject.length(); i++) {
                    JSONObject jsonObject = destObject.getJSONObject(i);
                    deptObject.put(jsonObject);

                }
                destinations = deptObject.toString();
                if(deptObject.length()==0){
                    showError(webview_label,"Please enter Depature Island");
                    return;
                }
                if (destObject.length()==0) {
                    showError(webview2_label,"Please enter Destination Island(s) in order");
                    Log.d("saveTrip", "Please enter Departure and Arrival Islands");
                    return;
                }
            } else {    //hourly/daily hire
                destinations = deptObject.toString();
                if (deptObject.length() == 0) { //no depature island set
                    showError(webview_label,"Please enter Departure Island");
                    Log.d("saveTrip", "Please enter Departure");
                    return;
                }

            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Please enter Destinations", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return;
        }

        mHireTask = new saveHireTask(tripDateTime, "new", hire_id, v_type, destinations, remarks, hireStatus, boatTypeData,hireType, hireDurationText, durationIndex);
        mHireTask.execute((Void) null);

    }

    public void showDatePicker(View view) {
//        final Calendar currentDate = Calendar.getInstance();
        final Calendar calendar = Calendar.getInstance();
        String dateString = date_picker.getText().toString();
        if(!dateString.equalsIgnoreCase("")) {
            String[] date = dateString.split("-");
            calendar.set(Calendar.MONTH, Integer.parseInt(date[1])-1);
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[0]));
            calendar.set(Calendar.YEAR, Integer.parseInt(date[2]));
        }
        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                calendarDate = mFormat.format(calendar.get(Calendar.DAY_OF_MONTH))+"-"+mFormat.format(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.YEAR);
                date_picker.setText(calendarDate);
                //date_picker.setError("Invalid Date");
                System.out.println("Calender Date:"+calendarDate);
                date_picker_label.setError(null);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();
    }
    public void showTimePicker(View view) {
        System.out.println("ShowTimePicker..................................");
        final Calendar calendar = Calendar.getInstance();
        String timeString = time_picker.getText().toString();
        if(!timeString.equalsIgnoreCase("")) {
            String[] t = timeString.split(":");
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(t[1]));
        }
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                calendarTime = mFormat.format(selectedHour)+":"+mFormat.format(selectedMinute);
                time_picker.setText(calendarTime);
                time_picker_label.setError(null);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.show();

    }


/*    public void showDateTimePicker(View view) {
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
                        calendarDate = mFormat.format(date.get(Calendar.DAY_OF_MONTH))+"-"+mFormat.format(date.get(Calendar.MONTH)+1)+"-"+date.get(Calendar.YEAR)+" "+mFormat.format(date.get(Calendar.HOUR_OF_DAY))+":"+mFormat.format(date.get(Calendar.MINUTE));
                        date_picker.setText(calendarDate);
                        System.out.println("Calender Date:"+calendarDate);
                        date_picker.setError(null);
                        //Log.v(TAG, "The choosen one " + date.getTime());
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

*/

    public class WebAppInterface {
        Context mContext;

        WebAppInterface(Context ctx){
            this.mContext=ctx;
        }


        @JavascriptInterface
        public void sendData(String element, String data) {
            System.out.println("Received Token Data:"+data);
            //Get the string value to process
                departure = data;
            System.out.println("Departure:"+departure);
        }
    }
    public class WebAppInterface2 {
        Context mContext;

        WebAppInterface2(Context ctx){
            this.mContext=ctx;
        }


        @JavascriptInterface
        public void sendData(String element, String data) {
            System.out.println("Received Token Data:"+data);
            //Get the string value to process
                destinations = data;
            System.out.println("Destinations:"+destinations);
        }
    }
    public class WebAppInterfaceOperators {
        Context mContext;

        WebAppInterfaceOperators(Context ctx){
            this.mContext=ctx;
        }


        @JavascriptInterface
        public void sendData(String element, String data) {
            System.out.println("Received Token Data:"+data);
            //Get the string value to process
            operators = data;
            System.out.println("Destinations:"+operators);
        }
    }

    public class saveHireTask extends AsyncTask<Void, Void, String> {
        String date;
        int v_type;
        String dest;
        String remarks;
        int trip_id;
        int trip_status;
        String action;
        JSONObject boatTypes;
        int hire_type;
        String hire_duration;
        int duration_index;

        saveHireTask(String date, String action, int trip_id, int v_type, String dest, String remarks, int status, JSONObject boatTypes, int hire_type, String hire_duration, int duration_index) {
            this.date = date;
            this.v_type = v_type;
            this.dest = dest;
            this.remarks = remarks;
            this.trip_status = status;
            this.action = action;
            this.trip_id = trip_id;
            this.boatTypes = boatTypes;
            this.hire_type = hire_type;
            this.hire_duration = hire_duration;
            this.duration_index=duration_index;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Saving...");
            dialog.show();

        }

        @Override
        protected String doInBackground(Void... arg0) {
            // TODO: attempt authentication against a network service.

            try {
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.saveBoatHire(context, action, trip_id, date, v_type, dest, remarks, trip_status, boatTypes, hire_type, hire_duration, duration_index);
            } catch (Exception ex) {
                //finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String result) {
            mHireTask = null;
            dialog.dismiss();
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
            mHireTask = null;
            //showProgress(false);
        }
    }


    public class getDeviceTypeTask extends AsyncTask<Void, Void, String> {
        getDeviceTypeTask() {
        }

        @Override
        protected String doInBackground(Void... arg0) {
            // TODO: attempt authentication against a network service.

            try {
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.getDeviceTypes(2,context);
            } catch (Exception ex) {
                //finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String result) {
            mTypeTask = null;
            //showProgress(false);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
                            //processed at background
                        //    ArrayAdapter<Data_vtypes> adapter = new ArrayAdapter<Data_vtypes>(getBaseContext(), android.R.layout.simple_spinner_item, AppUtils.setVesselTypes(jObj, getBaseContext()));
                        //    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //    vesselTypeSpinner.setAdapter(adapter);

                            try {
                               vData = jObj.getJSONArray("typeData");
                               boatTypeData = new JSONObject();
                               layoutCheckbox.removeAllViews();
                                for (int i = 0; i < vData.length(); i++) {
                                    JSONArray jsonArray = vData.getJSONArray(i);
                                    createCheckBox(jsonArray.getInt(0),jsonArray.getString(1),false);
                                    //	System.out.println(jsonArray);
                                   // vtypeList.add(new Data_vtypes(i,jsonArray.getInt(0),jsonArray.getString(1)));
                                    //   countryList.add(new Data_vtypes("1", "India"));

                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }


                   //         for (int position=0; position < adapter.getCount(); position++) {
                   //             if (adapter.getItem(position).getId() == 6) {   //6-person
                   //                 v_type = adapter.getItem(position).getId();
                   //                 vesselTypeSpinner.setSelection(adapter.getItem(position).getIndex());
                   //             }
                   //         }                            //	deviceTypeSpinner.setSelection(((ArrayAdapter<String>)deviceTypeSpinner.getAdapter()).getPosition(jObj.getString("vtype")));
                            //	int spinnerPosition = adapter.getPosition(((ArrayAdapter<String>)mySpinner.getAdapter()).getPosition(myString));
                            // vesselTypeSpinner.setSelection(jObj.getInt("vtype")-1); //not the correct way
                            //	Data_vtypes country =  jObj.getInt("vtype");
 /*                           vesselTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    Data_vtypes vtypes = (Data_vtypes) parent.getSelectedItem();
                                    v_type = vtypes.getId();
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
                            */
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
            mTypeTask = null;
            //showProgress(false);
        }
    }

    public void createCheckBox(int id, String label, boolean isChecked) {
        final CheckBox checkBox = new CheckBox(this);
//        final CheckBox checkBox = new CheckBox(new ContextThemeWrapper(context, R.style.myCheckBoxStyle));
        //checkBox.getLayoutParams().width = LayoutParams.WRAP_CONTENT;
        //checkBox.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        checkBox.setId(id);
        checkBox.setText(label);
        checkBox.setBackgroundResource(R.drawable.border_checkbox_boats);
        checkBox.setChecked(isChecked);
//        JSONArray jArray = new JSONArray();
//        String[] str = new String[3];
        if(isChecked){
            try {
                JSONObject bt = new JSONObject();
/*                str[0] =Integer.toString(id);
                str[1] = isChecked ? "1" : "0";
                str[2] = checkBox.getText().toString();
*/
//                boatTypeData.put("c_"+id,isChecked);
                bt.put("id", id);
                bt.put("status",isChecked);
                bt.put("name", checkBox.getText().toString());
                boatTypeData.put("c_"+id,bt);
            } catch (JSONException e){

            }
        }
        //checkBox.setTextAppearance(R.attr.textAppearanceSmall);
       //checkBox.setButtonDrawable(R.drawable.border);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boat_type_label.setError(null);
                try {
                    JSONObject bt = new JSONObject();
                /*    String[] str = new String[3];
                    str[0] = Integer.toString(buttonView.getId());
                    str[1] = isChecked ? "1" : "0";
                    str[2] = buttonView.getText().toString();
                    */
                    bt.put("id", buttonView.getId());
                    bt.put("status",isChecked);
                    bt.put("name", buttonView.getText().toString());
                    boatTypeData.put("c_"+buttonView.getId(),bt);
                    Log.d("TAG", boatTypeData.toString());
                } catch (JSONException e){

                }
            }
        });
        //allViews.add(checkBox);
        layoutCheckbox.addView(checkBox);
    }

    public class getHireInfoTask extends AsyncTask<Void, Void, String> {
        int trip_id;
        getHireInfoTask(int trip_id) {
            this.trip_id = trip_id;
        }

        @Override
        protected String doInBackground(Void... arg0) {
            // TODO: attempt authentication against a network service.

            try {
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.getBoatHireInfo(context, trip_id, "read");
            } catch (Exception ex) {
                //finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String result) {
            mHireInfoTask = null;
            //showProgress(false);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
                        //    parent_layout.setVisibility(View.VISIBLE);
                            date_picker.setText(jObj.getString("date"));
                            time_picker.setText(jObj.getString("time"));
                            hireRemarks.setText(jObj.getString("remarks"));
                            //String device_id = jObj.getString("device_id");
                            int hire_status = jObj.getInt("tender_status");
                         //   v_type = jObj.getInt("boat_type");

                        //    ArrayAdapter<NameValuePairs> adapter = new ArrayAdapter<NameValuePairs>(getBaseContext(), android.R.layout.simple_spinner_item, AppUtils.setNameValuePairs(jObj, getBaseContext()));
                        //    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //    vesselTypeSpinner.setAdapter(adapter);

                        /*    for (int position=0; position < adapter.getCount(); position++) {
                                if (adapter.getItem(position).getId() == jObj.getInt("boat_type")) {
                                    System.out.println("Find:"+jObj.getInt("boat_type")+",Position:"+position+", Index:"+adapter.getItem(position).getIndex()+",VType:"+adapter.getItem(position).getId()+",Type Name:"+adapter.getItem(position).getName());
                                    System.out.println("Found");
                                   // selectedBoat = adapter.getItem(position).getId();
                                    vesselTypeSpinner.setSelection(position);
                                    // vesselSpinner.setSelection(adapter.getItem(position).getIndex());
                                }
                            }
*/

                            try {
                                vData = jObj.getJSONArray("pairs");
                                boolean isChecked = false;
                                boatTypeData = new JSONObject();
                                layoutCheckbox.removeAllViews();
                                for (int i = 0; i < vData.length(); i++) {
                                    JSONArray jsonArray = vData.getJSONArray(i);
                                    if(jsonArray.getString(2).equalsIgnoreCase("null")){
                                        isChecked = false;
                                    } else {
                                        isChecked = true;
                                    }
                                    createCheckBox(jsonArray.getInt(0),jsonArray.getString(1),isChecked);
                                    //	System.out.println(jsonArray);
                                    // vtypeList.add(new Data_vtypes(i,jsonArray.getInt(0),jsonArray.getString(1)));
                                    //   countryList.add(new Data_vtypes("1", "India"));

                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }




                           // tripStatusSpinner.setSelection(trip_status-1);

/*                           vesselTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    NameValuePairs pair_id = (NameValuePairs) parent.getSelectedItem();
                                    v_type = pair_id.getId();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
*/
                            //ArrayAdapter<NameValuePairs> adapter2 = new ArrayAdapter<NameValuePairs>(getBaseContext(), android.R.layout.simple_spinner_item, AppUtils.setNameValuePairs(jObj.getJSONObject("islands"), getBaseContext()));
                            //multiAutoCompleteTextView.setAdapter(adapter2);
                            //multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

                            // String emailArr[] = getResources().getStringArray(R.array.atoll_list);
//                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, emailArr);
//                            multiAutoCompleteTextView.setAdapter(arrayAdapter);
//                            multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

                     /*       for (int position=0; position < adapter.getCount(); position++) {
                                if (adapter.getItem(position).getId() == 6) {   //6-person
                                    //v_type = adapter.getItem(position).getId();
                                    vesselSpinner.setSelection(adapter.getItem(position).getIndex());
                                }
                            }
                  */
                            //	charterTypeSpinner.setSelection(((ArrayAdapter<String>)charterTypeSpinner.getAdapter()).getPosition(jObj.getString("hire_type")));
                            //	int spinnerPosition = adapter.getPosition(((ArrayAdapter<String>)mySpinner.getAdapter()).getPosition(myString));
                            // vesselTypeSpinner.setSelection(jObj.getInt("vtype")-1); //not the correct way
                            //	Data_vtypes country =  jObj.getInt("vtype");
                            durationIndex = jObj.getInt("duration_index");
                            charterTypeSpinner.setSelection(jObj.getInt("hire_type")-1);
      /*                      vesselTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    NameValuePairs pair_id = (NameValuePairs) parent.getSelectedItem();
                                    v_type = pair_id.getId();
                                    //   v_type = vtypes.getId();
                                    //    Data_vtypes vtype = (Data_vtypes) parent.getSelectedItem();
                                    //    System.out.println("VTYPE: "+vtype);
                                    //           v_type = ((Data_vtypes) parent.getSelectedItem()).getId();
                                    //     Toast.makeText(context, "Country ID: "+country.getId()+",  Country Name : "+country.getName(), Toast.LENGTH_SHORT).show();
                                    //System.out.println("VTYPE=============================: "+vtype+"ID:"+v_type);
                                }
*
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                            */
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
            mHireInfoTask = null;
            //showProgress(false);
        }
    }

}
