package com.asif.followme.MyAccount;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;
import com.asif.followme.*;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.model.DataMyBoats;
import com.asif.followme.util.AppUtils;
import com.asif.followme.util.SettingsPreferences;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyBoatsFragment extends Fragment implements AdapterView.OnItemClickListener,ActivityCompat.OnRequestPermissionsResultCallback {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    public static ListView lv;

    static SwipeRefreshLayout swipeLayout;
    int HireState = 0;
    static int HireNav = 0;
    private static Context context;
    private GetMyDevices mDevicesTask = null;
    private DeleteDevice mDeleteTask = null;
    public int MyAccountType;
    MenuItem item_device;
    private ToggleButton switchLive;
    public static MenuItem switchItem;

    public static List<DataMyBoats> data = new ArrayList<DataMyBoats>();
    private static Activity activity;
    public static String selected_item_id,selected_item_name;
    private LinearLayout errorLayout;
    private TextView errorTextView;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MyBoatsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MyBoatsFragment newInstance(int columnCount) {
        MyBoatsFragment fragment = new MyBoatsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_my, container, false);
        setHasOptionsMenu(true);
        lv= (ListView) view.findViewById(R.id.my_list);
        lv.setOnItemClickListener(this);
//      lv.setOnClickListener((AdapterView.OnClickListener) context);


        errorLayout = (LinearLayout) view.findViewById(R.id.error_layout);
        errorTextView = (TextView) view.findViewById(R.id.error_text_view);

//        context = getActivity().getApplicationContext();
        getActivity().setTitle("My Devices");
//        MyAccountType = SettingsPreferences.getSelectedMyAccount(context);

        context = view.getContext();
        activity = getActivity();
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);
        swipeLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mDevicesTask = new GetMyDevices();
                        mDevicesTask.execute((Void) null);
                    }
                }
        );

        populateMyVessels(context);

        // Set the adapter
/*        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
           // recyclerView.setAdapter(new PublicHireAdapter(context, data));
        }
 */

        String[] PERMISSIONS = {android.Manifest.permission.INTERNET, android.Manifest.permission.ACCESS_NETWORK_STATE, android.Manifest.permission.WAKE_LOCK, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE};
        if(!AppUtils.hasPermissions(getActivity(), PERMISSIONS)){
            requestPermissions(PERMISSIONS, 1);
        }

        return view;
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        mDevicesTask = new GetMyDevices();
        mDevicesTask.execute((Void) null);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
