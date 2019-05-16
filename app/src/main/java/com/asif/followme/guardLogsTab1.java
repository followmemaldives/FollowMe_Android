package com.asif.followme;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.model.DataGuardLogs;
import com.asif.followme.util.AppUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by user on 2/4/2018.
 */

public class guardLogsTab1 extends Fragment {
    public int mYear;
    public int mMonth;
    public int mDay;
    public int nMonth;
    public String tripLogDate;
    public GetGuardLogs mGuardTask = null;
    private  ProgressDialog mDialog;
    public static ListView lv;
    public static TextView guard_error_label;
    public static List<DataGuardLogs> data = new ArrayList<DataGuardLogs>();
    Context context;
    FloatingActionButton tripFab;
    boolean isTripsLoaded = false;
    View shareView;
//    static LinearLayout tripListLayout;
    static SwipeRefreshLayout swipeLayout;



    public guardLogsTab1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.guard_logs_tab1,container,false);
        shareView = rootView;
//        tripListLayout = rootView.findViewById(R.id.trip_list_layout);
        lv= (ListView) rootView.findViewById(R.id.guard_list);
        guard_error_label = (TextView) rootView.findViewById(R.id.guard_notfound);
       guard_error_label.setVisibility(View.INVISIBLE);
        context = getActivity().getApplicationContext();
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container1);
        swipeLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);
        swipeLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mGuardTask = new GetGuardLogs(getActivity(), GuardLogsActivity.selected_date);
                        mGuardTask.execute();
                    }
                }
        );
        mGuardTask = new GetGuardLogs(getActivity(), GuardLogsActivity.selected_date);
        mGuardTask.execute();

        //      if(context == null){
 //           System.out.println("A Null Context at onCreateView ...........................");
 //       }

//        context = getContext();
//        context = getActivity().getApplicationContext();
//        context = this;


    //    tripLogDate = MyMapsActivity.tripLogDate;
    //    String[] d = tripLogDate.split("-");
    //    Calendar c = Calendar.getInstance();
    //    c.set(Integer.parseInt(d[0]), Integer.parseInt(d[1])-1, Integer.parseInt(d[2]));

//        tripFab = (FloatingActionButton) rootView.findViewById(R.id.fab_calendar);
//        tripFab.setVisibility(View.INVISIBLE);
//        tripFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {

                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //         .setAction("Action", null).show();
//            }
//        });


