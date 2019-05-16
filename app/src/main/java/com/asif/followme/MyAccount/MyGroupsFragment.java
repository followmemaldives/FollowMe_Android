package com.asif.followme.MyAccount;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.asif.followme.*;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.util.AppUtils;
import com.asif.followme.util.SettingsPreferences;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyGroupsFragment extends Fragment implements AdapterView.OnItemClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    public static ListView lv;

    static SwipeRefreshLayout swipeLayout;
    static int HireNav = 0;
    private static Context context;
    private GetMyGroups mGroupsTask = null;
    public int MyAccountType;
    MenuItem item_group;
//    private ToggleButton switchLive;
//    private static MenuItem switchItem;

    public static List<DataMyGroups> data = new ArrayList<DataMyGroups>();
    private static Activity activity;
    public static String selected_item_id,selected_item_name;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MyGroupsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MyGroupsFragment newInstance(int columnCount) {
        MyGroupsFragment fragment = new MyGroupsFragment();
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
//        lv.setOnClickListener((AdapterView.OnClickListener) context);



//        context = getActivity().getApplicationContext();
        getActivity().setTitle("My Groups");
//        MyAccountType = SettingsPreferences.getSelectedMyAccount(context);

        context = view.getContext();
        activity = getActivity();
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);
        swipeLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mGroupsTask = new GetMyGroups();
                        mGroupsTask.execute((Void) null);
                    }
                }
        );
 //       populateMyGroups(context);

        String[] PERMISSIONS = {android.Manifest.permission.INTERNET, android.Manifest.permission.ACCESS_NETWORK_STATE, android.Manifest.permission.WAKE_LOCK, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE};
        if(!AppUtils.hasPermissions(getActivity(), PERMISSIONS)){
            requestPermissions(PERMISSIONS, 1);
        }

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
        return view;
    }

/*    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        System.out.println("OnStart...................................................................");
        mGroupsTask = new GetMyGroups();
        mGroupsTask.execute((Void) null);
    }
*/
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        System.out.println("OnResume...................................................................");
        mGroupsTask = new GetMyGroups();
        mGroupsTask.execute((Void) null);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
//        switchItem = menu.findItem(R.id.my_switch_item);
/*        if(SettingsPreferences.getIsTracker(context)) {
            switchItem.setVisible(true);
        } else {
            switchItem.setVisible(false);
        }
*/
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.my_group_menu, menu);
        item_group = menu.findItem(R.id.action_new_group);
/*        switchItem = menu.findItem(R.id.my_switch_item);
        switchLive = (ToggleButton) switchItem.getActionView().findViewById(R.id.switch_live);
        if(SettingsPreferences.getTrackMe(context).equalsIgnoreCase("on")){
            if(((MyActivity)getActivity()).isMyServiceRunning(LocationService.class)){
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
                        ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, 2);
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
        */
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_new_group){
            Intent intent		=	new Intent(context, NewGroupForm.class);
            intent.putExtra("action","new");
            startActivity(intent);
            return true;
        }

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        DataMyGroups object = (DataMyGroups) adapterView.getItemAtPosition(i);
        selected_item_name = object.getName();
        selected_item_id = object.getId();

        //Lets use this  for common store location to have same for my device and groups
        MyActivity.selected_item_id = selected_item_id;
        MyActivity.selected_item_name = selected_item_name;
        SettingsPreferences.setSelectedItemID(context,selected_item_id);
        SettingsPreferences.setSelectedItemName(context,selected_item_name);
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
        Intent intent = new Intent(context, MyMenuActivity.class);
        intent.putExtra("vname",selected_item_name);
        intent.putExtra("acc","groups");
        startActivityForResult(intent,5);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("onActivityResult Call back from MyGroupFragment.......................................................");
        System.out.println("Request Code:"+requestCode);
        switch(requestCode) {
            case (4):  //return from New Device form
                if (resultCode == Activity.RESULT_OK) {
                    String action = data.getStringExtra("action");
                }
                break;
            case (5): { // from MyMenuActivity
                if (resultCode == Activity.RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.
                    final String action = data.getStringExtra("action");
                    final String group_id = selected_item_id;
                    String title = selected_item_name;
                    if (action.equalsIgnoreCase("group_delete")) {
                        AppUtils.showConfirmDialog(context, title, "Do you want to Delete this Group Now?",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DeleteGroup mGroupTask = new DeleteGroup(context, group_id, "del",MyGroupsFragment.this);
                                    mGroupTask.execute((Void) null);
                                }
                            });
                    }
                }
            }

        }

    }


