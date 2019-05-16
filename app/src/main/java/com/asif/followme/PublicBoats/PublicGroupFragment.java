package com.asif.followme.PublicBoats;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.*;
import com.asif.followme.Database;
import com.asif.followme.MyMenuActivity;
import com.asif.followme.R;
import com.asif.followme.manager.ContentParser;
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

public class PublicGroupFragment extends Fragment implements AdapterView.OnItemClickListener,ActivityCompat.OnRequestPermissionsResultCallback {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    public static ListView lv;

    static SwipeRefreshLayout swipeLayout;
    int HireState = 0;
    static int HireNav = 0;
    private static Context context;
    private GetPublicGroups mGroupsTask = null;

    private MenuItem atoll_menu;
    public int publicAccountType;
    public static boolean runAd;



    public static List<DataPublicGroups> data = new ArrayList<DataPublicGroups>();
    private static Activity activity;
    public static String selected_item_id,selected_item_name;
    private LinearLayout errorLayout;
    private TextView errorTextView;

    TextView heading,adText;
    String banner, link;
    private JSONArray adTextArray = null;
    private int textsize=0;
    private int textmargin=0;
    private Handler mHandler;
    public int adTextCounter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PublicGroupFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PublicGroupFragment newInstance(int columnCount) {
        PublicGroupFragment fragment = new PublicGroupFragment();
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
        getActivity().setTitle("Public Groups");
//      MyAccountType = SettingsPreferences.getSelectedMyAccount(context);

        context = view.getContext();
        activity = getActivity();
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);
        swipeLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getPublicGroups();
                    }
                }
        );

        getPublicGroups();

 //       populatePublicVessels(context);

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
        if(adTextArray!=null){
            AnimateText();
        }

        getPublicGroups();
    }

    @Override
    public void onPause(){
        super.onPause();
        runAd = false;
        mHandler.removeCallbacks(mStatusChecker);

    }

/*    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        atoll_menu = menu.findItem(R.id.action_atolls);
        if (SettingsPreferences.getCountry(context).equalsIgnoreCase("mv")) {
            atoll_menu.setVisible(true);
        } else {
            atoll_menu.setVisible(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //getMenuInflater().inflate(R.menu.public_menu,menu);

        inflater.inflate(R.menu.public_boat_menu, menu);
        System.out.println("onCreateOptionMenu............................................................................");
        //item_device = menu.findItem(R.id.action_new_device);
        publicAccountType = SettingsPreferences.getSelectedPublicAccount(context);



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
*/

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        System.out.println("onItemClick at PublicGroupFragment.......................");
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

        DataPublicGroups object = (DataPublicGroups) adapterView.getItemAtPosition(i);
        selected_item_id = String.valueOf(object.getId());
        SettingsPreferences.setSelectedItemID(context, selected_item_id);
        selected_item_name = object.getName();
        SettingsPreferences.setSelectedItemName(context,selected_item_name);
        PublicGroupFragment.selected_item_id = selected_item_id;
        PublicGroupFragment.selected_item_name = selected_item_name;
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


    public void getPublicGroups(){
        mGroupsTask = new GetPublicGroups();
        mGroupsTask.execute((Void) null);
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
                    populatePublicGroups(context); // to refresh the images when permission accepted
                } else {
                }
                break;

        }

    }

    public void populatePublicGroups(Context context){
//        System.out.println("Lets Populate");
        Database db = new Database(context);
        data = db.fetchPublicGroups();
       // lv.setAdapter(new PublicBoatsAdapter(context, data));
        lv.setAdapter(new PublicGroupAdapter(context, data, PublicGroupFragment.this));

    }


    public class GetPublicGroups extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            swipeLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);
            swipeLayout.setRefreshing(true);

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
                result = parser.GetPublicGroups(context);

                if (!TextUtils.isEmpty(result)) {
                    try {
                        JSONObject jObj = new JSONObject(result);
                        if (jObj.getString("status").equalsIgnoreCase("ok")) {
                            //    Time m = new Time();
                            //    m.setToNow();
                            //    long m1 = m.toMillis(true);
                            SettingsPreferences.setListPublicTime(context, System.currentTimeMillis());
                            //	errmsg = jObj.getString(TAG_ERROR_MSG);
                            AppUtils.savePublicGroups(jObj, context);

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

                        //populatePublicVessels(context);
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

                                        Toast.makeText(context, "No matchnig Public item found",Toast.LENGTH_LONG).show();

                                }
                                // Toast.makeText(context, "Public List is empty. Please goto Preferences and check your country", Toast.LENGTH_LONG).show();
                            }
                        }
                        populatePublicGroups(context);
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
