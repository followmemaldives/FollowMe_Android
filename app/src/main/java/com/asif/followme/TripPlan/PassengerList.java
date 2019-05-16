package com.asif.followme.TripPlan;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.widget.*;
import com.asif.followme.R;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.util.AppUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PassengerList extends AppCompatActivity implements AdapterView.OnItemClickListener {
	private GetPaxList getPaxList = null;
	SwipeRefreshLayout swipeLayout;
	Context context;
	ListView lv;
	public static List<DataPax> data = new ArrayList<DataPax>();
//	public static List<DataLegInfo> data2 = new ArrayList<DataLegInfo>();

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
	private TextView notfoundTextView;
	public static int pax_id =0;
	public int trip_id=0;
	private int bstatus =0;
	private int bfrom = 0;
	private int bto =0;



	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trip_passenger_activity);
		setTitle(TripPlanFragment.selected_item_name);
		context = this;


		settings = getSharedPreferences(PREFS_NAME, 0);
		context=this;

		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		notfoundTextView = (TextView) findViewById(R.id.notfound_text);
		notfoundTextView.setVisibility(View.GONE);
		swipeLayout.setOnRefreshListener(
				new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						getPaxList = new GetPaxList(trip_id,bfrom,bto,bstatus);
						getPaxList.execute();
					}
				}
		);
		//populateSharedUsers();
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

//						String trip_id=TripPlanFragment.selected_item_id;
						//  String user_id=shared_user_id;
						//   PostSharedUsers(user_email,device_id,"0");
//						System.out.println("Remove user: "+shared_user_id+" From Vessel: "+trip_id);
//						RemoveSharedUsers(trip_id,shared_user_id);
						//Stop the activity
						// YourClass.this.finish();
					}

				});
		shareAlert = sharebuilder.create();

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		System.out.println("OnStart...............................");
		super.onStart();
		setTitle(TripPlanFragment.selected_item_name);
		try{
			Bundle extras = getIntent().getExtras();
			trip_id=extras.getInt("trip_id");


		} catch (Exception e){

		}
		getPaxList = new GetPaxList(trip_id,bfrom,bto,bstatus);
		getPaxList.execute();

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("onActivityResult: RequestCode: "+requestCode+",ResultCode: "+resultCode+" xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		if (requestCode == 1) {
			if(resultCode == RESULT_OK) {
				System.out.println("okkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
				getPaxList = new GetPaxList(trip_id,bfrom,bto,bstatus);
				getPaxList.execute();
				//String strEditText = data.getStringExtra("editTextValue");
			}
		}
		if (requestCode == 2) {
			if(resultCode == RESULT_OK) {
				System.out.println("Return from Pax Filter Form");
				try{
					Bundle extras = data.getExtras();
					bfrom=extras.getInt("bfrom");
					bto=extras.getInt("bto");
					bstatus=extras.getInt("bstatus");
				} catch (Exception e){
					e.printStackTrace();
				}
				getPaxList = new GetPaxList(trip_id,bfrom,bto,bstatus);
				getPaxList.execute();
				//String strEditText = data.getStringExtra("editTextValue");
			}
		}
		if (requestCode == 5) {	//Retuned from Menu Selection, Edit,... etc
			if(resultCode == RESULT_OK) {
				System.out.println("Return from Pax Filter Form");
				try{
					Bundle extras = data.getExtras();
					String sender = extras.getString("sender");
				} catch (Exception e){
					e.printStackTrace();
				}

				getPaxList = new GetPaxList(trip_id,bfrom,bto,bstatus);
				getPaxList.execute();
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
		inflater.inflate(R.menu.trip_pax_menu, menu);


		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_new_pax) {
			Intent intent = new Intent(context, NewPaxForm.class);
			intent.putExtra("pax_id",0);
			startActivityForResult(intent,1);
			return true;
		}
        if (id == R.id.action_filter_clear) {
            getPaxList = new GetPaxList(trip_id,0,0,0);
            getPaxList.execute();
            return true;
        }
		if (id == R.id.action_filter_dest) {
			Intent intent = new Intent(context, PaxFilterForm.class);
			startActivityForResult(intent,2);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public final static boolean isValidEmail(CharSequence target) {
		return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	}

	public void selectMenuItem(Context con, int id) {
		System.out.println("selectMenuItem Called from PublicHireFragment................................");
		pax_id = id;
		Intent intent = new Intent(con,TripPaxMenuActivity.class);
		//intent.putExtra("vname",selected_pax_name);
		intent.putExtra("pax_id",pax_id);
		startActivityForResult(intent,5);
	}


	public class GetPaxList extends AsyncTask<Void, Void, String> {
		private final int trip_id;
		private int bfrom;
		private int bto;
		private int bstatus;

		GetPaxList(int trip_id,int bfrom, int bto, int bstatus) {
			this.trip_id=trip_id;
			this.bfrom=bfrom;
			this.bto=bto;
			this.bstatus=bstatus;
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
				return parser.getPaxList(context,trip_id,bfrom,bto,bstatus);
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
							data = AppUtils.drawPaxList(jObj, context);
							//data2 = AppUtils.drawLegInfo(jObj,context);
							if(data.size()==0){
								notfoundTextView.setVisibility(View.VISIBLE);
								notfoundTextView.setText(jObj.getString("error"));
							} else {
								notfoundTextView.setVisibility(View.GONE);
							}
							lv=(ListView)findViewById(R.id.pax_list);
							//lv2 = (ListView) findViewById(R.id.leg_list);
							//lv2.setAdapter(new legInfoAdapter(context,data2,PassengerList.this));
							lv.setAdapter(new paxAdapter(context, data, PassengerList.this));

						} else {
							notfoundTextView.setText(jObj.getString("error"));
							notfoundTextView.setVisibility(View.VISIBLE);
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


}