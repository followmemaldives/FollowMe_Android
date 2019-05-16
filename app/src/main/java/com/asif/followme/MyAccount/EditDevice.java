package com.asif.followme.MyAccount;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.*;
import com.asif.followme.R;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.model.Data_vtypes;
import com.asif.followme.util.AppUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class EditDevice extends AppCompatActivity implements OnClickListener {

	Button btnSaveInfo;

	boolean isInternetPresent = false;
	//ConnectionDetector cd;
	public Activity activity;
	String statuss, errmsg;
	ProgressDialog Dialog;
	private CheckBox device_public_checkbox, charter_checkbox;
	private EditText device_name_input,device_imei_input,contact_input,device_sim_input,fuel_input;
	private Spinner vesselTypeSpinner;
	public String alert;
	private LinearLayout device_fuel_layout,device_sim_layout;
	private SharedPreferences.Editor loginPrefsEditor;
	public static String PREFRENCES_NAME="loginPrefs";
	String statuspass = "ok";                 
	String statusfailed = "failed"; 
	String text = "";
	private static final String TAG_STATUS = "status";
	private static final String TAG_ERROR_MSG = "error";
    public String loginType = "";
    Toast toast;
    private Context context;
    public Boolean alertMovement, alertSound, alertVibrate;
    public String device_id="";
    public String v_imei,v_name,v_public,v_contact,v_fuel;
    public int v_type;
    public String[] devices;
//	public static List<Data_vtypes> vtypes = new ArrayList<Data_vtypes>();
	private ArrayAdapter<Data_vtypes> adapter;
	private getEditInfoTask mEditInfoTask = null;
	private saveInfoTask mSaveInfoTask = null;

	//	JSONArray status = null;
	//	JSONArray error_msg = null;
	//public static DefaultHttpClient httpClient;
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_layout);
		setTitle(MyBoatsFragment.selected_item_name);
		context=this;
		//cd = new ConnectionDetector(this);
		//isInternetPresent = cd.isConnectingToInternet();
		//if (isInternetPresent){
