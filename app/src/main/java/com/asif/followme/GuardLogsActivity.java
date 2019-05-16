package com.asif.followme;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import com.asif.followme.MyAccount.MyActivity;
import com.asif.followme.MyAccount.MyBoatsFragment;
import com.asif.followme.util.SettingsPreferences;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GuardLogsActivity extends AppCompatActivity{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    //private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    FloatingActionButton fabCalendar;
    Context context;
    public int mYear;
    public int mMonth;
    public int mDay;
    public int nMonth;
    public String dateAction;
    private SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");


    public static String selected_date="";
    public static String tab2_date="",tab1_date ="";
    public static int orientation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guard_logs_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    //    Date d = Calendar.getInstance().getTime();
    //    String dispDate = df.format(d);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(MyActivity.selected_item_name);
        context = this;

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        //mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        //mViewPager = (ViewPager) findViewById(R.id.container);
        //mViewPager.setAdapter(mSectionsPagerAdapter);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.view_container);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
   //     tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
   //     tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addOnTabSelectedListener(onTabSelectedListener(viewPager));
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        nMonth= mMonth+1;
        selected_date = Integer.toString(mYear)+'-'+nMonth+'-'+mDay;


        fabCalendar = (FloatingActionButton) findViewById(R.id.fab_calendar);
       // fab.setVisibility(View.INVISIBLE);
        fabCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(GuardLogsActivity.this, new mDateSetListener(), mYear, mMonth, mDay);
            //    fab.setVisibility(View.INVISIBLE);
                Intent intent;
                switch(tabLayout.getSelectedTabPosition()){
                    case 0:
                        dialog.show();
                        break;
                    case 1:
                        dialog.show();
                        break;
                    default:
                     //   System.out.println("Tab 0 flloat clicked");

                }

                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //         .setAction("Action", null).show();
            }
        });


    }
    @Override
    public void onStart() {
        super.onStart();
        tab1_date = selected_date;
        orientation = getResources().getConfiguration().orientation;
        //Lets initialize to default, to avoid un-necessary big queries from previous filter

    }
    private TabLayout.OnTabSelectedListener onTabSelectedListener(final ViewPager viewPager) {

        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            //    viewPager.setCurrentItem(tab.getPosition());
                System.out.println("Tab Changed: "+tab.getPosition());
                if(tab.getPosition()==0){
                    if(!selected_date.equals(tab1_date)) {
                        tab1_date=selected_date;
                        guardLogsTab1 lf1 = new guardLogsTab1();
                        guardLogsTab1.GetGuardLogs alarmTask = lf1.new GetGuardLogs(context, selected_date);
                        alarmTask.execute();
                    }
                }
                if(tab.getPosition()==1){
                    if(!selected_date.equals(tab2_date)) {
                        tab2_date=selected_date;
                        guardLogsTab2 lf = new guardLogsTab2();
                        guardLogsTab2.GetSmartLogs alarmTask = lf.new GetSmartLogs(context, selected_date);
                        alarmTask.execute();
                    }

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
    }

    class mDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            int m=mMonth+1;
            Intent intent;
            // getCalender();
            selected_date = Integer.toString(mYear)+'-'+m+'-'+mDay;
//            String tripDispDate = mDay+"-"+m+"-"+Integer.toString(mYear);
            getSupportActionBar().setTitle(MyBoatsFragment.selected_item_name);
//            System.out.println("DateAction: "+dateAction+"*******************************************************");
            switch (tabLayout.getSelectedTabPosition()){
                case 0:
                    //   intent		=	new Intent(context, Get_trip_list.class);
                    //   startActivity(intent);
                    System.out.println("Log Date:"+selected_date);
                    tab1_date = selected_date;
                    // tripFragment.GetTripLogs tripTask = new tripFragment.GetTripLogs(context, tripLogDate);
                    guardLogsTab1 fragment = new guardLogsTab1();
                    guardLogsTab1.GetGuardLogs alarmTask = fragment.new GetGuardLogs(context,selected_date);
                    alarmTask.execute();
                    break;
                case 1:
                    System.out.println("Lets Run...");
                    System.out.println("Trip Date:"+selected_date);
                    tab2_date=selected_date;
                   // tripFragment.GetTripLogs tripTask = new tripFragment.GetTripLogs(context, tripLogDate);
                    guardLogsTab2 fragment2 = new guardLogsTab2();
                    guardLogsTab2.GetSmartLogs smartTask = fragment2.new GetSmartLogs(context,selected_date);
                    smartTask.execute();

                    break;

            }
            //      v.setText(new StringBuilder()
            // Month is 0 based so add 1
            //             .append(mMonth + 1).append("/").append(mDay).append("/")
            //            .append(mYear).append(" "));
            //   System.out.println(v.getText().toString());

//            GetTripLogs(mDate);


        }
        public void onShow(){
 //           System.out.println("Showing");
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new guardLogsTab1(), "LOG LIST");
        adapter.addFragment(new guardLogsTab2(), "LOG TABLE");
        viewPager.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_share_text) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String shareData="";
