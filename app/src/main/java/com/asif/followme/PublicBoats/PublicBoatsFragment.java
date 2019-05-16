package com.asif.followme.PublicBoats;

import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.asif.followme.Database;
import com.asif.followme.PublicActivity;
import com.asif.followme.R;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.model.DataPublicBoats;
import com.asif.followme.util.AppConstants;
import com.asif.followme.util.AppUtils;
import com.asif.followme.util.SettingsPreferences;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PublicBoatsFragment extends Fragment implements AdapterView.OnItemClickListener,ActivityCompat.OnRequestPermissionsResultCallback {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    public static ListView lv;


    static SwipeRefreshLayout swipeLayout;
    int HireState = 0;
    static int HireNav = 0;
    private static Context context;
    private GetPublicVessels mPublicTask = null;
    private GeoAlertTask setAlertTask = null;
    public static String currentIsland ="";
    String alertMsg="";

    private MenuItem atoll_menu;
    public int publicAccountType;
    final String PUBLIC_LIST_TITLE = "Public List";
    int selected_atoll = 0;

    TextView heading,adText;
    String banner, link;
    private JSONArray adTextArray = null;
    private int textsize=0;
    private int textmargin=0;
    private Handler mHandler;
    public int adTextCounter;
    public static boolean runAd;




    public static List<DataPublicBoats> data = new ArrayList<DataPublicBoats>();
    private static Activity activity;
    public static String selected_item_id,selected_item_name;
    private LinearLayout errorLayout;
    private TextView errorTextView;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PublicBoatsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PublicBoatsFragment newInstance(int columnCount) {
        PublicBoatsFragment fragment = new PublicBoatsFragment();
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
        context = view.getContext();
        //context=getContext();


        errorLayout = (LinearLayout) view.findViewById(R.id.error_layout);
        errorTextView = (TextView) view.findViewById(R.id.error_text_view);

//        context = getActivity().getApplicationContext();
        String title = SettingsPreferences.getPublicTitle(context);
        if(PublicActivity.NavigationIndex != 2){
            getActivity().setTitle(title);
        }
//      MyAccountType = SettingsPreferences.getSelectedMyAccount(context);


        activity = getActivity();
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);
        swipeLayout.setOnRefreshListener(
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeLayout.setRefreshing(true);
                    getPublicVessels(SettingsPreferences.getSelectedAtoll(context),"");
                }
            }
        );


        String ad = SettingsPreferences.getAd(context);
        ImageView adImg = (ImageView) view.findViewById(R.id.ad_img);
        AdView adGoogle = (AdView) view.findViewById(R.id.adView);
        RelativeLayout adLayout = (RelativeLayout) view.findViewById(R.id.ad_layout);
        adText = (TextView) view.findViewById(R.id.ad_text);
        mHandler = new Handler();
        if(AppConstants.DOMAIN.equalsIgnoreCase("followme.mv")){
            try{
                JSONObject adv = new JSONObject(ad);
                JSONObject adv_list = new JSONObject(adv.getString("list"));
                banner = adv_list.getString("img");
                link = adv_list.getString("link");
                System.out.println(adv_list);
                try{
                    adTextArray = adv_list.getJSONArray("text");
                    textsize=Integer.parseInt(adv_list.getString("text-size"));
                    textmargin=Integer.parseInt(adv_list.getString("text-margin"));

                } catch (Exception e){
                    e.getStackTrace();
                }
                String input = "http://"+ AppConstants.DOMAIN+"/ad/"+banner;
                new DownloadImageTask((ImageView) view.findViewById(R.id.ad_img)).execute(input);
                adGoogle.setVisibility(View.GONE);
                if(!link.equalsIgnoreCase("")){
                    adImg.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse(link));
                            startActivity(intent);
                        }
                    });
                }
            } catch (Exception e){
                adLayout.setVisibility(View.GONE);
                AdRequest adRequest = new AdRequest.Builder().build();
                adGoogle.loadAd(adRequest);
            }
        } else {
            adLayout.setVisibility(View.GONE);
            AdRequest adRequest = new AdRequest.Builder().build();
            adGoogle.loadAd(adRequest);
        }



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
        System.out.println("onStart PublicBoatFragment.....................");
        if(PublicActivity.public_refresh_time+60000 > System.currentTimeMillis()){
            populatePublicVessels(context);
        } else {
            PublicActivity.public_refresh_time=System.currentTimeMillis();
            getPublicVessels(SettingsPreferences.getSelectedAtoll(context), "");
        }
        if(adTextArray!=null){
            AnimateText();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        runAd = false;
        mHandler.removeCallbacks(mStatusChecker);

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
        System.out.println("onCreateOptionMenu............................................................................");
        //item_device = menu.findItem(R.id.action_new_device);
        publicAccountType = SettingsPreferences.getSelectedPublicAccount(context);
        if(publicAccountType == 0) {
            SearchManager searchManager = (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            //SearchView searchView = new SearchView(context);
            searchView.setQueryHint(getString(R.string.search_hint));
            //searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

            // Assumes current activity is the searchable activity
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default

            final MenuItem menuItem = menu.add("Search");
            menuItem.setTitle("Search");
            menuItem.setActionView(searchView);
            menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);



            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    getPublicVessels(SettingsPreferences.getSelectedAtoll(context), "");
                    return false;
                }
            });
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    getPublicVessels(SettingsPreferences.getSelectedAtoll(context), query);
                    // runQuery(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return true;
                }
            });
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

