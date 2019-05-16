package com.asif.followme.TripPlan;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.*;
import com.asif.followme.R;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.tripFragment;
import com.asif.followme.util.AppUtils;
import com.asif.followme.util.SettingsPreferences;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TripPlanFragment extends Fragment implements AdapterView.OnItemClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    public static ListView lv;
    private GetTripPlans tripPlanTask = null;
    private ChangeTripPlan changeTripPlanTask = null;
    private LinearLayout errorLayout;
    private TextView errorTextView;
    static SwipeRefreshLayout swipeLayout;
    int HireState = 0;
    static int HireNav = 0;
    private static Context context, context2;
    public static List<DataTripPlans> data = new ArrayList<DataTripPlans>();
    private static Activity activity;
    public static String selected_item_name;
    public static int selected_item_id;
    Calendar date;

    private SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

    public static int filter_index=0;
    public static int sort_index = 0;
    public static String date_from="";
    public static String date_to="";
    public int mYear;
    public int mMonth;
    public int mDay;
    public int nMonth;
    private String selected_date ="";
    private int device_id = 0;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TripPlanFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TripPlanFragment newInstance(int columnCount) {
        TripPlanFragment fragment = new TripPlanFragment();
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
        View view = inflater.inflate(R.layout.trip_plan_fragment, container, false);
        setHasOptionsMenu(true);
        lv= (ListView) view.findViewById(R.id.trip_plan_list);
        lv.setOnItemClickListener(this);
//        lv.setOnClickListener((AdapterView.OnClickListener) context);
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        nMonth= mMonth+1;


//        context = getActivity().getApplicationContext();
        getActivity().setTitle("Trip Plans");
        errorLayout = (LinearLayout) view.findViewById(R.id.error_layout);
        errorTextView = (TextView) view.findViewById(R.id.error_text_view);
        context = view.getContext();
        activity = getActivity();
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);
        swipeLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        tripPlanTask = new GetTripPlans(filter_index,selected_date, date_to,device_id);
                        tripPlanTask.execute((Void) null);
                    }
                }
        );


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

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        String[] mString = SettingsPreferences.getTripPlanFilter(context);
        filter_index = Integer.parseInt(mString[0]);
        date_from = mString[1];
        date_to = mString[2];
        tripPlanTask = new GetTripPlans(filter_index,selected_date, date_to,device_id);
        tripPlanTask.execute((Void) null);
        //    mHireTask = new GetPublicBoatHires(HireState, HireNav);
        //    mHireTask.execute((Void) null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.trip_plan_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_new_plan){
            Intent intent;
            intent = new Intent(context, NewTripForm.class);
            intent.putExtra("trip_id",0);
            startActivityForResult(intent, 4);
            //startActivity(intent);
            //return true;
        }
        if(id == R.id.action_calendar_trip){
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), new mDateSetListener(), mYear, mMonth, mDay);
            dialog.show();

        }
        if(id == R.id.action_template){
            Intent intent = new Intent(context, TemplateList.class);
            startActivity(intent);
            return true;

        }
        if(id == R.id.action_help){
            Intent intent = new Intent(context, TripHelpActivity.class);
            startActivity(intent);
            return true;

        }