//          intent.putExtra(Intent.EXTRA_TEXT, "Guard Logs of "+ SettingsPreferences.getSelectedItemName(context)+"\nDate:  "+mDay+"-"+nMonth+"-"+mYear+"\n----------------------------------------\n"+tripAdapter.getFleetShareData());
            if(tabLayout.getSelectedTabPosition()==0){
                shareData = guardAdapter.getShareData();
            } else {
                //shareData = guardSmartAdapter.getShareData();
            }
            intent.putExtra(Intent.EXTRA_TEXT, SettingsPreferences.getSelectedItemName(context)+"\n----------------------------------------\n"+shareData);
            startActivity(Intent.createChooser(intent, "Share"));

            return true;
        }
        if (id == R.id.action_share_image) {
            tripFragment.shareTripImage(context);

/*          Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
//          intent.putExtra(Intent.EXTRA_TEXT, "Trip Logs of "+ SettingsPreferences.getSelectedItemName(context)+"\nDate:  "+mDay+"-"+nMonth+"-"+mYear+"\n----------------------------------------\n"+tripAdapter.getFleetShareData());
            intent.putExtra(Intent.EXTRA_TEXT, SettingsPreferences.getSelectedItemName(context)+"\n----------------------------------------\n"+tripAdapter.getShareData());
            startActivity(Intent.createChooser(intent, "Share"));
*/
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /*
    /**
     * A placeholder fragment containing a simple view.
     */
//    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
/*        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }
*/
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
 /*       public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_logs, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
*/
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
/*    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
*/
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }


    public void shareScreen(){

        View screenView = findViewById(R.id.trip_list);
        Bitmap bitmap = screenShot(screenView);
        OutputStream fout = null;
        String filePath = System.currentTimeMillis() + ".jpeg";
        try
        {
            fout = openFileOutput(filePath,context.MODE_PRIVATE);

            // Write the string to the file
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
//                  Rbitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
            fout.flush();
            fout.close();
        }
        catch (FileNotFoundException e)
        {
            Log.d("ImageCapture", "FileNotFoundException");
            Log.d("ImageCapture", e.getMessage());
            filePath = "";
        }
        catch (IOException e)
        {
            Log.d("ImageCapture", "IOException");
            Log.d("ImageCapture", e.getMessage());
            filePath = "";
        }

        openShareImageDialog(filePath);


    }

    public static Bitmap loadBitmapFromView(View v) {
        int width = 100;
        int height = 200;
        Bitmap b = Bitmap.createBitmap(width , height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }
    public void openShareImageDialog(String filePath)
    {
        File file = this.getFileStreamPath(filePath);

        if(!filePath.equals(""))
        {
            final ContentValues values = new ContentValues(1);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            final Uri contentUriFile = getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            //sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            sendIntent.putExtra(Intent.EXTRA_STREAM, contentUriFile);
            sendIntent.setType("image/jpeg");
            startActivity(Intent.createChooser(sendIntent, "Share DDD"));
        //    startActivity(sendIntent);

        //    final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        //    intent.setType("image/jpeg");
         //   intent.putExtra(android.content.Intent.EXTRA_STREAM, contentUriFile);
         //   startActivity(Intent.createChooser(intent, "Share Image"));
        }
        else
        {
            //This is a custom class I use to show dialogs...simply replace this with whatever you want to show an error message, Toast, etc.
            //DialogUtilities.showOkDialogWithText(this, R.string.shareImageFailed);
        }
    }



/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch(requestCode) {
                case (23): {  //return from Trip  Log
                    date_from = data.getStringExtra("date_from");
//                    System.out.println("Filter Index:"+filter_index+",From:"+date_from+",Date To:"+date_to+",Sort:"+sort_index);
                    guardLogsTab1 tf = new guardLogsTab1();
//                    tripFragment.GetTripLogs tripTask = tf.new GetTripLogs(LogsActivity.this,tripLogDate);
                    guardLogsTab1.GetGuardLogs guardTask = tf.new GetGuardLogs(context,date_from);
                    guardTask.execute();
                }
                break;
                case 24:{   //alarm log
                    date_from = data.getStringExtra("date_from");
//                    System.out.println("Filter Index:"+filter_index+",From:"+date_from+",Date To:"+date_to+",Sort:"+sort_index);
                    guardLogsTab2 lf = new guardLogsTab2();
                    guardLogsTab2.GetSmartLogs alarmTask = lf.new GetSmartLogs(context,date_from);
                    alarmTask.execute();
                }
                break;
            }
        }
    }
*/
}
