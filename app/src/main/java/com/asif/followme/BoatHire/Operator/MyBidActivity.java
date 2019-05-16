package com.asif.followme.BoatHire.Operator;

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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.asif.followme.BoatHire.DataBids;
import com.asif.followme.R;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.util.AppUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyBidActivity extends AppCompatActivity implements View.OnClickListener {
	private GetBidList mBidTask = null;
	private AsyncTask<Void, Void, String> SharedRemoveAsyncTask;
	SwipeRefreshLayout swipeLayout;
	Context context;
	ListView lv;
	public static List<DataBids> data = new ArrayList<DataBids>();
	private ChangeBidTask mChangeTask = null;
	public static int hire_type;

	public static Dialog shareDialog;
	public Button shareNow,cancelShare;
	public static String shared_user_id,shared_user_email;
	public EditText share_email;
	public CheckBox checkBox_cl,checkBox_hs,checkBox_ds,checkBox_as;
	public TextView shareLabel1;
	public static SharedPreferences settings;
	public static final String PREFS_NAME = "trk_settings";
	public static AlertDialog cancelAlert;
	private Dialog sDialog;
	private TextView bidName,hireDate,hireDest,hireDistance,hireInfo,hireFrom,hireMethod,hireDuration,hireBy;
	public static String selected_bid_id = "0";
    private LinearLayout errorLayout;
    private TextView errorLabel;
    private LinearLayout layoutBoatType, layoutDest, durationLayout,parentLayout,destinationLayout;
    public static int tenderStatus;
    public Button bidNow;
    public static String alertAction;

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_bid_activity);
		setTitle(HireMyFragment.selected_item_name);
		context = this;

		bidName = (TextView) findViewById(R.id.bid_name);
		hireDate = (TextView) findViewById(R.id.hire_date);
		hireDest = (TextView) findViewById(R.id.hire_dest);
		hireDistance = (TextView) findViewById(R.id.hire_distance);
		hireInfo = (TextView) findViewById(R.id.hire_info);
        hireFrom = (TextView) findViewById(R.id.hire_from);
        hireMethod = (TextView) findViewById(R.id.hire_method);
        hireBy = (TextView) findViewById(R.id.hire_by);
        hireDuration = (TextView) findViewById(R.id.hire_duration);
		settings = getSharedPreferences(PREFS_NAME, 0);
        errorLabel = (TextView) findViewById(R.id.error_label);
        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        parentLayout = (LinearLayout) findViewById(R.id.layout_parent);
        destinationLayout = (LinearLayout) findViewById(R.id.destination_layout);
        parentLayout.setVisibility(View.GONE);
        layoutBoatType = (LinearLayout) findViewById(R.id.layout_boat_type);
        durationLayout = (LinearLayout) findViewById(R.id.duration_layout);
        layoutDest = (LinearLayout) findViewById(R.id.layout_dest);
        bidNow = (Button) findViewById(R.id.bid_now);
        bidNow.setVisibility(View.GONE);
        bidNow.setOnClickListener(this);
		context=this;

		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		swipeLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);
		//populateSharedUsers();
			mBidTask = new GetBidList(HireMyFragment.selected_item_id);
			mBidTask.execute();
		//GetSharedUsers(MyList.shared_vessel_id);
		// createAlert();
//        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mBidTask = new GetBidList(HireMyFragment.selected_item_id);
                        mBidTask.execute();
                    }
                }
        );

		AlertDialog.Builder sharebuilder = new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setNegativeButton("Cancel", null)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						mChangeTask = new ChangeBidTask(alertAction,HireMyFragment.selected_item_id);
						mChangeTask.execute((Void) null);
					}

				});
		cancelAlert = sharebuilder.create();

	}


	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		if (requestCode == 1) {
			if(resultCode == RESULT_OK) {
				System.out.println("okkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
				mBidTask = new GetBidList(HireMyFragment.selected_item_id);
				mBidTask.execute();
				//String strEditText = data.getStringExtra("editTextValue");
			}
		}
	}

