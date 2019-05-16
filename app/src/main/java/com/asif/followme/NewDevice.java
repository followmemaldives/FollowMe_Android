package com.asif.followme;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.model.Data_vtypes;
import com.asif.followme.util.AppUtils;
import com.asif.followme.util.SettingsPreferences;
import org.json.JSONException;
import org.json.JSONObject;

public class NewDevice extends AppCompatActivity implements View.OnClickListener, LocationListener {

    Context context;
    private EditText device_name_input, device_imei_input, contact_input, device_sim_input, fuel_input;
    private Spinner device_brand_input, vesselTypeSpinner;
    private CheckBox device_public_checkbox, charter_checkbox;
    private LinearLayout hide_mobile_layout;
    Button btnSaveInfo;
    private getDeviceTypeTask mTypeTask = null;
    public int v_type;
    public String v_imei, v_name, v_public, v_contact, v_fuel, v_sim, v_brand;
    private AsyncTask<Void, Void, String> SettingsAsyncTask;
    private Dialog dialog;
    public String latitude;
    public String longitude;
    public String device_id = "";
    private Location lastLocation;
    private saveInfoTask mSaveInfoTask = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_device);

        context = this;
        device_brand_input = (Spinner) findViewById(R.id.device_brand_input);
        device_imei_input = (EditText) findViewById(R.id.device_imei_input);
        device_name_input = (EditText) findViewById(R.id.device_name_input);
        contact_input = (EditText) findViewById(R.id.contact_input);
        device_sim_input = (EditText) findViewById(R.id.device_sim_input);
        fuel_input = (EditText) findViewById(R.id.fuel_input);
        vesselTypeSpinner = (Spinner) findViewById(R.id.vessel_type_spinner);
        device_public_checkbox = (CheckBox)findViewById(R.id.device_public_checkbox);
        charter_checkbox = (CheckBox)findViewById(R.id.charter_checkbox);
        hide_mobile_layout = (LinearLayout) findViewById(R.id.hide_mobile_Layout);


        fuel_input.setText("3");
        //device_public_checkbox.setChecked(true);


        btnSaveInfo = (Button) findViewById(R.id.device_save_btn);
        device_public_checkbox = (CheckBox) findViewById(R.id.device_public_checkbox);
        btnSaveInfo.setOnClickListener(this);

        mTypeTask = new getDeviceTypeTask();
        mTypeTask.execute((Void) null);

        device_brand_input.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {    //mobile
                    String imei = SettingsPreferences.getIMEI(context);
                    device_imei_input.setText(imei);
                    device_imei_input.setEnabled(false);
                    device_imei_input.setBackgroundResource(R.drawable.roundborder_disable);
//                    vesselTypeSpinner.setSelection(5);
//                    vesselTypeSpinner.setEnabled(false);
                    device_public_checkbox.setEnabled(false);
                    device_public_checkbox.setChecked(false);
                    charter_checkbox.setEnabled(false);
                    //fuel_input.setEnabled(false);
                    hide_mobile_layout.setVisibility(View.GONE);

                } else {
                    device_imei_input.setText("");
                    device_imei_input.setEnabled(true);
                    device_imei_input.setBackgroundResource(R.drawable.roundborder);
//                    vesselTypeSpinner.setEnabled(true);
                    device_public_checkbox.setEnabled(true);
                    device_public_checkbox.setChecked(true);
                    charter_checkbox.setEnabled(true);
                    //fuel_input.setEnabled(true);
                    hide_mobile_layout.setVisibility(View.VISIBLE);
                }
                // TODO Auto-generated method stub

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.device_save_btn:
                saveDevice();
                break;

        }
    }

    public void saveDevice() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        v_brand = Integer.toString(device_brand_input.getSelectedItemPosition());
        v_imei = device_imei_input.getText().toString();
        v_name = device_name_input.getText().toString();
        v_contact = contact_input.getText().toString();
        v_sim = device_sim_input.getText().toString();
        v_fuel = fuel_input.getText().toString();
        if (v_imei.length() == 0) {
            device_imei_input.setError("Please Enter Device IMEI Number");
            device_imei_input.requestFocus();
            return;
        }
        if (v_name.length() == 0) {
            device_name_input.setError("Please Enter Vessel/Vehicle Name");
            device_name_input.requestFocus();
            return;
        }
        if (v_contact.length() == 0) {
            contact_input.setError("Please Enter Your Contact Number");
            contact_input.requestFocus();
            return;
        }
        if (v_sim.length() == 0) {
            device_sim_input.setError("Please Enter Device Phone Number");
            device_sim_input.requestFocus();
            return;
        }
        if (v_fuel.length() == 0) {
            fuel_input.setError("Please Enter Estimated Fuel Consumption Per Mile");
            fuel_input.requestFocus();
            return;
        }

        mSaveInfoTask = new saveInfoTask();
        mSaveInfoTask.execute((Void) null);

    }