/*        if(id == R.id.action_calendar_tripxxx){
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), new mDateSetListener(), mYear, mMonth, mDay);
            Intent intent;
            intent = new Intent(context, TripFilterForm.class);
            intent.putExtra("action","tripFilter");
            startActivityForResult(intent,23);


        }
        */
        if(id == R.id.action_filter){
            Intent intent = new Intent(context, TripFilterForm.class);
            startActivityForResult(intent,23);
            return true;
        }
        if(id == R.id.action_draft){
            getActivity().setTitle("My Boat Hire/Draft");
            HireState = 1;
            HireNav = 0;
   //         mHireTask = new GetPublicBoatHires(HireState, HireNav);
   //         mHireTask.execute((Void) null);
            return true;
        }
        if(id == R.id.action_published){
            getActivity().setTitle("My Boat Hire/Published");
            HireState = 2;
            HireNav = 0;
     //       mHireTask = new GetPublicBoatHires(HireState, HireNav);
     //       mHireTask.execute((Void) null);
            return true;
        }
        if(id == R.id.action_hired){
            getActivity().setTitle("My Boat Hire/Awarded");
            HireState =3;
            HireNav = 0;
       //     mHireTask = new GetPublicBoatHires(HireState, HireNav);
        //    mHireTask.execute((Void) null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        DataTripPlans object = (DataTripPlans) adapterView.getItemAtPosition(i);
        selected_item_id = object.getTripId();
        selected_item_name = object.getName();
        Intent intent = new Intent(getActivity(), PassengerList.class);
        intent.putExtra("title",selected_item_name);
        intent.putExtra("trip_id",selected_item_id);
//            intent.putExtra("selected_id", selected_item_id);
//			Intent intent = new Intent(Get_list.this, DrawPointActivity.class);
        startActivity(intent);

//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        DataBids object = (DataBids) adapterView.getItemAtPosition(i);
/*        DataPublicHire object = (DataPublicHire) adapterView.getItemAtPosition(i);
        selected_item_name = object.getName();
        selected_item_id = object.getId();
        System.out.println("On Item Click................");
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, new PublicBidFragment());
        transaction.addToBackStack(null);
        transaction.commit();
*/
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
            String tripDispDate = mDay+"-"+m+"-"+Integer.toString(mYear);

            System.out.println(selected_date);
//            getSupportActionBar().setTitle(MyBoatsFragment.selected_item_name);
            SettingsPreferences.setTripPlanFilter(context,0,"","");
            tripPlanTask = new GetTripPlans(0,selected_date, date_to,device_id);
            tripPlanTask.execute((Void) null);



        }
        public void onShow(){
            //           System.out.println("Showing");
        }
    }



    public void captainAcknowledged(int id){
        changeTripPlanTask = new ChangeTripPlan("trip_ack",id);
        changeTripPlanTask.execute((Void) null);

    }


    public void selectMenuItem(int trip_id, int acc) {
        System.out.println("selectMenuItem Called from PublicHireFragment................................");
        selected_item_id = trip_id;
//        HireNav = nav;
        Intent intent;
        intent = new Intent(context, TripMenuActivity.class);
        intent.putExtra("trip_id",trip_id);
        startActivityForResult(intent,5);
        //context2.startActivityForResult(intent,5);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("onActivityResult Call back, requestCode: "+requestCode+",resultCode:"+resultCode+" ....................................................... Result Code: "+resultCode);
        switch(requestCode) {
            case (23): {  //return from Trip  Log
                if (resultCode == Activity.RESULT_OK) {
                    filter_index = data.getIntExtra("filter_index", 0) + 1;
                    date_from = data.getStringExtra("date_from");
                    date_to = data.getStringExtra("date_to");
                    device_id = data.getIntExtra("device_id", 0);
                    SettingsPreferences.setTripPlanFilter(context,filter_index,date_from,date_to);
                    selected_date = date_from;
                    System.out.println("Filter Index:" + filter_index + ",From:" + date_from + ",Date To:" + date_to + ",Sort:" + sort_index);
                    tripFragment tf = new tripFragment();
                    tripPlanTask = new GetTripPlans(filter_index, selected_date, date_to, device_id);
                    tripPlanTask.execute((Void) null);
                }

            }
            break;

            case (3):{  //return from New Device form
                if (resultCode == Activity.RESULT_OK) {
                    String action = data.getStringExtra("action");
                    if(action.equalsIgnoreCase("verify_ok")){
                        AppUtils.showAlertDialog(context, "Congratulations", "Your contact number is verified. You may start posting your boat charter requests now");

                    }
                }
                break;
            }
            case (4):{  //return from New Device form
                if (resultCode == Activity.RESULT_OK) {
                    String action = data.getStringExtra("action");
                }
                break;
            }
            case (5):{ //5 - menu dialog
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                if (resultCode == Activity.RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.
                    final String action = data.getStringExtra("action");
                    final int trip_id = TripPlanFragment.selected_item_id;
                    String message ="";
                    if(action.equalsIgnoreCase("del")){
                        message = "Do you want to Delete this Trip Plan Now?";
                    }//                    System.out.println("Action:"+action);
                    AppUtils.showConfirmDialog(context,"Delete Trip",message,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    changeTripPlanTask = new ChangeTripPlan(action,trip_id);
                                    changeTripPlanTask.execute((Void) null);
                                    //DeleteDevice mDeleteTask = new DeleteDevice(context,device_id,action);
                                    //mDeleteTask.execute((Void) null);
                                }
                            });
                }
                break;
            }


        }

    }



    public class GetTripPlans extends AsyncTask<Void, Void, String> {
        int index;
        String date_from;
        String date_to;
        int device_id;
        GetTripPlans(int index, String date_from, String date_to, int device_id) {
            this.index=index;
            this.date_from = date_from;
            this.date_to = date_to;
            this.device_id = device_id;
        }
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
                String result = parser.GetTripPlans(context, index, date_from, date_to, device_id);
                return result;
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
            if (!TextUtils.isEmpty(result)) try {
                JSONObject jObj = new JSONObject(result);
                if (jObj.has("status")) {
                    String statuss = jObj.getString("status");
                    if (statuss.equalsIgnoreCase("ok")) {
                        errorLayout.setVisibility(View.GONE);
                        data = AppUtils.drawTripPlanList(jObj, context);
                        //    lv=(ListView)findViewById(R.id.hire_list);
                        lv.setAdapter(new TripPlanAdapter(context, data, TripPlanFragment.this));
                 //       lv.setOnItemClickListener((AdapterView.OnItemClickListener) context);
                        //   lv.setOnItemClickListener((AdapterView.OnItemClickListener) context);
                        //   layoutList.setVisibility(View.VISIBLE);
                        //    layoutWelcome.setVisibility(View.GONE);
                    } else {
                        data = AppUtils.drawTripPlanList(jObj, context);
                        //    lv=(ListView)findViewById(R.id.hire_list);
                        lv.setAdapter(new TripPlanAdapter(context, data, TripPlanFragment.this));
                        System.out.println(jObj.getString("error"));
                            errorLayout.setVisibility(View.VISIBLE);
                            errorTextView.setText(jObj.getString("error"));
                            //AppUtils.showAlertDialog(context, jObj.getString("error_title"), jObj.getString("error_message"));
                    }
                    //	loading.setText("Authentication failed");
                    //	Intent intent = new Intent(SplashScreen.this, Login_Activity.class);
                    //	startActivity(intent);
                    //	finish();

                } else {
                    Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_LONG).show();
                    //	System.out.println("Result"+result);

                }
            } catch (JSONException e) {
                //loading.setText("Unable to reach Server...");
                Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_LONG).show();
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            else {
                //Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

            }
        }
    }
    public class ChangeTripPlan extends AsyncTask<Void, Void, String> {
        String trip_id;
        String action;
        ChangeTripPlan(String action, int trip_id) {
            this.action = action;
            this.trip_id = String.valueOf(trip_id);
        }

        @Override
        protected String doInBackground(Void... arg0) {
            // TODO: attempt authentication against a network service.

            try {
                ContentParser parser = new ContentParser(context);
                return parser.getAJAX(context,action, trip_id,"0","");
            } catch (Exception ex) {
                //finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String result) {
            changeTripPlanTask = null;
            //showProgress(false);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
                            tripPlanTask = new GetTripPlans(filter_index, selected_date, date_to, 0);
                            tripPlanTask.execute((Void) null);
                        } else {
                            String errmsg = jObj.getString("error");
                            AppUtils.showAlertDialog(context, "Error", errmsg);
//							Toast.makeText(getApplicationContext(), errmsg, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "Please check your internet connection 1", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Please check your internet connection 2", Toast.LENGTH_LONG).show();
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(context, "Unable to reach server. Please check your internet connection 2", Toast.LENGTH_LONG).show();
                //AppUtils.showAlertDialog(context, "Timeout", "Unable to reach Server. Please try again!");
            }


        }

        @Override
        protected void onCancelled() {
            changeTripPlanTask = null;
            //showProgress(false);
        }
    }

}