/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.public_map_menu,menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.bid_menu_my, menu);


		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_new_bid) {
			Intent intent = new Intent(context, NewBidForm.class);
			startActivityForResult(intent,1);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public final static boolean isValidEmail(CharSequence target) {
		return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	}
*/
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.bid_now:
				Intent intent = new Intent(context, NewBidForm.class);
				intent.putExtra("bid_id",0);
				startActivityForResult(intent, 1);
				break;
		}
	}


	public class GetBidList extends AsyncTask<Void, Void, String> {
		private final int hire_id;

		GetBidList(String id) {
			hire_id=Integer.parseInt(id);
		}
		@Override
		protected void onPreExecute() {
//			swipeLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);
			parentLayout.setVisibility(View.GONE);
			swipeLayout.setRefreshing(true);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			try{
				ContentParser parser = new ContentParser(getBaseContext());
				return parser.getMyBidList(context,hire_id);
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onCancelled() {
			swipeLayout.setRefreshing(false);
			parentLayout.setVisibility(View.VISIBLE);
//			System.out.println("Cancelled.....................");
//		    onMyAsyncTaskCompletedListener(null);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			swipeLayout.setRefreshing(false);
			parentLayout.setVisibility(View.VISIBLE);
			//System.out.println(result);
			if (!TextUtils.isEmpty(result)) {
				try {
					JSONObject jObj = new JSONObject(result);
					if (jObj.has("status")) {
						String statuss = jObj.getString("status");
						if (statuss.equalsIgnoreCase("ok")) {
                            JSONObject nObj = new JSONObject(jObj.getString("hireData"));
                            String hire_info = nObj.getString("remarks");
                            String hire_date = nObj.getString("hire_date");
                            hire_type = nObj.getInt("hire_type");
                            String hire_method  = nObj.getString("hire_method");
                            String hire_duration = nObj.getString("hire_duration");
                            String hire_distance = nObj.getString("distance");
                            String hire_by = nObj.getString("hire_by");
                            int bid_enabled = nObj.getInt("bid_enabled");
                            hireDistance.setText(hire_distance);
                            hireInfo.setText(hire_info);
                            hireDate.setText(hire_date);
                            hireMethod.setText(hire_method);
                            hireDuration.setText(hire_duration);
                            hireBy.setText(hire_by);
							if(bid_enabled==1){
                            	bidNow.setVisibility(View.VISIBLE);
							} else {
                            	bidNow.setVisibility(View.GONE);
							}
                            if(hire_type==1){
                                durationLayout.setVisibility(View.GONE);
                                destinationLayout.setVisibility(View.VISIBLE);
                            } else {
                                durationLayout.setVisibility(View.VISIBLE);
                                destinationLayout.setVisibility(View.GONE);
                            }
                            tenderStatus = nObj.getInt("tender_status");
                            data = AppUtils.drawMyBidList(jObj, context);
                            System.out.println("Data:"+data);
                            try {
                                JSONArray boatTypes = jObj.getJSONArray("boatTypes");
                                layoutBoatType.removeAllViews();
                                for (int i = 0; i < boatTypes.length(); i++) {
                                    JSONArray jsonArray = boatTypes.getJSONArray(i);
                                    System.out.println("The Array:"+jsonArray);
                                    addBoatType(jsonArray.getString(0));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONArray destIslands = jObj.getJSONArray("destIslands");
                                layoutDest.removeAllViews();
                                for (int i = 0; i < destIslands.length(); i++) {
                                    JSONArray jsonArray = destIslands.getJSONArray(i);
                                    System.out.println("The Array:"+jsonArray);
                                    if(i==0){
                                        hireFrom.setText(jsonArray.getString(0));
                                    } else {
                                        addDestinations(jsonArray.getString(0));
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //   lv=(ListView)findViewById(R.id.public_bid_fragment);
							 lv=(ListView)findViewById(R.id.bid_list);
                            lv.setAdapter(new MyBidAdapter(context, data));
                            if(data.size()==0){
                                errorLayout.setVisibility(View.VISIBLE);
                                errorLabel = (TextView) findViewById(R.id.error_label);
                                errorLayout = (LinearLayout) findViewById(R.id.error_layout);
                                errorLabel.setText(jObj.getString("error"));
                            } else {
                                errorLayout.setVisibility(View.GONE);
                            }

                         //   data = AppUtils.drawMyBidList(jObj, context);
						//	lv=(ListView)findViewById(R.id.bid_list);
						//	lv.setAdapter(new MyBidAdapter(context, data));

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

	public class ChangeBidTask extends AsyncTask<Void, Void, String> {
		String hire_id;
		String action;
		ChangeBidTask(String action, String hire_id) {
			this.action = action;
			this.hire_id = hire_id;
		}

		@Override
		protected String doInBackground(Void... arg0) {
			// TODO: attempt authentication against a network service.

			try {
				ContentParser parser = new ContentParser(context);
				return parser.getAJAX(context,action, hire_id,selected_bid_id, "");
			} catch (Exception ex) {
				//finish();
			}
			return null;
		}

		@Override
		protected void onPostExecute(final String result) {
			mChangeTask = null;
			//showProgress(false);
			if (!TextUtils.isEmpty(result)) {
				try {
					JSONObject jObj = new JSONObject(result);
					if (jObj.has("status")) {
						String statuss = jObj.getString("status");
						if (statuss.equalsIgnoreCase("ok")) {
							mBidTask = new GetBidList(HireMyFragment.selected_item_id);
							mBidTask.execute();
						} else {
							String errmsg = jObj.getString("error");
							AppUtils.showAlertDialog(context, "Error", errmsg);
//							Toast.makeText(getApplicationContext(), errmsg, Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(context, "Please check your internet connection 1", Toast.LENGTH_LONG).show();

					}
				} catch (JSONException e) {
					Toast.makeText(context, "Please check your internet connection 2", Toast.LENGTH_LONG).show();
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Toast.makeText(context, "Unable to reach server. Please check your internet connection 2", Toast.LENGTH_LONG).show();
				//AppUtils.showAlertDialog(context, "Timeout", "Unable to reach Server. Please try again!");
			}


		}

		@Override
		protected void onCancelled() {
			mChangeTask = null;
			//showProgress(false);
		}
	}
    public void addBoatType(String label) {
        View view = LayoutInflater.from(context).inflate(R.layout.tick_label,null);
        TextView labelBoatType = (TextView) view.findViewById(R.id.test_name);
//        final TextView labelBoatType = new TextView(context);
        labelBoatType.setText(label);
        layoutBoatType.addView(view);
    }
    public void addDestinations(String label) {
//        final CheckBox checkBox = new CheckBox(this);
        final TextView labelDest = new TextView(context);
        labelDest.setText(label);
//        labelBoatType.setBackgroundResource(R.drawable.border);
        //allViews.add(checkBox);
        layoutDest.addView(labelDest);
    }

}