/*    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        System.out.println("Already Permission Granted..........................................");
        return true;
    }
*/
    public class saveInfoTask extends AsyncTask<Void, Void, String> {
        String charter;
            @Override
            protected void onPreExecute() {
                latitude = "0";
                longitude ="0";
                //if(alertSoundCheckBox.isChecked() || alertVibrateCheckBox.isChecked()){
                if (device_public_checkbox.isChecked()) {
                    v_public = "1";
                } else {
                    v_public = "0";
                }
                if (charter_checkbox.isChecked()) {
                    charter = "1";
                } else {
                    charter = "0";
                }
                dialog = ProgressDialog.show(context, "Please wait", "Saving Device...");
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(Void... arg0) {
                // TODO Auto-generated method stub
                try {
                    ContentParser parser = new ContentParser(getBaseContext());
                    return parser.SaveNewDevice(context, device_id, "new", v_brand, v_imei, v_name, v_public, v_contact, v_sim, v_fuel, latitude, longitude, charter);
                } catch (Exception ex) {
                    //finish();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                System.out.println(result);
                if (!TextUtils.isEmpty(result)) {
                    try {
                        JSONObject jObj = new JSONObject(result);
                        if (jObj.has("status")) {
                            if (jObj.getString("status").equalsIgnoreCase("ok")) {
                                if (jObj.getString("imei").equalsIgnoreCase(SettingsPreferences.getIMEI(context))) {
 //                                   SettingsPreferences.setIsTracker(context, true);
                                    String my_tracker_id= jObj.getString("my_tracker");
                                    SettingsPreferences.setMyTracker(context,my_tracker_id);
                                    Intent intent = getIntent();
                                    intent.putExtra("action", "self_track");
                                    setResult(RESULT_OK,intent );
                                   // MyBoatsFragment.switchLive.setVisibility(View.VISIBLE);

                                }
                                //AppUtils.saveMyVessel(jObj, context);
                                //	Get_list.populateVessels(context);
                                //	SettingsPreferences.setAlertMovement(context, alertMovementCheckBox.isChecked());
                                //	SettingsPreferences.setAlertSound(context, alertSoundCheckBox.isChecked());
                                //	SettingsPreferences.setAlertVibrate(context, alertVibrateCheckBox.isChecked());
                                //	loginPrefsEditor.putBoolean("alertSound", alertSoundCheckBox.isChecked());
                                //	loginPrefsEditor.putBoolean("alertVibrate", alertVibrateCheckBox.isChecked());
                                //	loginPrefsEditor.commit();
                                //Toast.makeText(getApplicationContext(), "New Device Saved!!", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                AppUtils.showAlertDialog(context, "Error", jObj.getString("error"));
                                //Toast.makeText(getApplicationContext(), errmsg, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

                }
            }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public class getDeviceTypeTask extends AsyncTask<Void, Void, String> {
        getDeviceTypeTask() {
        }

        @Override
        protected String doInBackground(Void... arg0) {
            // TODO: attempt authentication against a network service.

            try {
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.getDeviceTypes(0,context);
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
                            ArrayAdapter<Data_vtypes> adapter = new ArrayAdapter<Data_vtypes>(getBaseContext(), android.R.layout.simple_spinner_item, AppUtils.setVesselTypes(jObj, getBaseContext()));
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            vesselTypeSpinner.setAdapter(adapter);


                            for (int position=0; position < adapter.getCount(); position++) {
                                if (adapter.getItem(position).getId() == 6) {   //6-person
                                    v_type = adapter.getItem(position).getId();
                                    vesselTypeSpinner.setSelection(adapter.getItem(position).getIndex());
                                }
                            }                            //	deviceTypeSpinner.setSelection(((ArrayAdapter<String>)deviceTypeSpinner.getAdapter()).getPosition(jObj.getString("vtype")));
                            //	int spinnerPosition = adapter.getPosition(((ArrayAdapter<String>)mySpinner.getAdapter()).getPosition(myString));
                            // vesselTypeSpinner.setSelection(jObj.getInt("vtype")-1); //not the correct way
                            //	Data_vtypes country =  jObj.getInt("vtype");
                            vesselTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

}