//        switchItem = menu.findItem(R.id.my_switch_item);
//        if(SettingsPreferences.getMyTracker(context).equalsIgnoreCase("0")){
//            switchItem.setVisible(false);
//        } else {
//            switchItem.setVisible(true);
//        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.my_devices_menu, menu);
        System.out.println("onCreateOptionMenu............................................................................");
        item_device = menu.findItem(R.id.action_new_device);
        switchItem = menu.findItem(R.id.my_switch_item);
        if(SettingsPreferences.getMyTracker(context).equalsIgnoreCase("")){
            switchItem.setVisible(false);
        }
        switchLive = (ToggleButton) switchItem.getActionView().findViewById(R.id.switch_live);
            if (SettingsPreferences.getTrackMe(context).equalsIgnoreCase("on")) {
                if (((MyActivity) getActivity()).isMyServiceRunning(LocationService.class)) {
                    switchLive.setChecked(true);
                } else {
                    switchLive.setChecked(false);
                }
            } else {
                switchLive.setChecked(false);
            }

        switchLive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean track) {
                if(track){
//                        System.out.println("Track ON");
                    String[] PERMISSIONS = {android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.WAKE_LOCK};
                    if(!AppUtils.hasPermissions(context, PERMISSIONS)){
                        requestPermissions(PERMISSIONS, 2);
                    } else {
                        SettingsPreferences.setTrackMe(context, "on");
                        ((MyActivity)getActivity()).startService(MyActivity.ServiceIntent);
                    }

                } else {
                    SettingsPreferences.setTrackMe(context,"off");
//                        System.out.println("Track OFF");
                    getActivity().stopService(MyActivity.ServiceIntent);
                    //Your code when unchecked
                }
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_new_device){
            Intent intent		=	new Intent(context, NewDevice.class);
            activity.startActivityForResult(intent,4);
            //startActivity(intent);
            return true;
        }
        if (id == R.id.action_settings) {
            Intent intent = new Intent(context, MyPreferences.class);
            startActivity(intent);
            return true;
        }

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        System.out.println("onItemClick at MyBoatsFragment.......................");
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        DataBids object = (DataBids) adapterView.getItemAtPosition(i);
/*        DataMy object = (DataMy) adapterView.getItemAtPosition(i);
        selected_item_name = object.getName();
        selected_item_id = object.getId();
        System.out.println("On Item Click................");
        Fragment newFragment = new PublicBidFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container,newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        */

        DataMyBoats object = (DataMyBoats) adapterView.getItemAtPosition(i);
        selected_item_id = object.getId();
        SettingsPreferences.setSelectedItemID(context, selected_item_id);
        selected_item_name = object.getName();
        SettingsPreferences.setSelectedItemName(context,selected_item_name);
        MyActivity.selected_item_id = selected_item_id;
        MyActivity.selected_item_name = selected_item_name;
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);

        if (resultCode == ConnectionResult.SUCCESS)
        {
            Intent intent = new Intent(getActivity(), MyMapsActivity.class);
            intent.putExtra("title",selected_item_name);
//            intent.putExtra("selected_id", selected_item_id);
//			Intent intent = new Intent(Get_list.this, DrawPointActivity.class);
            startActivity(intent);
//            getActivity().finish();
            //		Toast.makeText(getApplicationContext(),"isGooglePlayServicesAvailable SUCCESS",Toast.LENGTH_LONG).show();
        }
        else
        {
            //GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), requestCode);
            dialog.show();
        }


    }




    public void selectMenuItem(String id) {
        System.out.println("selectMenuItem Called from PublicHireFragment................................");
        selected_item_id = id;
        Intent intent = new Intent(context,MyMenuActivity.class);
        intent.putExtra("vname",selected_item_name);
        intent.putExtra("acc","my_boats");
        startActivityForResult(intent,5);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("onActivityResult Call back.......................................................");
        switch(requestCode) {
            case (4):  //return from New Device form
                if (resultCode == Activity.RESULT_OK) {
                    String action = data.getStringExtra("action");
                }
                break;
            case (5):   //return from MyMenuActivity
            if (resultCode == Activity.RESULT_OK) {
                // TODO Extract the data returned from the child Activity.
                final String action = data.getStringExtra("action");
                final String device_id =MyBoatsFragment.selected_item_id;
                String title = selected_item_name;
                String message ="";
                if(action.equalsIgnoreCase("rem")){
                    message = "Do you want to Remove this Device from My Account?";
                }
                if(action.equalsIgnoreCase("del")){
                    message = "This will Delete the device & its history permanently from all shared users. Do you want to Delete this Device Now?";
                }
                AppUtils.showConfirmDialog(context,title,message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDeleteTask = new DeleteDevice(context,selected_item_id,action, MyBoatsFragment.this);
                                mDeleteTask.execute((Void) null);

                                mDevicesTask = new GetMyDevices();
                                mDevicesTask.execute((Void) null);

                            }
                        });
            }



        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        System.out.println("onRequestPermission(), Request Code:"+requestCode);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("Lets Reload Again after Permission Granted ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
//                    mDevicesTask = new GetMyDevices();
//                    mDevicesTask.execute((Void) null);
                    populateMyVessels(context); // to refresh the images when permission accepted
                } else {
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SettingsPreferences.setTrackMe(context, "on");
                    ((MyActivity)getActivity()).startService(MyActivity.ServiceIntent);
//                    startService(ServiceIntent);
 //                  System.out.println("Permission Granted Successfully. Write working code here. ************************************************************************************");
                } else {
                    switchLive.setChecked(false);
                    SettingsPreferences.setTrackMe(context,"off");
//                    System.out.println("You did not accept the request can not use the functionality.");
                }
                break;

        }

    }

    public void populateMyVessels(Context context){
//        System.out.println("Lets Populate");
        Database db = new Database(context);
        data = db.fetchallMyBoatsdata();
        //lv.setAdapter(new MyBoatsListAdapter(context, data));
        lv.setAdapter(new MyBoatsListAdapter(context, data, MyBoatsFragment.this));

        //lv.setOnItemClickListener((AdapterView.OnItemClickListener) context);
        if(data.size()==0){
//            Toast.makeText(context, "List is empty", Toast.LENGTH_LONG).show();
        }
        if(MyActivity.state != null) {
            System.out.println("State is NOT NULL");
            lv.onRestoreInstanceState(MyActivity.state);
        } else {
            System.out.println("State is NULL");
        }

    }


    public class GetMyDevices extends AsyncTask<Void, Void, String> {

        //GetMyDevices{};
        @Override
        protected void onPreExecute() {
            MyActivity.state = lv.onSaveInstanceState();

//              System.out.println("Calling GetMyVessels with id:"+id2+"++++++++++++++++++++++++++++++++++++");
            swipeLayout.setRefreshing(true);

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            //			fetchDataFromJSON();
            //	uname= loginPreferences.getString("username", "");
            //	pwd= loginPreferences.getString("password", "");
            //	System.out.println("DoBackground....");
            try{
                ContentParser parser = new ContentParser(context);
                return parser.GetMyDevices(context);

             /*   if (!TextUtils.isEmpty(result)) {
                    try {
                        JSONObject jObj = new JSONObject(result);
                        if (jObj.has("status")) {
                            String statuss = jObj.getString("status");
                            if (statuss.equalsIgnoreCase("ok")) {
                                AppUtils.saveMyVessel(jObj, context);
                            }
                            return result;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                */

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
            //SettingsPreferences.setSelectedMyAccount(context, 0);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
                            data = AppUtils.drawMyVessel(jObj, context);
                            lv.setAdapter(new MyBoatsListAdapter(context, data, MyBoatsFragment.this));
                            lv.onRestoreInstanceState(MyActivity.state);

                            //    Time m = new Time();
                            //    m.setToNow();
                            //   long m1 = m.toMillis(true);
                            //   SettingsPreferences.setListMyTime(context, m1);
                            //	errmsg = jObj.getString(TAG_ERROR_MSG);
//                                AppUtils.saveMyVessel(jObj, context);
                            /*MyActivity.state = lv.onSaveInstanceState();
                            populateMyVessels(context);
                            JSONArray jArray = new JSONArray(jObj.getString("aaData"));
                            if(jArray.length()>0){
                                errorLayout.setVisibility(View.GONE);
                            } else {
                                errorLayout.setVisibility(View.VISIBLE);
                                errorTextView.setText(jObj.getString("error"));
                            }
                            */
                            //	System.out.println(jObj.toString());

                            //	loading.setText("Login Successful...");
                            //	Intent intent = new Intent(context, Get_list.class);
                            //	startActivity(intent);
                            //	finish();
                        } else {
                            if(jObj.getString("status").equalsIgnoreCase("login")){
                                Intent intent = new Intent(context, LoginActivity.class);
                                intent.putExtra("sender","my_account");
//                                intent.putExtra("action","relogin");
                                startActivity(intent);
                                getActivity().finish();
//                                mReAuthTask = new UserReLoginTask();
//                                mReAuthTask.execute((Void) null);

                                //    Intent intent = new Intent(MyActivity.this, LoginActivity.class);
                                //   startActivity(intent);
                                //   finish();
                            } else {
                                   Toast.makeText(context, "Error: 122-1"+jObj.getString("error"), Toast.LENGTH_LONG).show();
                                // AppUtils.showAlertDialog(context, "Error", jObj.getString("error"));
                            }
                            //	loading.setText("Authentication failed");
                            //	Intent intent = new Intent(SplashScreen.this, Login_Activity.class);
                            //	startActivity(intent);
                            //	finish();
                        }
                    } else {
                        Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_LONG).show();
                        //	System.out.println("Result"+result);

                    }
                } catch (JSONException e) {
                    //loading.setText("Unable to reach Server...");
                    //Toast.makeText(context), "Please check your internet connection", Toast.LENGTH_LONG).show();
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {
                //Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

            }
        }
    }

    public  static class DeleteDevice extends AsyncTask<Void, Void, String> {
        String device_id,action;
        Dialog dialog;
        Context con;
        MyBoatsFragment fragment;
        DeleteDevice(Context context,String device_id,String action, MyBoatsFragment fragment){
            con = context;
            this.device_id = device_id;
            this.action = action;
            this.fragment = fragment;
        }
        @Override
        protected void onPreExecute() {
            dialog= ProgressDialog.show(con, null, "Please wait...");
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            try{
                ContentParser parser = new ContentParser(con);
                return parser.SaveNewDevice(con,device_id,action,"","","","","","","","","", "");
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

            //		System.out.println(result);
            //		SettingsPreferences.setSelectedAccount(context, id2);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        if (jObj.getString("status").equalsIgnoreCase("ok")) {
//                                newFleetDialog.dismiss();
                            //	errmsg = jObj.getString(TAG_ERROR_MSG);
                            //AppUtils.saveMyVessel(jObj, con);
                            //fragment.populateMyVessels(con);
                            //if(jObj.getString("imei").equalsIgnoreCase(SettingsPreferences.getIMEI(con))){
                            if(SettingsPreferences.getMyTracker(con).equalsIgnoreCase(device_id)){
//                                SettingsPreferences.setIsTracker(con, false);
                                SettingsPreferences.setTrackMe(con, "off");
                                SettingsPreferences.setMyTracker(con,"0");
//                                switchItem.setVisible(false);

//                                    toggleTrack.setVisibility(View.GONE);

//                                    AppUtils.stopAlarmManager(context);
                            }


                        } else {
                            //	showAlert(context,"Error",jObj.getString("error"));
                            AppUtils.showAlertDialog(con, "Error", jObj.getString("error"));
                            //alert.show();
                            //Toast.makeText(getApplicationContext(), jObj.getString("error"), Toast.LENGTH_LONG).show();
                        }


                    } else {
                        Toast.makeText(con, "Please check your internet connection", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    //loading.setText("Unable to reach Server...");
                    Toast.makeText(con, "Please check your internet connection", Toast.LENGTH_LONG).show();
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(con, "Please check your internet connection", Toast.LENGTH_LONG).show();

            }
            if(dialog.isShowing()){
                dialog.dismiss();
            }
        }

    }

}