//			getDeviceInfo();
		//}
		device_name_input =(EditText) findViewById (R.id.device_name_input);
		device_imei_input =(EditText) findViewById (R.id.device_imei_input);
		contact_input =(EditText) findViewById (R.id.contact_input);
		device_sim_input = (EditText) findViewById(R.id.device_sim_input);
		fuel_input =(EditText) findViewById (R.id.fuel_input);
		vesselTypeSpinner = (Spinner) findViewById(R.id.vessel_type_spinner);

		btnSaveInfo = (Button) findViewById(R.id.device_save_btn);
		device_public_checkbox = (CheckBox)findViewById(R.id.device_public_checkbox);
		charter_checkbox = (CheckBox)findViewById(R.id.charter_checkbox);
		device_fuel_layout = (LinearLayout) findViewById(R.id.device_fuel_Layout);
		device_sim_layout = (LinearLayout) findViewById(R.id.device_sim_Layout);

		btnSaveInfo.setOnClickListener(this);
		getWindow().setSoftInputMode(
			      WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


		device_id=MyBoatsFragment.selected_item_id;
		mEditInfoTask = new getEditInfoTask(device_id);
		mEditInfoTask.execute((Void) null);

	}
	

	public void onClick(View v) {
		// TODO Auto-generated method stub
		System.out.println(v.getId());
		switch (v.getId()) 
		{
		case R.id.device_save_btn:
			mSaveInfoTask = new saveInfoTask();
			mSaveInfoTask.execute((Void) null);
			break;			
		
		}	
	}
	



	public class getEditInfoTask extends AsyncTask<Void, Void, String> {
		String device_id;
		getEditInfoTask(String device_id) {
			this.device_id = device_id;
		}

		@Override
		protected void onPreExecute() {
			//if(alertSoundCheckBox.isChecked() || alertVibrateCheckBox.isChecked()){
			Dialog=ProgressDialog.show(context, "Please wait", "Reading Preferences...");
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			try{
				ContentParser parser = new ContentParser(getBaseContext());
				return parser.getDeviceEditInfo(context,device_id);
			}
			catch(Exception ex)
			{
				//finish();
//				System.out.println("err??????????????????????");
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(Dialog.isShowing())
			{
				Dialog.dismiss();
			}
			if (!TextUtils.isEmpty(result)) {
//				System.out.println(result);
				try {
					JSONObject jObj = new JSONObject(result);
					if (jObj.has("status")) {
						if (jObj.getString("status").equalsIgnoreCase("ok")) {
							device_name_input.setText(jObj.getString("name"));
							device_imei_input.setText(jObj.getString("imei"));
							contact_input.setText(jObj.getString("contact"));
							fuel_input.setText(jObj.getString("fuel"));
							device_sim_input.setText(jObj.getString("sim"));
							if(jObj.getInt("model")==0){// Not listed in imei table, its a mobile phone
								device_public_checkbox.setChecked(false);
								device_public_checkbox.setEnabled(false);
								charter_checkbox.setEnabled(false);
								//fuel_input.setEnabled(false);
								device_fuel_layout.setVisibility(View.GONE);
								device_sim_layout.setVisibility(View.GONE);
							} else {
								device_public_checkbox.setEnabled(true);
								charter_checkbox.setEnabled(true);
								//fuel_input.setEnabled(true);
								device_fuel_layout.setVisibility(View.VISIBLE);
								device_sim_layout.setVisibility(View.VISIBLE);
								if (jObj.getString("public").equalsIgnoreCase("1")) {
									device_public_checkbox.setChecked(true);
								} else {
									device_public_checkbox.setChecked(false);
								}
								if(jObj.getString("charter").equalsIgnoreCase("1")){
									charter_checkbox.setChecked(true);
								} else {
									charter_checkbox.setChecked(false);
								}
							}
							//devices=new String[]{"Your Mobile Phone","Followme Tracking Device"};
							 //  ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, devices); //selected item will look like a spinner set from XML
							//   spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							//   deviceTypeSpinner.setAdapter(spinnerArrayAdapter);

							ArrayList<Data_vtypes> dd = AppUtils.setVesselTypes(jObj, getBaseContext());
					        adapter = new ArrayAdapter<Data_vtypes>(getBaseContext(), android.R.layout.simple_spinner_item, dd);
					  //      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							vesselTypeSpinner.setAdapter(adapter);


							for (int position=0; position < adapter.getCount(); position++) {
//								System.out.println("Find:"+jObj.getInt("vtype")+",Position:"+position+", Index:"+adapter.getItem(position).getIndex()+",VType:"+adapter.getItem(position).getId()+",Type Name:"+adapter.getItem(position).getName());
								if (adapter.getItem(position).getId() == jObj.getInt("vtype")) {
//									System.out.println("Found");
									v_type = adapter.getItem(position).getId();
									vesselTypeSpinner.setSelection(adapter.getItem(position).getIndex());
								}
							}

							vesselTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					            @Override
					            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

					                 Data_vtypes vtypes = (Data_vtypes) parent.getSelectedItem();
					                 v_type = vtypes.getId();
					            }


					            @Override
					            public void onNothingSelected(AdapterView<?> parent) {    
					            }
					        });
/*							if(jObj.getString("model").equalsIgnoreCase("2")){
								vesselTypeSpinner.setEnabled(false);
							} else {
								vesselTypeSpinner.setEnabled(true);
							}
*/
						} else {
							errmsg = jObj.getString(TAG_ERROR_MSG);
							//AppUtils.showAlertDialog(context, "Error", errmsg);
							AlertDialog.Builder builder = new AlertDialog.Builder(context);
							builder.setTitle("Error")
									.setMessage(errmsg)
									.setCancelable(false)
									.setPositiveButton("OK", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											dialog.cancel();
											finish();
										}
									});
							AlertDialog alert = builder.create();
							alert.show();
							//Toast.makeText(getApplicationContext(), errmsg, Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
						
					}
				} catch (JSONException e) {
//					Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}


	public class saveInfoTask extends AsyncTask<Void, Void, String> {
		String v_charter;
		@Override
		protected void onPreExecute() {
			//if(alertSoundCheckBox.isChecked() || alertVibrateCheckBox.isChecked()){
			if(device_public_checkbox.isChecked()){
				v_public="1";
			} else {
				v_public="0";
			}
			if(charter_checkbox.isChecked()){
				v_charter="1";
			} else {
				v_charter="0";
			}
			v_name=device_name_input.getText().toString();
			v_imei=device_imei_input.getText().toString();
			v_contact = contact_input.getText().toString();
			v_fuel = fuel_input.getText().toString();
			int vpos = vesselTypeSpinner.getSelectedItemPosition();
			//int vpos = adapter.getPosition(vesselTypeSpinner.getSelectedItem().toString());
			//v_type = adapter.getItem(vpos).toString();
			System.out.println("Selected Vtype:"+v_type);
			//v_type = vesselTypeSpinner.getSelectedItemPosition();
			Dialog=ProgressDialog.show(context, "Please wait", "Saving Preferences...");
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			try{
				ContentParser parser = new ContentParser(getBaseContext());
				return parser.saveDeviceInfo(context,device_id,v_type,v_imei,v_name,v_public,v_contact,v_fuel,v_charter);
			}
			catch(Exception ex)
			{
				//finish();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(Dialog.isShowing())
			{
				Dialog.dismiss();
			}
			if (!TextUtils.isEmpty(result)) {
				try {
					JSONObject jObj = new JSONObject(result);
					if (jObj.has("status")) {
						statuss = jObj.getString(TAG_STATUS);
						if (statuss.equalsIgnoreCase(statuspass)) {
							
							//AppUtils.saveMyVessel(jObj, context);
							Toast.makeText(getApplicationContext(), "Device Saved!!", Toast.LENGTH_LONG).show();
							finish();
						} else {
							errmsg = jObj.getString(TAG_ERROR_MSG);
							AppUtils.showAlertDialog(context, "Error", errmsg);
//							Toast.makeText(getApplicationContext(), errmsg, Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();						
					}
				} catch (JSONException e) {
					Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}



	
	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(Dialog!=null && Dialog.isShowing())
		{
			Dialog.dismiss();
			Dialog=null;
		}
	}
	

}