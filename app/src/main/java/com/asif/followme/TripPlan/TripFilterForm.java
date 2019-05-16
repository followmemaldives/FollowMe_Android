package com.asif.followme.TripPlan;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.asif.followme.R;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.util.AppUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by user on 1/6/2018.
 */

public class TripFilterForm extends Activity implements View.OnClickListener {
    public static String RESULT_IDS = "my_menu_ids";
    Context context;
    public Button filterBtn;
    private Spinner filterSpinner, vesselSpinner;
    private int filter_index  =0;
    private int sort_index = 0;
    private LinearLayout custom_date_layout, sort_layout;
    private TextView date_picker1, date_picker2;
    private String date_from,date_to;
    DecimalFormat mFormat= new DecimalFormat("00");
    private String nav_name;
    private getTripFilterTask mTripFilterTask = null;



    //    private AsyncTask<Void, Void, String> SharedReadAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_filter_form);
        context = this;

        filterBtn = (Button) findViewById(R.id.trip_filter_btn);
        filterSpinner = (Spinner) findViewById(R.id.filter_spinner);
        vesselSpinner = (Spinner) findViewById(R.id.vessel_spinner);

//        orderSpinner = (Spinner) findViewById(R.id.order_spinner);
        custom_date_layout = findViewById(R.id.custom_date_layout);
        date_picker1 = findViewById(R.id.date_picker1);
        date_picker2 = findViewById(R.id.date_picker2);
        final Calendar calendar = Calendar.getInstance();
        sort_layout = findViewById(R.id.sort_option_layout);
        if(TripPlanFragment.date_from.equals("")) {
            date_from = mFormat.format(calendar.get(Calendar.DAY_OF_MONTH)) + "-" + mFormat.format(calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);
            date_to=date_from;
        } else {
            date_from=TripPlanFragment.date_from;
            date_to = TripPlanFragment.date_to;
        }
        date_picker1.setText(date_from);
        date_picker2.setText(date_to);


        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter_index = position;
                if(filter_index==4){    //Custom date
                    custom_date_layout.setVisibility(View.VISIBLE);
                } else {
                    custom_date_layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });


//        share_email_edit = (EditText) findViewById(R.id.share_email_edit);
 //       share_email = new EditText(new ContextThemeWrapper(this, R.style.App_EditTextDisable), null, 0);
 //       shareLabel1 = (TextView) findViewById(R.id.share_label1);
        filterBtn.setOnClickListener(this);
      //  cancelBtn.setOnClickListener(this);

    }
    @Override
    public void onStart() {

        setTitle("Trip Filter");
        super.onStart();
        try{
            Bundle extras = getIntent().getExtras();
//            vname = extras.getString("vname");
//            bid_id = Integer.parseInt(HireMyFragment.selected_item_id);
//            setTitle("New Bid");

        } catch (Exception e){
 //           vname="";
        }
/*        int filter_index = LogsActivity.filter_index;
        filterSpinner.setSelection(filter_index);
        custom_date_layout.setVisibility(View.GONE);
        if(!date_from.equals("")){date_picker1.setText(date_from);};
        if(!date_to.equals("")){date_picker2.setText(date_to);};
*/
        mTripFilterTask = new getTripFilterTask();
        mTripFilterTask.execute((Void) null);


    }
    @Override
    public void onClick(View view) {
        System.out.println("Clicked:"+view.getId());
        switch (view.getId() /*to get clicked view id**/) {
            case R.id.trip_filter_btn:
                prepareFilter();
                break;
            case R.id.cancel_btn:
                finish();
                break;
        }

    }
    public void prepareFilter(){
 //       System.out.println("Package: "+reload_pkg+" , Device ID:"+device_id);
        //String filter_index = filterSpinner.getSelectedItem().toString();
        //System.out.println("Filter Option Index:"+filter_index);
        DataBoats pair_id = (DataBoats) vesselSpinner.getSelectedItem();
        int device_id = pair_id.getId();

        date_from = date_picker1.getText().toString();
        date_to = date_picker2.getText().toString();
//        sort_index = orderSpinner.getSelectedItemPosition();
        Intent intent = new Intent();
        intent.putExtra("filter_index",filter_index);
        intent.putExtra("date_from",date_from);
        intent.putExtra("date_to",date_to);
        intent.putExtra("device_id",device_id);
        setResult(RESULT_OK,intent );
        finish();
    }

    public void processReload(){
 //       System.out.println("Package: "+reload_pkg+" , Device ID:"+device_id);
 //       mReloadTask = new sendReloadTask("save",reload_pkg,device_id);
//        mReloadTask.execute((Void) null);

    }



    public void showDatePicker1(View view) {
//        final Calendar currentDate = Calendar.getInstance();
        final Calendar calendar = Calendar.getInstance();
        String dateString = date_picker1.getText().toString();
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
                date_from = mFormat.format(calendar.get(Calendar.DAY_OF_MONTH))+"-"+mFormat.format(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.YEAR);
                date_picker1.setText(date_from);
                //date_picker.setError("Invalid Date");
                System.out.println("Calender Date:"+date_from);
               // date_picker_label1.setError(null);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();
    }
    public void showDatePicker2(View view) {
//        final Calendar currentDate = Calendar.getInstance();
        final Calendar calendar = Calendar.getInstance();
        String dateString = date_picker2.getText().toString();
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
                date_to = mFormat.format(calendar.get(Calendar.DAY_OF_MONTH))+"-"+mFormat.format(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.YEAR);
                date_picker2.setText(date_to);
                //date_picker.setError("Invalid Date");
                System.out.println("Calender Date:"+date_to);
                // date_picker_label1.setError(null);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();
    }

