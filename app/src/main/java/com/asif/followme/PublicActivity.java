package com.asif.followme;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.asif.followme.Flight.AirportFragment;
import com.asif.followme.PublicBoats.PublicBoatsFragment;
import com.asif.followme.PublicBoats.PublicGroupFragment;
import com.asif.followme.PublicBoats.PublicPreferences;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.util.AppUtils;
import com.asif.followme.util.SettingsPreferences;
public class PublicActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//    static ListView lv;
    Context context;
    SwipeRefreshLayout swipeLayout;
//    private AsyncTask<Void, Void, String> VesselAsyncTask;
//    public static String selected_vessel_id,selected_vessel_name,selectedImage;
    static Parcelable state;
    public static Parcelable state_boat_list,state_group_list,state_fav_list;
    public int publicAccountType;
    private MenuItem atoll_menu;
//    public static String selected_item_name, selected_item_id;
    static Activity activity;
    public String NavigationName;
    final int NAV_PUBLIC_BOAT_INDEX = 0;
    final int NAV_PUBLIC_GROUP_INDEX = 1;
    final int NAV_PUBLIC_FAV_INDEX = 2;
    final int NAV_FLIGHT_SCHEDULE_INDEX = 3;
    public static int NavigationIndex = 0;
    final String PUBLIC_LIST_TITLE = "Public List";
    final String PUBLIC_FAVOURITE_TITLE = "My Favourites";
    final String PUBLIC_GROUP_TITLE = "Public Groups";
    final String AIRPORT_LIST_TITLE = "Flight Schedule";
    int selected_atoll =0;

//    private TextView PublicErrorLabel;

//    public static List<DataPublicBoats> resultDB = new ArrayList<DataPublicBoats>();

//    public int adTextCounter;
//    public boolean runAd;
    private NavigationView navigationView;
    public static long public_refresh_time = 0;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;

//        setTitle(SettingsPreferences.getPublicTitle(context));
       // if(SettingsPreferences.getSelectedAtoll(context) > 0)
            //setTitle("Atoll");
       // }

        //publicMenu = (Menu) findViewById(R.menu.public_menu);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        NavigationName = SettingsPreferences.getPublicNavigationName(context);
        Fragment newFragment = null;
        System.out.println("Navigatioin Name:"+NavigationName+" +++++++++++++++++++++++++++++++++++++++++");
        String title = PUBLIC_LIST_TITLE;
        switch (NavigationName){
            case "nav_public_group":
                NavigationIndex = NAV_PUBLIC_GROUP_INDEX;
                newFragment = new PublicGroupFragment();
                //title = PUBLIC_GROUP_TITLE;
                //setTitle(PUBLIC_GROUP_TITLE);
                //SettingsPreferences.setPublicTitle(context,title);

                break;
            case "nav_public_fav":
                NavigationIndex = NAV_PUBLIC_FAV_INDEX;
                newFragment = new PublicBoatsFragment();
                title = PUBLIC_FAVOURITE_TITLE;
                setTitle(PUBLIC_FAVOURITE_TITLE);
                //SettingsPreferences.setPublicTitle(context,title);

                break;
            case "nav_flight_schedule":
                NavigationIndex = NAV_FLIGHT_SCHEDULE_INDEX;
                newFragment = new AirportFragment();
                //setTitle(AIRPORT_LIST_TITLE);
                //title = AIRPORT_LIST_TITLE;
                //SettingsPreferences.setPublicTitle(context,title);
                break;

            default:    //nav_public_list
                NavigationIndex = NAV_PUBLIC_BOAT_INDEX;
                newFragment = new PublicBoatsFragment();
                //setTitle(SettingsPreferences.getPublicTitle(context));
//                setTitle(PUBLIC_LIST_TITLE);
                break;
        }
        //setTitle(title);

        navigationView.getMenu().getItem(NavigationIndex).setChecked(true);
        //newFragment = new MyBoatsFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container,newFragment);
        //   transaction.addToBackStack(null);
        transaction.commit();

        //PublicErrorLabel = (TextView) findViewById(R.id.public_notfound);
        //PublicErrorLabel.setVisibility(View.INVISIBLE);


 /*       swipeLayout.setColorSchemeColors(android.R.color.holo_blue_bright android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
              */


 //       lv=(ListView) findViewById(R.id.list);
        this.activity = this;





        String[] PERMISSIONS = {android.Manifest.permission.INTERNET, android.Manifest.permission.ACCESS_NETWORK_STATE, android.Manifest.permission.WAKE_LOCK, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE};
        if(!AppUtils.hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }


    }

    @Override
    public void onStart() {
        super.onStart();
     //    SettingsPreferences.setAccount(context, "public");
        publicAccountType = SettingsPreferences.getSelectedPublicAccount(context);

/*        long m1 = System.currentTimeMillis();
        long m2=SettingsPreferences.getListPublicTime(context);
        if(m1-m2 > 30000){
            state=null;
            getPublicVessels(SettingsPreferences.getSelectedAtoll(context), "");
        }
       */
        //populatePublicVessels(context);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void onPause(){
        System.out.println("onPause...................................................................");
//        state = lv.onSaveInstanceState();

        super.onPause();
        //mStatusChecker.
    }