//        getActivity().setTitle("Trip Logs");
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        nMonth= mMonth+1;
//        tripLogDate = Integer.toString(mYear)+'-'+nMonth+'-'+mDay;
//        System.out.println("tripFragment Visible: Date:"+tripLogDate);
//        mTripTask = new GetTripLogs(getActivity(),0,"","",tripLogDate);
//        mTripTask = new GetTripLogs(getActivity(),0,"","",0);
//        mTripTask.execute();


        return rootView;
        //return inflater.inflate(R.layout.trip_list, container, false);


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //MyMapsActivity.fab.setImageResource(R.drawable.ic_event_note_black_48dp);
        if (isVisibleToUser && !isTripsLoaded ) {
            //MyMapsActivity.fab.setVisibility(View.VISIBLE);
            //loadLectures();
            //_areLecturesLoaded = true;
        }
    }


    public class GetGuardLogs extends AsyncTask<Void, Void, String> {
        //String mDate;
        String date;
        final Context con;
        GetGuardLogs(Context context, String date) {
            this.date=date;
             con = context;
        }
        @Override
        protected void onPreExecute() {
            swipeLayout.setRefreshing(true);
//            System.out.println("+++++++++ Get Trip Logs for "+mDate+" +++++++++++++++++");
//            mDialog= ProgressDialog.show(con, null, "Loading...");
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... arg0) {
                ContentParser parser = new ContentParser(con);
                return parser.getGuardLogs(con,date);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            swipeLayout.setRefreshing(false);
//            if(mDialog.isShowing()){
//                mDialog.dismiss();
//            }
            isTripsLoaded = true;
//            System.out.println(result);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        if (jObj.getString("status").equalsIgnoreCase("ok")) {
                            nMonth=mMonth+1;
                         //   getActivity().setTitle("Trip Logs / "+mDay+"-"+nMonth+"-"+mYear);
                            //	errmsg = jObj.getString(TAG_ERROR_MSG);

                            guard_error_label.setVisibility(View.INVISIBLE);
                            //populateSharedUsers();
                            //lv=(ListView)findViewById(R.id.trip_list);
                            data = AppUtils.drawGuardList(jObj, con);
                            lv.setAdapter(new guardAdapter(con, data));

                        } else {
                           // if(jObj.getJSONArray("aaData")==null){
                            //    data = AppUtils.drawTripList(jObj, context);
                            //    lv.setAdapter(new tripAdapter(context, data));

                            //}
                            data = AppUtils.drawGuardList(jObj, con);
                            lv.setAdapter(new guardAdapter(con, data));
                            guard_error_label.setVisibility(View.VISIBLE);
                            guard_error_label.setText(jObj.getString("error"));
                            //Toast.makeText(context, jObj.getString("error"), Toast.LENGTH_LONG).show();
                           // AppUtils.showAlertDialog(context, "Error", jObj.getString("error"));
                        }


                    } else {
                        Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    //loading.setText("Unable to reach Server...");
                    Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_LONG).show();

            }
        }
    }

    public static void shareTripImage(Context con){
        //View shareLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_trip, null);
        //LinearLayout contentView = (LinearLayout) findViewById(R.id.trip_list_layout);
        // LinearLayout contentView = shareLayout.findViewById(R.id.trip_list_layout);
        try {
           // Bitmap bitmap = getBitmapFromView(tripListLayout);
            Bitmap bitmap = getWholeListViewItemsToBitmap();
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, getImageUri(con, bitmap));
            shareIntent.setType("image/jpeg");
            con.startActivity(Intent.createChooser(shareIntent, "Share"));

        }catch (Exception e){
            e.getMessage();
        }

    }
    public static Bitmap getWholeListViewItemsToBitmap() {

        ListView listview    = lv;
        ListAdapter adapter  = listview.getAdapter();
        int itemscount       = adapter.getCount();
        int allitemsheight   = 0;
        List<Bitmap> bmps    = new ArrayList<Bitmap>();

        for (int i = 0; i < itemscount; i++) {

            View childView      = adapter.getView(i, null, listview);
            childView.measure(View.MeasureSpec.makeMeasureSpec(listview.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
            childView.setDrawingCacheEnabled(true);
            childView.buildDrawingCache();
            bmps.add(childView.getDrawingCache());
            allitemsheight+=childView.getMeasuredHeight();
        }

        Bitmap bigbitmap    = Bitmap.createBitmap(listview.getWidth(), allitemsheight, Bitmap.Config.ARGB_8888);
        Canvas bigcanvas    = new Canvas(bigbitmap);
        bigcanvas.drawColor(Color.WHITE);

        Paint paint = new Paint();
        int iHeight = 0;

        for (int i = 0; i < bmps.size(); i++) {
            Bitmap bmp = bmps.get(i);
            bigcanvas.drawBitmap(bmp, 0, iHeight, paint);
            iHeight+=bmp.getHeight();

            bmp.recycle();
            bmp=null;
        }


        return bigbitmap;
    }

    private static Bitmap getBitmapFromView(LinearLayout view) {

            Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(returnedBitmap);
            Drawable bgDrawable = view.getBackground();
            if (bgDrawable != null)
                bgDrawable.draw(canvas);
            else
                canvas.drawColor(Color.WHITE);
            view.draw(canvas);
            return returnedBitmap;
    }
    private static Bitmap getBitmapFromView2(LinearLayout view) {
        try {

            view.setDrawingCacheEnabled(true);

            view.measure(View.MeasureSpec.makeMeasureSpec(800, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(600, View.MeasureSpec.UNSPECIFIED));
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

            view.buildDrawingCache(true);
            Bitmap returnedBitmap = Bitmap.createBitmap(view.getDrawingCache());

            //Define a bitmap with the same size as the view
            view.setDrawingCacheEnabled(false);

            return returnedBitmap;
        }catch (Exception e){
            e.printStackTrace();
            //Global.logError("getBitmapFromView", e);
        }
        return null;
    }
    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                    inImage, "", "");
            return Uri.parse(path);
        }catch (Exception e){
            e.getMessage();
        }
        return null;
    }

}
