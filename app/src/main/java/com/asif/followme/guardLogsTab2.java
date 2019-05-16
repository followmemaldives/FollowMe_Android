package com.asif.followme;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.model.DataSmartLogs;
import com.asif.followme.util.AppUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by user on 2/4/2018.
 */

public class guardLogsTab2 extends Fragment {
    public int mYear;
    public int mMonth;
    public int mDay;
    public int nMonth;
 //   public String tripLogDate;
    private GetSmartLogs mSmartTask = null;
    ProgressDialog mDialog;
    public static ListView lv;
    public static List<DataSmartLogs> data = new ArrayList<DataSmartLogs>();
    Context context;
    public static TextView alarm_error_label, header_date;
    boolean isAlarmsLoaded = false;
    static SwipeRefreshLayout swipeLayout2;
    private static LinearLayout headerLayout;
    private static final int MAX_POSTS = 11;
    private static TextView[] textViewArray = new TextView[MAX_POSTS];
    TextView h0,h1,h2,h3,h4,h5,h6,h7,h8,h9,h10;

//    String selected_date = "";



    public guardLogsTab2() {

        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.guard_logs_tab2,container,false);
        lv= (ListView) rootView.findViewById(R.id.alarm_list);
        alarm_error_label = (TextView) rootView.findViewById(R.id.alarm_notfound);
        header_date = (TextView) rootView.findViewById(R.id.header_date);
        headerLayout = (LinearLayout) rootView.findViewById(R.id.layout_log_header);
        h0 = (TextView) rootView.findViewById(R.id.h0);
        h1 = (TextView) rootView.findViewById(R.id.h1);
        h2 = (TextView) rootView.findViewById(R.id.h2);
        h3 = (TextView) rootView.findViewById(R.id.h3);
        h4 = (TextView) rootView.findViewById(R.id.h4);
        h5 = (TextView) rootView.findViewById(R.id.h5);
        h6 = (TextView) rootView.findViewById(R.id.h6);
        h7 = (TextView) rootView.findViewById(R.id.h7);
        h8 = (TextView) rootView.findViewById(R.id.h8);
        h9 = (TextView) rootView.findViewById(R.id.h9);
        h10 = (TextView) rootView.findViewById(R.id.h10);
        textViewArray[0] = h0;
        textViewArray[1] = h1;
        textViewArray[2] = h2;
        textViewArray[3] = h3;
        textViewArray[4] = h4;
        textViewArray[5] = h5;
        textViewArray[6] = h6;
        textViewArray[7] = h7;
        textViewArray[8] = h8;
        textViewArray[9] = h9;
        textViewArray[10] = h10;


        alarm_error_label.setVisibility(View.GONE);
        context = getActivity().getApplicationContext();
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        nMonth= mMonth+1;
        swipeLayout2 = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container2);
        swipeLayout2.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);
        swipeLayout2.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                       // GuardLogsActivity.tab2_date=selected_date;
                        mSmartTask = new GetSmartLogs(getActivity(), GuardLogsActivity.selected_date);
                        mSmartTask.execute();
                    }
                }
        );
        mSmartTask = new GetSmartLogs(getActivity(), GuardLogsActivity.selected_date);
        mSmartTask.execute();

        //       tripLogDate = Integer.toString(mYear)+'-'+nMonth+'-'+mDay;

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("OnStart.....................................");
       /* if(GuardLogsActivity.orientation != getResources().getConfiguration().orientation) {
            mSmartTask = new GetSmartLogs(getActivity(), GuardLogsActivity.selected_date);
            mSmartTask.execute();
        }
        */
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //MyMapsActivity.fab.setImageResource(R.drawable.ic_event_note_black_48dp);
        if (isVisibleToUser && !isAlarmsLoaded ) {
            //MyMapsActivity.fab.setVisibility(View.VISIBLE);

     //       mAlarmTask = new alarmFragment.GetAlarmLogs(context, tripLogDate);
    //        mAlarmTask.execute((Void) null);
            //loadLectures();
            //_areLecturesLoaded = true;
        }
    }
    public class GetSmartLogs extends AsyncTask<Void, Void, String> {
        String date_from;
        final Context con;
        GetSmartLogs(Context context, String date_from) {
            this.con = context;
            this.date_from=date_from;
        }
        @Override
        protected void onPreExecute() {
//            System.out.println("+++++++++ Get Trip Logs +++++++++++++++++");
//            mDialog= ProgressDialog.show(con, null, "Loading...");
            super.onPreExecute();
           swipeLayout2.setRefreshing(true);

        }

        @Override
        protected String doInBackground(Void... arg0) {
            try{
                ContentParser parser = new ContentParser(con);
                return parser.getSmartGuardLogs(con,date_from);
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
            swipeLayout2.setRefreshing(false);
//            if(mDialog.isShowing()){
//                mDialog.dismiss();
//            }
//            System.out.println(result);
            isAlarmsLoaded = true;
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        header_date.setText(GuardLogsActivity.selected_date);
                        if (jObj.getString("status").equalsIgnoreCase("ok")) {
                            alarm_error_label.setVisibility(View.GONE);
                            //data = AppUtils.drawGuardSmartList(jObj, con);
                            ArrayList<String> arr = new ArrayList<String>();
                            JSONArray titleObject = new JSONArray(jObj.getString("titles"));
                        //    headerLayout.removeAllViews();
                            for(int i = 0;i<titleObject.length();i++){
                                String kk = (String)titleObject.get(i);
                                System.out.println("I:"+i+",KK:"+kk);
                                if(i < MAX_POSTS) {
                                    textViewArray[i].setText(kk);
                                    textViewArray[i].setVisibility(View.VISIBLE);
                                }

                            }

                            //System.out.println(titleObject);
                           // ArrayList<String> arr_titles = new ArrayList<String>();
                            //arr_titles = jObj.getString("titles");
                            data = AppUtils.drawSmartLogs(jObj);
                            lv.setAdapter(new guardSmartAdapter(con, data));
                        } else {
                            alarm_error_label.setVisibility(View.VISIBLE);
                            alarm_error_label.setText(jObj.getString("error"));
                        }

                    } else {
                        Toast.makeText(con, "Please check your internet connection", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    //loading.setText("Unable to reach Server...");
                    Toast.makeText(con, "Please check your internet connection", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(con, "Please check your internet connection", Toast.LENGTH_LONG).show();

            }
        }
    }

}
