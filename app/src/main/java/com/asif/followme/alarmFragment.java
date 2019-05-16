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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.model.DataLogs;
import com.asif.followme.util.AppUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by user on 2/4/2018.
 */

public class alarmFragment extends Fragment {
    public int mYear;
    public int mMonth;
    public int mDay;
    public int nMonth;
 //   public String tripLogDate;
    private GetAlarmLogs mAlarmTask = null;
    ProgressDialog mDialog;
    public static ListView lv;
    public static List<DataLogs> data = new ArrayList<DataLogs>();
    Context context;
    public static TextView alarm_error_label;
    boolean isAlarmsLoaded = false;
    static SwipeRefreshLayout swipeLayout2;



    public alarmFragment() {

        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.alarm_fragment,container,false);
        lv= (ListView) rootView.findViewById(R.id.alarm_list);
        alarm_error_label = (TextView) rootView.findViewById(R.id.alarm_notfound);
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
                        mAlarmTask = new alarmFragment.GetAlarmLogs(getActivity(), LogsActivity.filter_index,LogsActivity.date_from,LogsActivity.date_to,LogsActivity.sort_index);
                        mAlarmTask.execute();
                    }
                }
        );
        mAlarmTask = new alarmFragment.GetAlarmLogs(getActivity(), LogsActivity.filter_index,LogsActivity.date_from,LogsActivity.date_to,LogsActivity.sort_index);
        mAlarmTask.execute();

        //       tripLogDate = Integer.toString(mYear)+'-'+nMonth+'-'+mDay;

        return rootView;
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
    public class GetAlarmLogs extends AsyncTask<Void, Void, String> {
        int filter_index;
        String date_from;
        String date_to;
        int sort_index;
        final Context con;
        GetAlarmLogs(Context context, int filter_index,String date_from,String date_to,int sort_index) {
            this.con = context;
            this.filter_index=filter_index;
            this.date_from=date_from;
            this.date_to=date_to;
            this.sort_index=sort_index;
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
                return parser.getAlarmLogs(con,filter_index,date_from,date_to,sort_index);
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
                        if (jObj.getString("status").equalsIgnoreCase("ok")) {
                            alarm_error_label.setVisibility(View.INVISIBLE);
                            data = AppUtils.drawAlarmList(jObj, con);
                            lv.setAdapter(new alarmAdapter(con, data));
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
