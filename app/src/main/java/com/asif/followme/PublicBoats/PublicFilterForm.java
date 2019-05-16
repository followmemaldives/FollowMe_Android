package com.asif.followme.PublicBoats;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.asif.followme.R;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.model.Data_vtypes;
import com.asif.followme.util.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 1/6/2018.
 */

public class PublicFilterForm extends Activity implements View.OnClickListener {
    public static String RESULT_IDS = "my_menu_ids";
    Context context;

//    private AsyncTask<Void, Void, String> SharedReadAsyncTask;
    private Dialog sDialog;


    private Spinner atollSpinner,vesselTypeSpinner;
    private getDeviceTypeTask mTypeTask = null;
    public int v_type;
    public Button btnFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_filter_form);
        context = this;

        vesselTypeSpinner = (Spinner) findViewById(R.id.vessel_type_spinner);
        atollSpinner = (Spinner) findViewById(R.id.atolls_spinner);
        btnFilter = (Button) findViewById(R.id.device_filer_btn);
        btnFilter.setOnClickListener(this);

        mTypeTask = new getDeviceTypeTask();
        mTypeTask.execute((Void) null);

    }
    @Override
    public void onStart() {
        super.onStart();
        try{
            Bundle extras = getIntent().getExtras();
           String action  = extras.getString("action");


        } catch (Exception e){
           // System.out.println(e);
           // vname="";
        }

    }
    @Override
    public void onClick(View view) {
        System.out.println("Clicked:"+view.getId());
        switch (view.getId() /*to get clicked view id**/) {
            case R.id.device_filer_btn:
                System.out.println("Filter Save Clicked................");
                Intent intent = new Intent();
                intent.putExtra("reload", true);
                intent.putExtra("atoll_name",atollSpinner.getSelectedItem().toString());
//                intent.putExtra("atoll_code",Integer.toString(atollSpinner.getSelectedItemPosition()));
                TypedArray atollValueArray = getResources().obtainTypedArray(R.array.atoll_values);
                String atoll_code = atollValueArray.getString(atollSpinner.getSelectedItemPosition());
                intent.putExtra("atoll_code",atoll_code);
                setResult(RESULT_OK, intent);
                finish();

        }

    }


    public class getDeviceTypeTask extends AsyncTask<Void, Void, String> {
        getDeviceTypeTask() {
        }

        @Override
        protected String doInBackground(Void... arg0) {
            try{
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.getDeviceTypes(0,context);
            }
            catch(Exception ex) {
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
                            ArrayAdapter<Data_vtypes> adapter = new ArrayAdapter<Data_vtypes>(getBaseContext(), android.R.layout.simple_spinner_item, AppUtils.setVesselTypes(jObj, getBaseContext()));
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            vesselTypeSpinner.setAdapter(adapter);
                            //	deviceTypeSpinner.setSelection(((ArrayAdapter<String>)deviceTypeSpinner.getAdapter()).getPosition(jObj.getString("vtype")));
                            //	int spinnerPosition = adapter.getPosition(((ArrayAdapter<String>)mySpinner.getAdapter()).getPosition(myString));
                            // vesselTypeSpinner.setSelection(jObj.getInt("vtype")-1); //not the correct way
                            //	Data_vtypes country =  jObj.getInt("vtype");
                            vesselTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    //    Data_vtypes vtype = (Data_vtypes) parent.getSelectedItem();
                                    //    System.out.println("VTYPE: "+vtype);
                                    v_type = ((Data_vtypes) parent.getSelectedItem()).getId();
                                    //     Toast.makeText(context, "Country ID: "+country.getId()+",  Country Name : "+country.getName(), Toast.LENGTH_SHORT).show();
                                    //System.out.println("VTYPE=============================: "+vtype+"ID:"+v_type);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });                        } else {
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
}