/*    @Override
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

    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        int atoll;
        String public_title=PUBLIC_LIST_TITLE;
        //System.out.println("Menu Selected:"+id);
        //noinspection SimplifiableIfStatement

        if(id == R.id.action_atolls){
            // Intent intent = new Intent(context, AtollMenuActivity.class);
            //intent.putExtra("vname",selected_item_name);
            // startActivityForResult(intent,7);


            return true;

        }
        if(id == R.id.atoll_all){
//            setTitle(PUBLIC_LIST_TITLE);
            public_title=PUBLIC_LIST_TITLE;
            selected_atoll = 0;
//            getPublicVessels(selected_atoll, "");
        }
        if(id == R.id.atoll_ha){
            selected_atoll = 1;
            public_title="Haa Alif";
        }
        if(id == R.id.atoll_hd){
            selected_atoll = 2;
            public_title="Haa Dhaalu";
        }
        if(id == R.id.atoll_sh){
            selected_atoll = 3;
            public_title="Shaviyani";
        }
        if(id == R.id.atoll_n){
            selected_atoll = 4;
            public_title="Noonu";
        }
        if(id == R.id.atoll_r){
            selected_atoll = 5;
            public_title="Raa";
        }
        if(id == R.id.atoll_b){
            selected_atoll = 6;
            public_title="Baa";
        }
        if(id == R.id.atoll_lh){
            selected_atoll = 7;
            public_title="Lhaviyani";
        }
        if(id == R.id.atoll_k){
            selected_atoll = 8;
            public_title="Kaafu";
        }
        if(id == R.id.atoll_aa){
            selected_atoll = 9;
            public_title="Alif Alif";
        }
        if(id == R.id.atoll_ad){
            selected_atoll = 10;
            public_title="Alif Dhaalu";
        }
        if(id == R.id.atoll_v){
            selected_atoll = 11;
            public_title="Vaavu";
        }
        if(id == R.id.atoll_m){
            selected_atoll = 12;
            public_title="Meemu";
        }
        if(id == R.id.atoll_f){
            selected_atoll = 13;
            public_title="Faafu";
        }
        if(id == R.id.atoll_d){
            selected_atoll = 14;
            public_title="Dhaalu";
        }
        if(id == R.id.atoll_t){
            selected_atoll = 15;
            public_title="Thaa";
        }
        if(id == R.id.atoll_l){
            selected_atoll = 16;
            public_title="Laamu";
        }
        if(id == R.id.atoll_ga){
            selected_atoll = 17;
            public_title="Gaafu Alif";
        }
        if(id == R.id.atoll_gd){
            selected_atoll = 18;
            public_title="Gaafu Dhaal";
        }
        if(id == R.id.atoll_gn){
            selected_atoll = 19;
            public_title="Fuvahmulah";
        }
        if(id == R.id.atoll_s){
            selected_atoll = 20;
            public_title="Seenu";
        }
        if(id == R.id.atoll_male){
            selected_atoll = 21;
            public_title="Male City";
        }
        getActivity().setTitle(public_title);
        SettingsPreferences.setSelectedAtoll(context, selected_atoll);
        SettingsPreferences.setPublicTitle(context,public_title);
        getPublicVessels(selected_atoll,"");

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

        DataPublicBoats object = (DataPublicBoats) adapterView.getItemAtPosition(i);
        selected_item_id = object.getId();
        SettingsPreferences.setSelectedItemID(context, selected_item_id);
        selected_item_name = object.getName();
        SettingsPreferences.setSelectedItemName(context,selected_item_name);
        selected_item_id = selected_item_id;
        selected_item_name = selected_item_name;
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);

        if (resultCode == ConnectionResult.SUCCESS)
        {
            Intent intent = new Intent(getActivity(), PublicMapsActivity.class);
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


    public void getPublicVessels(int atoll, String find){
        mPublicTask = new GetPublicVessels(atoll, find);
        mPublicTask.execute((Void) null);
    }

    public void selectMenuItem(int id){
        System.out.println("selectMenuItem Called from PublicHireFragment................................");
        selected_item_id = String.valueOf(id);
        Intent intent = new Intent(context,PublicMenuActivity.class);
        intent.putExtra("vname",selected_item_name);
        intent.putExtra("acc","public_boats");
        startActivityForResult(intent,5);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("onActivityResult Call back.......................................................");
        switch(requestCode) {
            case (5):{ // public menu
                if (resultCode == Activity.RESULT_OK) {
                    final String action = data.getStringExtra("action");
                    final String device_id =selected_item_id;
                    String title = selected_item_name;
                    String message ="";
                    switch(action){
                        case "share":
                            shareLocation();
                            break;
                        case "alert_me":
                            //System.out.println("Current Island:"+currentIsland);
                            if(currentIsland.equalsIgnoreCase("-")){
                                alertMsg = "when "+selected_item_name+" arrives next island";
                            } else {
                                alertMsg = "when "+selected_item_name+" leaves "+currentIsland;
                            }
                            showNotificationDialog(alertMsg);
                    }
                }
                break;

            }




        }

    }

    public void shareLocation(){
        String id=SettingsPreferences.getSelectedItemID(context);
        String textToShare = "https://m.followme.mv/public/"+id+"/";
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        //   	intent.setType("text/html");
//   	intent.putExtra(Intent.EXTRA_SUBJECT, "sample");
        intent.putExtra(Intent.EXTRA_TEXT, textToShare);
        startActivity(Intent.createChooser(intent, "Share Location"));

    }
    private void showNotificationDialog(String msg) {
        new AlertDialog.Builder(context)
                .setTitle("Move Notification")
                .setMessage("Alert me "+msg)
                .setCancelable(true)
                .setNegativeButton("NO",null)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //setPublicNotifiction(PublicActivity.selected_vessel_id,currentIsland);
                        System.out.println("ID:"+SettingsPreferences.getSelectedItemID(context)+",IS:"+currentIsland);
                        setAlertTask = new GeoAlertTask(SettingsPreferences.getSelectedItemID(context), currentIsland);
                        setAlertTask.execute((Void) null);


                    }
                })
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .show();

    }
    public class GeoAlertTask extends AsyncTask<Void, Void, String> {

        private final String device_id;
        private final String fence_id;

        GeoAlertTask(String id, String fid) {
            device_id = id;
            fence_id = fid;
        }

        @Override
        protected String doInBackground(Void... arg0) {
            try{
                ContentParser parser = new ContentParser(context);
                return parser.setGeoAlert(context,device_id,fence_id);
                //GetText();
            }
            catch(Exception ex)
            {
                //finish();
            }
            return null;

        }

        @Override
        protected void onPostExecute(final String result) {
            setAlertTask = null;
            //showProgress(false);

            System.out.println("Login REsult: "+result);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
//					System.out.println("SID:"+jObj.getString("sid"));
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
                            AppUtils.showAlertDialog(context,"Alert Saved","You will be notified "+alertMsg);
                        } else {
                            String errmsg = jObj.getString("error");
                            //AppUtils.showAlertDialog(context, "Error", errmsg);
                            Toast.makeText(context, errmsg, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(context, "Unable to reach server. Please check your internet connection", Toast.LENGTH_LONG).show();
                //AppUtils.showAlertDialog(context, "Timeout", "Unable to reach Server. Please try again!");
            }







        }

        @Override
        protected void onCancelled() {
            setAlertTask = null;
            //showProgress(false);
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
                    populatePublicVessels(context); // to refresh the images when permission accepted
                } else {
                }
                break;

        }

    }

    public void populatePublicVessels(Context context){
//        System.out.println("Lets Populate");
        Database db = new Database(context);
        data = db.fetchallPublicdata();
        PublicActivity.state_boat_list = lv.onSaveInstanceState();
       // lv.setAdapter(new PublicBoatsAdapter(context, data));
        lv.setAdapter(new PublicBoatsAdapter(context, data, PublicBoatsFragment.this));

        //lv.setOnItemClickListener((AdapterView.OnItemClickListener) context);
        if(data.size()==0){
//            Toast.makeText(context, "List is empty", Toast.LENGTH_LONG).show();
        }
        if(PublicActivity.state_boat_list != null) {
            System.out.println("State is NOT NULL");
            lv.onRestoreInstanceState(PublicActivity.state_boat_list);
        } else {
            System.out.println("State is NULL");
        }

    }

    public class GetPublicVessels extends AsyncTask<Void, Void, String> {
        private final String search;
        int atoll;

        GetPublicVessels(int atoll, String find) {
            this.search = find;
            this.atoll = atoll;
        }
        @Override
        protected void onPreExecute() {
            swipeLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);
            swipeLayout.setRefreshing(true);
            if(publicAccountType==0 || publicAccountType==2) {  //do not save title for group, instead display manually
                //SettingsPreferences.setSelectedAtoll(context, atoll);
                //SettingsPreferences.setPublicTitle(context, getTitle().toString());
            }
            if(!TextUtils.isEmpty(search)){
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... arg0) {
            //			fetchDataFromJSON();
            //	uname= loginPreferences.getString("username", "");
            //	pwd= loginPreferences.getString("password", "");
            //	System.out.println("DoBackground....");
            String result = null;
            try{
                ContentParser parser = new ContentParser(context);
                result = parser.GetPublicVessels(context,atoll, search);

                if (!TextUtils.isEmpty(result)) {
                    try {
                        JSONObject jObj = new JSONObject(result);
                        if (jObj.getString("status").equalsIgnoreCase("ok")) {
                            //    Time m = new Time();
                            //    m.setToNow();
                            //    long m1 = m.toMillis(true);
                            SettingsPreferences.setListPublicTime(context, System.currentTimeMillis());
                            //	errmsg = jObj.getString(TAG_ERROR_MSG);
                            AppUtils.savePublicVessels(jObj, context);

                        }

                        return result;
                        //} else {
                        //Toast.makeText(getApplicationContext(), "No Status,"+result, Toast.LENGTH_LONG).show();
                        //System.out.println("Result"+result);

                        //}
                    } catch (JSONException e) {
                        System.out.println(e.toString());
                        //e.printStackTrace();
                    }
                }
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
                    JSONObject jObj = new JSONObject(result);
                    //if (jObj.has("status")) {
                    if (jObj.getString("status").equalsIgnoreCase("ok")) {
                        //System.out.println("A");
                        //SettingsPreferences.setSessionID(context,jObj.getString("sid"));
                        //    Time m = new Time();
                        //    m.setToNow();
                        //    long m1 = m.toMillis(true);
                        //                           SettingsPreferences.setListPublicTime(context, System.currentTimeMillis());
                        //	errmsg = jObj.getString(TAG_ERROR_MSG);
                        //                           AppUtils.savePublicVessels(jObj, context);
              //          state = lv.onSaveInstanceState();

                        populatePublicVessels(context);
                        System.out.println("B");
                        int dd = jObj.getString("aaData").length();
                        System.out.println("This is aaData:"+dd);

                        if(jObj.getString("aaData").length() == 2){ //string length of []
                            if (SettingsPreferences.getSelectedAtoll(context) > 0) {
                                Toast.makeText(context, "Not Found", Toast.LENGTH_LONG).show();
                            } else {
                                if(SettingsPreferences.getCountry(context).equalsIgnoreCase("mv")){
                                    Toast.makeText(context, "Not Found", Toast.LENGTH_LONG).show();
                                } else {
                                    if(search.equalsIgnoreCase("")) {
                                   /*     if(!isFinishing()) {
                                            AppUtils.showAlertDialog(context, "Not Found", "No Public Vessel or Vehicle available for your Country . Please go to Settings and check the selected country");
                                        } else {
                                            Toast.makeText(context, "No Public Vessel or Vehicle available for your Country. You may add new tracking devices under My Account and start Tracking", Toast.LENGTH_LONG).show();
                                        }
                                        */
                                    } else {
                                        Toast.makeText(context, "No matchnig Public item found",Toast.LENGTH_LONG).show();
                                    }
                                }
                                // Toast.makeText(context, "Public List is empty. Please goto Preferences and check your country", Toast.LENGTH_LONG).show();
                            }
                        }
                        //	System.out.println(jObj.toString());

                        //	loading.setText("Login Successful...");
                        //	Intent intent = new Intent(context, Get_list.class);
                        //	startActivity(intent);
                        //	finish();
                    } else {
                        //                          AppUtils.showAlertDialog(context, "Error", jObj.getString("error"));
                        //	loading.setText("Authentication failed");
                        //	Intent intent = new Intent(SplashScreen.this, Login_Activity.class);
                        //	startActivity(intent);
                        //	finish();
                    }
                    //} else {
                    //Toast.makeText(getApplicationContext(), "No Status,"+result, Toast.LENGTH_LONG).show();
                    //System.out.println("Result"+result);

                    //}
                } catch (JSONException e) {
                    Toast.makeText(context, "Unable to reach server. Please try again", Toast.LENGTH_LONG).show();
                    //loading.setText("Unable to reach Server...");
                    //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    //e.printStackTrace();
                }

            } else {
                Toast.makeText(context, "Unable to reach server. Please try again", Toast.LENGTH_LONG).show();

            }
        }
    }

    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public void AnimateText(){
        adTextCounter = 0;
        runAd=true;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)adText.getLayoutParams();
        //	LayoutParams params2 = (LayoutParams)adText.getLayoutParams();
        //	int pad = toPx(textpadding);
        params.setMargins(toPx(textmargin), 0, 0, 0); //substitute parameters for left, top, right, bottom
        adText.setLayoutParams(params);
        //	adText.setPadding(textpadding, textpadding, textpadding, textpadding);

        //	adText.setMovementMethod(new ScrollingMovementMethod());

        adText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textsize);

        mStatusChecker.run();

    }
    public int toPx(int size){
        Resources r = context.getResources();
        int tm_px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                size,
                r.getDisplayMetrics()
        );
        return tm_px;

    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            if(adTextCounter>=adTextArray.length()){
                adTextCounter=0;
            }
//          updateStatus(); //this function can change value of mInterval.
            try {
                adText.setText(adTextArray.getString(adTextCounter));
                //adText.append("\n"+adTextArray.getString(adTextCounter));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            TranslateAnimation slide = new TranslateAnimation(0, 0, 300,0 );
            slide.setDuration(500);
            slide.setFillAfter(false);
            slide.setStartOffset(0);
            adText.startAnimation(slide);

            adTextCounter++;
            System.out.println("Done...............................");
            if(runAd){
                mHandler.postDelayed(mStatusChecker, 5000);
            }
        }
    };

}
