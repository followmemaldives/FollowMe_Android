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
import com.asif.followme.model.NameValuePairs;
import com.asif.followme.util.AppUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TemplateList extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
	private GetTemplateList getTemplateList = null;
	private RouteDeleteTask routeDeleteTask = null;
	SwipeRefreshLayout swipeLayout;
	Context context;
	ListView lv;
	public static List<DataRouteInfo> data = new ArrayList<DataRouteInfo>();
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
	public int selected_client_id=0;
	public int route_id = 0;


    private LinearLayout errorLayout;
    private TextView errorTextView;
    private LinearLayout clientLayout;
    private Spinner clientSpinner;
    private Button clientFilterBtn;


	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trip_template_activity);
		setTitle(TripPlanFragment.selected_item_name);
		context = this;


		settings = getSharedPreferences(PREFS_NAME, 0);
		context=this;
        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        errorTextView = (TextView) findViewById(R.id.error_text_view);
        clientLayout = (LinearLayout) findViewById(R.id.client_layout);
        clientSpinner = (Spinner) findViewById(R.id.client_spinner);
        clientFilterBtn = (Button) findViewById(R.id.client_filter_btn);
        clientFilterBtn.setOnClickListener(this);

		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(
				new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						getTemplateList = new GetTemplateList(selected_client_id);
						getTemplateList.execute();
					}
				}
		);


	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		System.out.println("OnStart...............................");
		super.onStart();
		setTitle("Route Templates");

		getTemplateList = new GetTemplateList(0);
		getTemplateList.execute();

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("onActivityResult: RequestCode: "+requestCode+",ResultCode: "+resultCode+" xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		if (requestCode == 1) {
			if(resultCode == RESULT_OK) {
				System.out.println("okkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
				getTemplateList = new GetTemplateList(selected_client_id);
				getTemplateList.execute();
				//String strEditText = data.getStringExtra("editTextValue");
			}
		}

		if (requestCode == 5) {	//Retuned from Menu Selection, Edit,... etc
			if(resultCode == RESULT_OK) {
				System.out.println("Return from Option Menu");
				try{
					Bundle extras = data.getExtras();
					String action = extras.getString("action");
					if(action.equalsIgnoreCase("del")){
						AppUtils.showConfirmDialog(context,"Delete Route","This will Delete selected Route.",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										routeDeleteTask = new RouteDeleteTask("del",route_id,selected_client_id);
										routeDeleteTask.execute();
									}
								});

						System.out.println("A");
						return;
					}
				} catch (Exception e){
					e.printStackTrace();
				}

			}
			System.out.println("B");
			getTemplateList = new GetTemplateList(selected_client_id);
			getTemplateList.execute();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.trip_template_menu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_new_template) {
			Intent intent = new Intent(context, NewTemplateForm.class);
			intent.putExtra("route_id",0);
			intent.putExtra("client_id", selected_client_id);
			intent.putExtra("route_status", "Active");
			startActivityForResult(intent,1);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public final static boolean isValidEmail(CharSequence target) {
		return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	}

	public void selectMenuItem(Context con, int id, String status) {
		System.out.println("selectMenuItem Called : Status: "+status+" ...............................");
		route_id = id;
		Intent intent = new Intent(con,TripRouteMenuActivity.class);
		intent.putExtra("route_id",route_id);
		intent.putExtra("client_id", selected_client_id);
		intent.putExtra("route_status", status);

		startActivityForResult(intent,5);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.client_filter_btn:
				getTemplateList = new GetTemplateList(selected_client_id);
				getTemplateList.execute();
				break;

		}
	}


	public class GetTemplateList extends AsyncTask<Void, Void, String> {
		int client_id;
		GetTemplateList(int client_id) {
			this.client_id = client_id;
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
				return parser.getTemplateList(context, client_id);
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
						    int client_count = jObj.getInt("client_count");
						    selected_client_id = jObj.getInt("client_id");
							JSONArray clientArray = jObj.getJSONArray("clData");
							if(client_count <= 1){
								clientLayout.setVisibility(View.GONE);
							} else {
								if(clientSpinner.getCount()==0) {
									clientLayout.setVisibility(View.VISIBLE);
									ArrayAdapter<NameValuePairs> clientAdapter = new ArrayAdapter<NameValuePairs>(getBaseContext(), android.R.layout.simple_spinner_item, AppUtils.setPairs(jObj.getJSONArray("clData"), getBaseContext()));
									clientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
									clientSpinner.setAdapter(clientAdapter);
                                    clientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                            NameValuePairs pair_id = (NameValuePairs) parent.getSelectedItem();
                                            selected_client_id = pair_id.getId();
                                            System.out.println("Client ID:"+selected_client_id);
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {
                                        }
                                    });

								}
							}
                            errorLayout.setVisibility(View.GONE);
							data = AppUtils.drawTemplateList(jObj, context);
							lv=(ListView)findViewById(R.id.pax_list);
							lv.setAdapter(new templateAdapter(context, data, TemplateList.this));
						} else {
							int client_count = jObj.getInt("client_count");
							selected_client_id = jObj.getInt("client_id");
							if(client_count <= 1) {
								clientLayout.setVisibility(View.GONE);
							}
							data = AppUtils.drawTemplateList(jObj, context);
							lv=(ListView)findViewById(R.id.pax_list);
							lv.setAdapter(new templateAdapter(context, data, TemplateList.this));
                            errorLayout.setVisibility(View.VISIBLE);
                            errorTextView.setText(jObj.getString("error"));
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
				//Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

			}
		}
	}

	public class RouteDeleteTask extends AsyncTask<Void, Void, String> {
		int route_id;
		int client_id;
		String action;
		RouteDeleteTask(String action, int route_id, int client_id) {
			this.action = action;
			this.route_id = route_id;
			this.client_id = client_id;
		}

		@Override
		protected String doInBackground(Void... arg0) {
			try {
				ContentParser parser = new ContentParser(getBaseContext());
				return parser.saveRoutePlans(context, action, route_id, client_id, "", 0);
			} catch (Exception ex) {
				//finish();
			}
			return null;
		}

		@Override
		protected void onPostExecute(final String result) {
			routeDeleteTask = null;
			//showProgress(false);
			if (!TextUtils.isEmpty(result)) {
				try {
					JSONObject jObj = new JSONObject(result);
					if (jObj.has("status")) {
						String statuss = jObj.getString("status");
						if (statuss.equalsIgnoreCase("ok")) {
							System.out.println("Route Deleted...");
                            getTemplateList = new GetTemplateList(selected_client_id);
                            getTemplateList.execute();
						} else {
							String errmsg = jObj.getString("error");
							//AppUtils.showAlertDialog(context, "Error", errmsg);
							Toast.makeText(getApplicationContext(), errmsg, Toast.LENGTH_LONG).show();
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
			routeDeleteTask = null;
			//showProgress(false);
		}
	}

}