/*    public void populateMyGroups(Context context){
        Database db = new Database(context);
        data = db.fetchallMyGroupsData();
        lv.setAdapter(new MyGroupsListAdapter(context, data, MyGroupsFragment.this));
       // lv.setOnItemClickListener((AdapterView.OnItemClickListener) context);
        if(data.size()==0){
            Toast.makeText(context, "List is empty", Toast.LENGTH_LONG).show();
        }
//        if(state != null) {
//            lv.onRestoreInstanceState(state);
//        }
    }
*/

    public class GetMyGroups extends AsyncTask<Void, Void, String> {
        //GetMyDevices{};
        @Override
        protected void onPreExecute() {
//              System.out.println("Calling GetMyVessels with id:"+id2+"++++++++++++++++++++++++++++++++++++");
            swipeLayout.setRefreshing(true);

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... arg0) {
            try{
                ContentParser parser = new ContentParser(context);
                String result = parser.GetMyGroups(context);
                return result;

             /*   if (!TextUtils.isEmpty(result)) {
                    try {
                        JSONObject jObj = new JSONObject(result);
                        if (jObj.has("status")) {
                            String statuss = jObj.getString("status");
                            if (statuss.equalsIgnoreCase("ok")) {
                                AppUtils.saveMyGroups(jObj, context);
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
//            SettingsPreferences.setSelectedMyAccount(context, 0);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
                            //populateMyGroups(context);
                            data = AppUtils.drawMyGroups(jObj, context);
                            lv.setAdapter(new MyGroupsListAdapter(context, data, MyGroupsFragment.this));

                          //  lv.setAdapter(new PublicHireAdapter(context, data, PublicHireFragment.this));

                        } else {
                            if(jObj.getString("status").equalsIgnoreCase("login")){
//                                mReAuthTask = new UserReLoginTask();
//                                mReAuthTask.execute((Void) null);
                                Intent intent = new Intent(context, LoginActivity.class);
                                intent.putExtra("sender","my_group");
//                                intent.putExtra("action","relogin");
                                startActivity(intent);
                                getActivity().finish();

                                //    Intent intent = new Intent(MyActivity.this, LoginActivity.class);
                                //   startActivity(intent);
                                //   finish();
                            } else {
                                //    Toast.makeText(getApplicationContext(), "Error: 122-1"+jObj.getString("error"), Toast.LENGTH_LONG).show();
                                AppUtils.showAlertDialog(context, "Error", jObj.getString("error"));
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

        public class DeleteGroup extends AsyncTask<Void, Void, String> {
            String action;
            int fleet_id;
            Dialog dialog;
            Context con;
            MyGroupsFragment fragment;
            DeleteGroup(Context context,String id,String action,MyGroupsFragment fragment){
                con = context;
                fleet_id = Integer.parseInt(id);
                this.action = action;
                this.fragment = fragment;
            }
            @Override
            protected void onPreExecute() {
                dialog= ProgressDialog.show(con, null, "Deleting, Please wait...");
                dialog.show();
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(Void... arg0) {
                // TODO Auto-generated method stub
                try{
                    ContentParser parser = new ContentParser(con);
                    String result = parser.changeGroup(con, action, "", fleet_id);
                    return result;

                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
                //		System.out.println(result);
                //		SettingsPreferences.setSelectedAccount(context, id2);
                if (!TextUtils.isEmpty(result)) {
                    try {
                        JSONObject jObj = new JSONObject(result);
                        if (jObj.has("status")) {
                            if (jObj.getString("status").equalsIgnoreCase("ok")) {
                                //fragment.populateMyGroups(context);
                                data = AppUtils.drawMyGroups(jObj, context);
                                lv.setAdapter(new MyGroupsListAdapter(context, data, MyGroupsFragment.this));

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
            }

        }

}