/*   public void showDatePicker(View view) {
        final String which_date = view.getTag().toString();
        String dateString;
        final String calendarDate;
        if(which_date.equalsIgnoreCase("date1")){
            dateString = date_picker1.getText().toString();
            final Calendar calendar1 = Calendar.getInstance();
         } else {
            dateString = date_picker2.getText().toString();
            final Calendar calendar2 = Calendar.getInstance();
        }
        final Calendar calendar = calendar1;
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
                if(which_date.equalsIgnoreCase("date1")){
                    calendarDate1 = mFormat.format(calendar.get(Calendar.DAY_OF_MONTH))+"-"+mFormat.format(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.YEAR);
                    date_picker1.setText(calendarDate1);
                } else {
                    calendarDate2 = mFormat.format(calendar.get(Calendar.DAY_OF_MONTH))+"-"+mFormat.format(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.YEAR);
                    date_picker2.setText(calendarDate1);

                }
                //date_picker.setError("Invalid Date");
                System.out.println("Calender Date:"+calendarDate1);
                //date_picker_label1.setError(null);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();
    }
*/

    public class getTripFilterTask extends AsyncTask<Void, Void, String> {
        getTripFilterTask() {}

        @Override
        protected String doInBackground(Void... arg0) {
            // TODO: attempt authentication against a network service.

            try {
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.getTripFilter(context);
            } catch (Exception ex) {
                //finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String result) {
            mTripFilterTask = null;
            //showProgress(false);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
                        //    parent_layout.setVisibility(View.VISIBLE);
                            // List<DataBoats> data = AppUtils.drawTripBoats(jObj.getJSONArray("boats"), context);
                            ArrayAdapter<DataBoats> boatsAdapter = new ArrayAdapter<DataBoats>(getBaseContext(), android.R.layout.simple_spinner_item, AppUtils.setBoatData(jObj.getJSONArray("boats"), getBaseContext()));
                            boatsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            vesselSpinner.setAdapter(boatsAdapter);

//                            ArrayAdapter<NameValuePairs> tripStatusAdapter = new ArrayAdapter<NameValuePairs>(getBaseContext(), android.R.layout.simple_spinner_item, AppUtils.setPairs(jObj.getJSONArray("tstatus"), getBaseContext()));
//                            tripStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            tripStatusSpinner.setAdapter(tripStatusAdapter);


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
//            mNameValueTask = null;
            mTripFilterTask = null;
            //showProgress(false);
        }
    }
}
