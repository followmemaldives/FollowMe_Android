package com.asif.followme.MyAccount;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.asif.followme.R;
import com.asif.followme.ShareFormActivity;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.Adapters.shareAdapter;
import com.asif.followme.util.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShareList extends AppCompatActivity implements AdapterView.OnItemClickListener {
	private GetSharedUsers sListTask = null;
	private AsyncTask<Void, Void, String> SharedRemoveAsyncTask;
	SwipeRefreshLayout swipeLayout;
	Context context;
	ListView lv;
	public static List<DataMyShare> data = new ArrayList<DataMyShare>();

	public static Dialog shareDialog;
	public Button shareNow,cancelShare;
	public static String shared_user_id,shared_user_email;
	public EditText share_email;
	public CheckBox checkBox_cl,checkBox_hs,checkBox_ds,checkBox_as;
	public TextView shareLabel1;
	public static SharedPreferences settings;
	public static final String PREFS_NAME = "trk_settings";
	public static AlertDialog shareAlert;
	private Dialog sDialog;



	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shared_list);
		setTitle(MyBoatsFragment.selected_item_name);
		context = this;


		settings = getSharedPreferences(PREFS_NAME, 0);
		context=this;

		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(
				new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						sListTask = new GetSharedUsers(MyBoatsFragment.selected_item_id);
						sListTask.execute();
					}
				}
		);
		//populateSharedUsers();
			sListTask = new GetSharedUsers(MyBoatsFragment.selected_item_id);
			sListTask.execute();
		//GetSharedUsers(MyList.shared_vessel_id);
		// createAlert();

		AlertDialog.Builder sharebuilder = new AlertDialog.Builder(this)
				.setTitle("Remove User")
				.setMessage("")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setNegativeButton("Cancel", null)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						String device_id=MyBoatsFragment.selected_item_id;
						//  String user_id=shared_user_id;
						//   PostSharedUsers(user_email,device_id,"0");
						System.out.println("Remove user: "+shared_user_id+" From Vessel: "+device_id);
						RemoveSharedUsers(device_id,shared_user_id);
						//Stop the activity
						// YourClass.this.finish();
					}

				});
		shareAlert = sharebuilder.create();

	}


	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		if (requestCode == 1) {
			if(resultCode == RESULT_OK) {
				System.out.println("okkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
				sListTask = new GetSharedUsers(MyBoatsFragment.selected_item_id);
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
		inflater.inflate(R.menu.share_menu, menu);


		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_new_share) {
			Intent intent = new Intent(context, ShareFormActivity.class);
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


	public class GetSharedUsers extends AsyncTask<Void, Void, String> {
		private final int device_id;

		GetSharedUsers(String id) {
			device_id=Integer.parseInt(id);
		}
		@Override
		protected void onPreExecute() {
			swipeLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);
			swipeLayout.setRefreshing(true);

			super.onPreExecute();

		}

		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			try{
				ContentParser parser = new ContentParser(getBaseContext());
				return parser.getSharedUsers(context,device_id);
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
							data = AppUtils.drawSharedUserList(jObj, context);
							lv=(ListView)findViewById(R.id.shared_list);
							lv.setAdapter(new shareAdapter(context, data));

						} else {
							//errmsg = jObj.getString(TAG_ERROR_MSG);
							//AppUtils.showAlertDialog(context, "Error", errmsg);
							AlertDialog.Builder builder = new AlertDialog.Builder(context);
							builder.setTitle("Error")
									.setMessage(jObj.getString("error"))
									.setCancelable(false)
									.setPositiveButton("OK", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											dialog.cancel();
											finish();
										}
									});
							AlertDialog alert = builder.create();
							alert.show();


						}
					} else {
						Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
						//	System.out.println("Result"+result);

					}
				} catch (JSONException e) {
					//loading.setText("Unable to reach Server...");
					Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				//Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

			}
		}
	}

	private void RemoveSharedUsers(final String device_id, final String shared_user) {
		SharedRemoveAsyncTask = new AsyncTask<Void, Void, String>()    {
			@Override
			protected void onPreExecute() {
				sDialog= ProgressDialog.show(ShareList.this, null, "Removing User, Please wait...");
				sDialog.show();
				super.onPreExecute();

			}

			@Override
			protected String doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				try{
					ContentParser parser = new ContentParser(getBaseContext());
					return parser.RemoveSharedUsers(context,device_id,shared_user);
					//GetText();
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
				if(sDialog.isShowing()){
					sDialog.dismiss();
				}
				System.out.println(result);
				//		SettingsPreferences.setSelectedAccount(context, id2);
				if (!TextUtils.isEmpty(result)) {
					try {
						JSONObject jObj = new JSONObject(result);
						if (jObj.has("status")) {
							if (jObj.getString("status").equalsIgnoreCase("ok")) {
								//	errmsg = jObj.getString(TAG_ERROR_MSG);
								data = AppUtils.drawSharedUserList(jObj, context);
								//populateSharedUsers();
								lv=(ListView)findViewById(R.id.shared_list);
								lv.setAdapter(new shareAdapter(context, data));

							} else {
								AppUtils.showAlertDialog(context, "Error", jObj.getString("error"));
//							showAlert(context,"Error",jObj.getString("error"));
								//alert.show();
								//Toast.makeText(getApplicationContext(), jObj.getString("error"), Toast.LENGTH_LONG).show();
							}


						} else {
							Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

						}
					} catch (JSONException e) {
						//loading.setText("Unable to reach Server...");
						Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

				}
			}
		};
		SharedRemoveAsyncTask.execute(null, null, null);
	}


}