/*
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        atoll_menu = menu.findItem(R.id.action_atolls);
        if(publicAccountType==0) {
            MenuItem sitem = menu.findItem(R.id.action_search);
            menu.setGroupVisible(0, true);
            //menu.getItem(0).setVisible(false);
            sitem.setVisible(false);
            if (SettingsPreferences.getCountry(context).equalsIgnoreCase("mv")) {
                atoll_menu.setVisible(true);
            } else {
                atoll_menu.setVisible(false);
            }
        } else {
            atoll_menu.setVisible(false);
            menu.setGroupVisible(0,false);

        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.public_menu,menu);

       // Inflate the menu; this adds items to the action bar if it is present.
        publicAccountType = SettingsPreferences.getSelectedPublicAccount(context);
        if(publicAccountType == 0) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            //SearchView searchView = new SearchView(context);
            searchView.setQueryHint(getString(R.string.search_hint));
            //searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

            // Assumes current activity is the searchable activity
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
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
        if(publicAccountType == 2){ //atoll
            //getMenuInflater().inflate(R.menu.public_atoll_menu,menu);
            //final MenuItem menuItem = menu.add("Atoll");
            //menuItem.setTitle("Atoll");
            //menuItem.setIcon(R.drawable.ic_action_add_person);
            //menuItem.setActionView(searchView);
           // menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        }

        return true;
    }

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
//            setTitle("Haa Alif");
            selected_atoll = 1;
            public_title="Haa Alif";
//            getPublicVessels(1, "");
        }
        if(id == R.id.atoll_hd){
//            setTitle("Haa Dhaalu");
            selected_atoll = 2;
            public_title="Haa Dhaalu";
//            getPublicVessels(2, "");
        }
        if(id == R.id.atoll_sh){
//            setTitle("Shaviyani");
            selected_atoll = 3;
            public_title="Shaviyani";
//            getPublicVessels(3, "");
        }
        if(id == R.id.atoll_n){
//            setTitle("Noonu");
            selected_atoll = 4;
            public_title="Noonu";
//            getPublicVessels(4, "");
        }
        if(id == R.id.atoll_r){
//            setTitle("Raa");
//            getPublicVessels( 5, "");
            selected_atoll = 5;
            public_title="Raa";
        }
        if(id == R.id.atoll_b){
//            setTitle("Baa");
            selected_atoll = 6;
            public_title="Baa";
//            getPublicVessels(6, "");
        }
        if(id == R.id.atoll_lh){
//            setTitle("Lhaviyani");
            selected_atoll = 7;
            public_title="Lhaviyani";
//            getPublicVessels(7, "");
        }
        if(id == R.id.atoll_k){
 //           setTitle("Kaafu");
            selected_atoll = 8;
            public_title="Kaafu";
//            getPublicVessels(8, "");
        }
        if(id == R.id.atoll_aa){
//            setTitle("Alif Alif");
            selected_atoll = 9;
            public_title="Alif Alif";
//            getPublicVessels(9, "");
        }
        if(id == R.id.atoll_ad){
//            setTitle("Alif Dhaalu");
            selected_atoll = 10;
            public_title="Alif Dhaalu";
//            getPublicVessels(10, "");
        }
        if(id == R.id.atoll_v){
 //           setTitle("Vaavu");
            selected_atoll = 11;
            public_title="Vaavu";
//            getPublicVessels(11, "");
        }
        if(id == R.id.atoll_m){
   //         setTitle("Meemu");

            selected_atoll = 12;
            public_title="Meemu";
//            getPublicVessels(12, "");
        }
        if(id == R.id.atoll_f){
//            setTitle("Faafu");
            selected_atoll = 13;
            public_title="Faafu";
//            getPublicVessels(13, "");
        }
        if(id == R.id.atoll_d){
//            setTitle("Dhaalu");
            selected_atoll = 14;
            public_title="Dhaalu";
//            getPublicVessels(14, "");
        }
        if(id == R.id.atoll_t){
//            setTitle("Thaa");
            selected_atoll = 15;
            public_title="Thaa";
//            getPublicVessels(15, "");
        }
        if(id == R.id.atoll_l){
 //           setTitle("Laamu");
            selected_atoll = 16;
            public_title="Laamu";
//            getPublicVessels(16, "");
        }
        if(id == R.id.atoll_ga){
   //         setTitle("Gaafu Alif");
            selected_atoll = 17;
            public_title="Gaafu Alif";
//            getPublicVessels(17, "");
        }
        if(id == R.id.atoll_gd){
//            setTitle("Gaafu Dhaalu");
            selected_atoll = 18;
            public_title="Gaafu Dhaal";
//            getPublicVessels(18, "");
        }
        if(id == R.id.atoll_gn){
  //          setTitle("Gnaviyani");
            selected_atoll = 19;
            public_title="Fuvahmulah";
//            getPublicVessels(19, "");
        }
        if(id == R.id.atoll_s){
 //           setTitle("Seenu");
            selected_atoll = 20;
            public_title="Seenu";
//            getPublicVessels(20, "");
        }
        if(id == R.id.atoll_male){
 //           setTitle("Male City");
            selected_atoll = 21;
            public_title="Male City";
//            getPublicVessels(21, "");
        }
        setTitle(public_title);
        SettingsPreferences.setSelectedAtoll(context, selected_atoll);
        SettingsPreferences.setPublicTitle(context,public_title);
        getPublicVessels(selected_atoll,"");

        return super.onOptionsItemSelected(item);
    }
*/
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        //System.out.println("Request Code:"+requestCode);
        //System.out.println("Result Code:"+resultCode);
        //System.out.println("Data:"+data);
        switch(requestCode) {
            case (3):{  //return from public pref
                if (resultCode == Activity.RESULT_OK) {
                    if(publicAccountType==0) {
//                        setTitle("Public List");
                        if (SettingsPreferences.getCountry(context).equalsIgnoreCase("mv")) {
                            atoll_menu.setVisible(true);
                        } else {
                            atoll_menu.setVisible(false);
                        }
                        getPublicVessels(SettingsPreferences.getSelectedAtoll(context), "");

                    } else {
                        atoll_menu.setVisible(false);
                    }


                }
                break;
            }
            case (7):{ //5
                if (resultCode == Activity.RESULT_OK) {

                }
            }
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
            case (8):{ // return from Feedback
                if (resultCode == Activity.RESULT_OK) {
                    final String action = data.getStringExtra("action");
                    if(action.equalsIgnoreCase("feedback_ok")){
                        AppUtils.showAlertDialog(context,"Feedback Received","Thank you for your time on providing valuable feedback, relating to our service.\n Thank you ");
                    }
                }
            }

        }
    }
