package com.asif.followme.Flight;

import android.app.Activity;
import android.content.Context;
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
import com.asif.followme.MyMenuActivity;
import com.asif.followme.MyPreferences;
import com.asif.followme.NewDevice;
import com.asif.followme.R;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.util.AppUtils;
import com.asif.followme.util.SettingsPreferences;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class AirportFragment extends Fragment implements AdapterView.OnItemClickListener,ActivityCompat.OnRequestPermissionsResultCallback {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    public static ListView lv;

    static SwipeRefreshLayout swipeLayout;
    int HireState = 0;
    static int HireNav = 0;
    private static Context context;
    private GetAirports mAirportsTask = null;

    private MenuItem atoll_menu;
    public int publicAccountType;



    public static List<DataAirports> data = new ArrayList<DataAirports>();
    private static Activity activity;
    public static String selected_item_id,selected_item_name;
    private LinearLayout errorLayout;
    private TextView errorTextView;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AirportFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static AirportFragment newInstance(int columnCount) {
        AirportFragment fragment = new AirportFragment();
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
        View view = inflater.inflate(R.layout.content_public, container, false);
        setHasOptionsMenu(true);
        lv= (ListView) view.findViewById(R.id.public_list);
        lv.setOnItemClickListener(this);
//      lv.setOnClickListener((AdapterView.OnClickListener) context);


        errorLayout = (LinearLayout) view.findViewById(R.id.error_layout);
        errorTextView = (TextView) view.findViewById(R.id.error_text_view);

//        context = getActivity().getApplicationContext();
        getActivity().setTitle("Flight Schedule");
//      MyAccountType = SettingsPreferences.getSelectedMyAccount(context);

        context = view.getContext();
        activity = getActivity();
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);
        swipeLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getAirports();
                    }
                }
        );
        getAirports();

//        getPublicVessels(SettingsPreferences.getSelectedAtoll(context),"");
//        populatePublicVessels(context);

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
        //getPublicVessels(SettingsPreferences.getSelectedAtoll(context),"");
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        atoll_menu = menu.findItem(R.id.action_atolls);
        if (SettingsPreferences.getCountry(context).equalsIgnoreCase("mv")) {
            atoll_menu.setVisible(true);
        } else {
            atoll_menu.setVisible(false);
        }

//        switchItem = menu.findItem(R.id.my_switch_item);
//        if(SettingsPreferences.getMyTracker(context).equalsIgnoreCase("0")){
//            switchItem.setVisible(false);
//        } else {
//            switchItem.setVisible(true);
//        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //getMenuInflater().inflate(R.menu.public_menu,menu);
        inflater.inflate(R.menu.public_boat_menu, menu);



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

        DataAirports object = (DataAirports) adapterView.getItemAtPosition(i);
        String selected_airport = object.getCodeIataAirport();
        SettingsPreferences.setSelectedAirportCode(context, selected_airport);

        Intent intent = new Intent(getActivity(), FlightScheduleActivity.class);
        intent.putExtra("sender","my_account");
        startActivity(intent);

    /*    Fragment fragment = new FlightScheduleFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
    */


    }


    public void getAirports(){
        mAirportsTask = new GetAirports("MV");
        mAirportsTask.execute((Void) null);
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
                    //populatePublicVessels(context); // to refresh the images when permission accepted
                } else {
                }
                break;

        }

    }



    public class GetAirports extends AsyncTask<Void, Void, String> {
        private final String country;

        GetAirports(String country) {
            this.country = country;
        }
        @Override
        protected void onPreExecute() {
            swipeLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);
            swipeLayout.setRefreshing(true);

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... arg0) {

            String result = null;
            try{
                ContentParser parser = new ContentParser(context);
                return parser.GetAirports(context,country);

            }
            catch(Exception ex) {
                ex.printStackTrace();
            }

            return result;
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
            //PublicErrorLabel.setVisibility(View.GONE);
            System.out.println("RRRR"+result);
            //SettingsPreferences.setSelectedMyAccount(context, id2);
            if (!TextUtils.isEmpty(result)) {
                try {
                   // JSONObject jObj = new JSONObject(result);
                    JSONArray jArray = new JSONArray(result);
                    //String kk = jArray.get(0).toString();
                    //JSONObject jObj = new JSONObject(kk);
                    data = AppUtils.drawAirportList(jArray, context);
                    lv.setAdapter(new AirportsAdapter(context, data, AirportFragment.this));
                    System.out.println("JSON OK");
                    //if (jObj.has("status")) {
                        //System.out.println("A");
                        //SettingsPreferences.setSessionID(context,jObj.getString("sid"));
                        //    Time m = new Time();
                        //    m.setToNow();
                        //    long m1 = m.toMillis(true);
                        //                           SettingsPreferences.setListPublicTime(context, System.currentTimeMillis());
                        //	errmsg = jObj.getString(TAG_ERROR_MSG);
                        //                           AppUtils.savePublicVessels(jObj, context);
              //          state = lv.onSaveInstanceState();

                        //populatePublicVessels(context);
                        System.out.println("B");
                        //..int dd = jObj.getString("aaData").length();
                        //System.out.println("This is aaData:"+dd);


                        //	System.out.println(jObj.toString());

                        //	loading.setText("Login Successful...");
                        //	Intent intent = new Intent(context, Get_list.class);
                        //	startActivity(intent);
                        //	finish();

                    //} else {
                    //Toast.makeText(getApplicationContext(), "No Status,"+result, Toast.LENGTH_LONG).show();
                    //System.out.println("Result"+result);

                    //}
                } catch (JSONException e) {
                    Toast.makeText(context, "Unable to reach server. Please try again", Toast.LENGTH_LONG).show();
                    //loading.setText("Unable to reach Server...");
                    //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(context, "Unable to reach server. Please try again", Toast.LENGTH_LONG).show();

            }
        }
    }

}
