package com.asif.followme;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.asif.followme.MyAccount.MyGroupsFragment;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.model.DataMy;
import com.asif.followme.util.AppUtils;
import com.asif.followme.util.SettingsPreferences;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GroupDeviceList extends AppCompatActivity implements AdapterView.OnItemClickListener {
	private GetGroupDevices sListTask = null;
	private ChangeGroup changeTask = null;
	SwipeRefreshLayout swipeLayout;
	Context context;
	ListView lv;
	public static List<DataMy> data = new ArrayList<DataMy>();

	public static Dialog shareDialog;
	public Button shareNow,cancelShare;
	public static String shared_user_id,shared_user_email;
	public EditText share_email;
	public CheckBox checkBox_cl,checkBox_hs,checkBox_ds,checkBox_as;
	public TextView shareLabel1;
//	public static SharedPreferences settings;
	public static final String PREFS_NAME = "trk_settings";
	public static AlertDialog fleetAlert;
	private Dialog sDialog;
	public static String fleet_device_id;



	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_device_list);
		setTitle(MyGroupsFragment.selected_item_name);
		context = this;


//		settings = getSharedPreferences(PREFS_NAME, 0);
		context=this;

		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(
				new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						sListTask = new GetGroupDevices();
						sListTask.execute();
					}
				}
		);
		sListTask = new GetGroupDevices();
		sListTask.execute();

		//populateSharedUsers();
		//GetSharedUsers(MyList.shared_vessel_id);
		// createAlert();

		AlertDialog.Builder sharebuilder = new AlertDialog.Builder(this)
				.setTitle("Remove Device")
				.setMessage("")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setNegativeButton("Cancel", null)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						String fleet_id=MyGroupsFragment.selected_item_id;
						//  String user_id=shared_user_id;
						//   PostSharedUsers(user_email,device_id,"0");
						//System.out.println("Remove Device: Fleet ID:"+fleet_device_id+" From Vessel: "+device_id);
						changeTask = new ChangeGroup("rem", fleet_id,fleet_device_id);
						changeTask.execute();
						//RemoveGroupDevice(MyActivity.selected_item_id,fleet_device_id);
						//Stop the activity
						// YourClass.this.finish();
					}

				});
		fleetAlert = sharebuilder.create();

	}


	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		if (requestCode == 1) {
			if(resultCode == RESULT_OK) {
				System.out.println("okkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
				sListTask = new GetGroupDevices();
				sListTask.execute();
				//String strEditText = data.getStringExtra("editTextValue");
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.public_map_menu,menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.group_device_menu, menu);


		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.new_group_device) {
			Intent intent = new Intent(context, GroupDeviceFormActivity.class);
			intent.putExtra("user_email","");
			intent.putExtra("user_id","0");
			startActivityForResult(intent,1);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public final static boolean isValidEmail(CharSequence target) {
		return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	}


	public class GetGroupDevices extends AsyncTask<Void, Void, String> {
		private final int group_id;

		GetGroupDevices() {
			group_id = Integer.parseInt(SettingsPreferences.getSelectedItemID(context));
			System.out.println("Group ID:"+group_id);
		}
		@Override
		protected void onPreExecute() {
			swipeLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);
			swipeLayout.setRefreshing(true);

			super.onPreExecute();

		}

		@Override
		protected String doInBackground(Void... arg0) {
			try{
				ContentParser parser = new ContentParser(getBaseContext());
				return parser.getGroupDevices(context,group_id);
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onCancelled() {
			swipeLayout.setRefreshing(false);
//			System.out.println("Cancelled.....................");
//		    onMyAsyncTaskCompletedListener(null);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			swipeLayout.setRefreshing(false);
			//System.out.println(result);
			if (!TextUtils.isEmpty(result)) {
				try {
					JSONObject jObj = new JSONObject(result);
					if (jObj.has("status")) {
						String statuss = jObj.getString("status");
						if (statuss.equalsIgnoreCase("ok")) {
							data = AppUtils.drawFleetDeviceList(jObj, context);
							lv=(ListView)findViewById(R.id.group_device_list);
							lv.setAdapter(new fleetAdapter(context, data));

						} else {
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


	public class ChangeGroup extends AsyncTask<Void, Void, String> {
		private final int fleet_id;
		private final int device_id;
		private final String action;

		ChangeGroup(String act, String fid, String did) {
			this.action = act;
			this.fleet_id=Integer.parseInt(fid);
			this.device_id = Integer.parseInt(did);
		}
		@Override
		protected void onPreExecute() {
			//swipeLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);
			//swipeLayout.setRefreshing(true);
			sDialog= ProgressDialog.show(GroupDeviceList.this, null, "Removing Device, Please wait...");
			sDialog.show();

			super.onPreExecute();

		}

		@Override
		protected String doInBackground(Void... arg0) {
			try{
				ContentParser parser = new ContentParser(getBaseContext());
				return parser.changeFleet(context,action,fleet_id, device_id);
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onCancelled() {
			if(sDialog.isShowing()){
				sDialog.dismiss();
			}
//			System.out.println("Cancelled.....................");
//		    onMyAsyncTaskCompletedListener(null);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
//			swipeLayout.setRefreshing(false);
			if(sDialog.isShowing()){
				sDialog.dismiss();
			}
			//System.out.println(result);
			if (!TextUtils.isEmpty(result)) {
				try {
					JSONObject jObj = new JSONObject(result);
					if (jObj.has("status")) {
						String statuss = jObj.getString("status");
						if (statuss.equalsIgnoreCase("ok")) {
							data = AppUtils.drawFleetDeviceList(jObj, context);
							lv=(ListView)findViewById(R.id.group_device_list);
							lv.setAdapter(new fleetAdapter(context, data));

						} else {
							Toast.makeText(getApplicationContext(), jObj.getString("error"), Toast.LENGTH_LONG).show();
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


}