*/


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        Class fragmentClass = null;
        int id = item.getItemId();
        String nav_name = getResources().getResourceEntryName(id);
        SettingsPreferences.setPublicNavigationName(context,nav_name);
        PublicBoatsFragment.runAd = false;
        PublicGroupFragment.runAd = false;

        if (id == R.id.nav_public_list) {
//            invalidateOptionsMenu();
            //publicMenu.setGroupVisible(0,true);
            // publicMenu.getItem(1).setVisible(false);
            public_refresh_time=0;
            publicAccountType=0;
            NavigationIndex = NAV_PUBLIC_BOAT_INDEX;
            public_refresh_time = 0;
            SettingsPreferences.setSelectedPublicAccount(context, publicAccountType);
            fragmentClass = PublicBoatsFragment.class;
        } else if (id == R.id.nav_public_fav){
            public_refresh_time=0;
            publicAccountType = 2;
            NavigationIndex = NAV_PUBLIC_FAV_INDEX;
            invalidateOptionsMenu();
            SettingsPreferences.setSelectedPublicAccount(context, publicAccountType);
            setTitle(PUBLIC_FAVOURITE_TITLE);
//          getPublicVessels(SettingsPreferences.getSelectedAtoll(context), "");
            fragmentClass = PublicBoatsFragment.class;

        } else if (id == R.id.nav_public_group){
            publicAccountType = 1;
            invalidateOptionsMenu();
            //publicMenu.setGroupVisible(0,false);
            NavigationIndex = NAV_PUBLIC_GROUP_INDEX;
            SettingsPreferences.setSelectedPublicAccount(context, publicAccountType);
//            setTitle(PUBLIC_GROUP_TITLE);
//            getPublicVessels(SettingsPreferences.getSelectedAtoll(context), "");
            // } else if (id == R.id.nav_public_atoll) {
            //     publicAccountType = 2;
            //    SettingsPreferences.setSelectedPublicAccount(context,2);
            //    getPublicVessels(publicAccountType, SettingsPreferences.getSelectedAtoll(context), "");
            fragmentClass = PublicGroupFragment.class;
        } else if(id == R.id.nav_flight_schedule){
            NavigationIndex = NAV_FLIGHT_SCHEDULE_INDEX;
            SettingsPreferences.setMyNavigationName(context,nav_name);
            fragmentClass = AirportFragment.class;

        } else if (id == R.id.nav_manage) {
                Intent intent = new Intent(context, PublicPreferences.class);
                startActivityForResult(intent,3);

            //startActivity(intent);

        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.asif.followme");
            startActivity(Intent.createChooser(intent, "Share"));

        } else if (id == R.id.nav_feedback) {
            Intent intent	=	new Intent(context, FeedbackActivity.class);
            activity.startActivityForResult(intent,8);

        }  else if(id == R.id.nav_buy){
            Intent intent = new Intent(context, BuyActivity.class);
            startActivity(intent);
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

/*    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3){
        DataPublicBoats object = (DataPublicBoats) arg0.getItemAtPosition(arg2);
 //       SettingsPreferences.setSelectedDeviceName(context, object.getName());
        SettingsPreferences.setSelectedItemID(context, object.getId());
        selected_item_id = object.getId();
        selected_item_name = object.getName();
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        if (resultCode == ConnectionResult.SUCCESS){
            Intent intent = new Intent(PublicActivity.this, PublicMapsActivity.class);
            intent.putExtra("title",selected_item_name);
            startActivity(intent);
            finish();
            //		Toast.makeText(getApplicationContext(),"isGooglePlayServicesAvailable SUCCESS",Toast.LENGTH_LONG).show();
        } else {
            //GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, requestCode);
            dialog.show();
        }
    }

*/
/*
    public static void populatePublicVessels(Context context){
        System.out.println("PopulatePublicVessels");
        Database db = new Database(context);
        resultDB = db.fetchallPublicdata();
//        System.out.println(resultDB.get(0).getId());
        lv.setAdapter(new PublicBoatsAdapter(context, resultDB));
        lv.setOnItemClickListener((AdapterView.OnItemClickListener) context);

        if(state != null) {
            //Log.d(TAG, "trying to restore listview state..");
           lv.onRestoreInstanceState(state);
        }
    }
*/



    public static void updateFavourites(final Context context,final String device_id,final int fav) {

        class FavTask extends AsyncTask<Void, Void, String>    {
            @Override
            protected void onPreExecute() {

//			Dialog=ProgressDialog.show(context, null, "Loading Notice, Please wait...");
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... arg0) {
                try{
                    //	System.out.println("Device id:"+GlobalData.getItemId(context));
                    ContentParser parser = new ContentParser(context);
                    return parser.updateFavourite(context,device_id,fav);
                } catch(Exception ex) {
                    //finish();
//				System.out.println("err??????????????????????");
                }
                return null;
//			fetchDataFromJSON2();
//			return null;

            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                //System.out.println("This:"+result);
                //		if(Dialog.isShowing()){
                //			Dialog.dismiss();
                //		}

            }
        }
        FavTask favTask=new FavTask();
        favTask.execute();

    }
/*    public static void selectMenuItem(int acc) {
        Intent intent;
        if(acc == 0 || acc == 2){   //public list || favourite list
            intent = new Intent(activity, PublicMenuActivity.class);
            intent.putExtra("vname",selected_item_name);
            activity.startActivityForResult(intent,5);
        } else {    //group menu
            intent = new Intent(activity, MyGroupMenuActivity.class);
            intent.putExtra("vname",selected_item_name);
            activity.startActivityForResult(intent,6);
        }
    }
*/
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //populatePublicVessels(context); // to refresh the images when permission accepted
                } else {
                }
                break;
        }
    }


}
