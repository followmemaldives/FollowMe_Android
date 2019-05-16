package com.asif.followme;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.asif.followme.model.DataMy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by user on 2/4/2018.
 */

public class mapFragment extends Fragment {
    public int mYear;
    public int mMonth;
    public int mDay;
    public int nMonth;
    public String tripLogDate;
//    private GetTripLogs mTripTask = null;
    ProgressDialog mDialog;
    ListView lv;
    TextView trip_error_label;
    public static List<DataMy> data = new ArrayList<DataMy>();
    Context context;

    public mapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.trip_fragment,container,false);
        lv= (ListView) rootView.findViewById(R.id.trip_list);
        trip_error_label = (TextView) rootView.findViewById(R.id.trip_notfound);
        trip_error_label.setVisibility(View.INVISIBLE);
        context = getActivity().getApplicationContext();
        tripLogDate = MyMapsActivity.tripLogDate;
        String[] d = tripLogDate.split("-");
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(d[0]), Integer.parseInt(d[1])-1, Integer.parseInt(d[2]));

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        nMonth= mMonth+1;
        //setTitle("Trip Logs");

 //       mTripTask = new GetTripLogs(tripLogDate);
 //       mTripTask.execute((Void) null);


        return rootView;
        //return inflater.inflate(R.layout.trip_list, container, false);


    }

/*    public class GetTripLogs extends AsyncTask<Void, Void, String> {
        String mDate;
        GetTripLogs(String mDate) {
            this.mDate = mDate;
        }
        @Override
        protected void onPreExecute() {
            System.out.println("+++++++++ Get Trip Logs +++++++++++++++++");
           // mDialog= ProgressDialog.show(getActivity().getApplicationContext(), null, "Loading...");
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... arg0) {
            try{
                ContentParser parser = new ContentParser(getActivity().getApplicationContext());
                return parser.getTripLogs(getActivity().getApplicationContext(), SettingsPreferences.getSelectedDevice(getActivity().getApplicationContext()),mDate);
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
            //if(mDialog.isShowing()){
            //    mDialog.dismiss();
            //}
            System.out.println(result);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        if (jObj.getString("status").equalsIgnoreCase("ok")) {
                            nMonth=mMonth+1;
 //                           setTitle("Trip Logs / "+mDay+"-"+nMonth+"-"+mYear);
                            //	errmsg = jObj.getString(TAG_ERROR_MSG);

                            trip_error_label.setVisibility(View.INVISIBLE);
                            //populateSharedUsers();
                            //lv=(ListView)findViewById(R.id.trip_list);
                            data = AppUtils.drawTripList(jObj, context);
                            lv.setAdapter(new tripAdapter(context, data));

                        } else {
                           // if(jObj.getJSONArray("aaData")==null){
                            //    data = AppUtils.drawTripList(jObj, context);
                            //    lv.setAdapter(new tripAdapter(context, data));

                            //}
                            trip_error_label.setVisibility(View.VISIBLE);
                            trip_error_label.setText(jObj.getString("error"));
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
    */

}
