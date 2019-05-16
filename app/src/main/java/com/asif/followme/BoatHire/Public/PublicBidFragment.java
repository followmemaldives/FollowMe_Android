package com.asif.followme.BoatHire.Public;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.asif.followme.BoatHire.DataBids;
import com.asif.followme.R;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.util.AppUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PublicBidFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    public static ListView lv;
    private GetBidList mBidTask = null;

    static SwipeRefreshLayout swipeLayout;
    int HireState = 0;
    static int HireNav = 0;
    private static Context context, context2;
    public static List<DataBids> data = new ArrayList<DataBids>();
    private static Activity activity;
    public static String selected_item_id,selected_item_name;
    private TextView hireDate, hireFrom, hireInfo, hireMethod, hireDuration;
    public static int tenderStatus;
    private LinearLayout errorLayout;
    private TextView errorLabel;
    private LinearLayout layoutBoatType, layoutDest, layoutDestination,layoutDuration, layoutHeader;
    private final int BID_AWARD_ACTIVITY = 50;
    private final int BID_RATE_ACTIVITY = 51;
 //   private Button btnCompleted;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PublicBidFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PublicBidFragment newInstance(int columnCount) {
        PublicBidFragment fragment = new PublicBidFragment();
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
        View view = inflater.inflate(R.layout.public_bid_fragment, container, false);
        setHasOptionsMenu(true);
        lv= (ListView) view.findViewById(R.id.bid_list);
        //lv.setOnItemClickListener(this);
        hireFrom = (TextView) view.findViewById(R.id.hire_from);
        hireDate = (TextView) view.findViewById(R.id.hire_date);
        hireInfo = (TextView) view.findViewById(R.id.hire_info);
        errorLabel = (TextView) view.findViewById(R.id.error_label);
        errorLayout = (LinearLayout) view.findViewById(R.id.error_layout);
        layoutHeader = (LinearLayout) view.findViewById(R.id.layout_bid_header);
        layoutBoatType = (LinearLayout) view.findViewById(R.id.layout_boat_type);
        layoutDest = (LinearLayout) view.findViewById(R.id.layout_dest);
        layoutDestination = (LinearLayout) view.findViewById(R.id.destination_layout);
        layoutDuration = (LinearLayout) view.findViewById(R.id.duration_layout);
        hireMethod = (TextView) view.findViewById(R.id.hire_method);
        hireDuration = (TextView) view.findViewById(R.id.hire_duration);
        layoutHeader.setVisibility(View.GONE);
//        btnCompleted = (Button) view.findViewById(R.id.btn_hire_completed);
//        btnCompleted.setOnClickListener(this);

//        errorLayout.setVisibility(View.GONE);
//        lv.setOnClickListener((AdapterView.OnClickListener) context);



//        context = getActivity().getApplicationContext();
        getActivity().setTitle("My Boat Hire/All");

        context = view.getContext();
        activity = getActivity();
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);
        swipeLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mBidTask = new GetBidList(PublicHireFragment.selected_item_id);
                        mBidTask.execute();
                    }
                }
        );

        mBidTask = new GetBidList(PublicHireFragment.selected_item_id);
        mBidTask.execute();
//        mHireTask = new GetPublicBoatHires(HireState, HireNav);
//        mHireTask.execute((Void) null);

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


/*    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.boat_hire_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
*/


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("On Item Click................");

    }




    public void openBidAwardActivity(String id, String name) {
        System.out.println("selectMenuItem Called in PublicBidFragment................................");
        selected_item_id = id;
        selected_item_name = name;
        Intent intent;
        intent = new Intent(context, BidAwardActivity.class);
        intent.putExtra("v_name", selected_item_name);
        intent.putExtra("bid_id",id);
//        intent.putExtra("vname",selected_item_name);
        startActivityForResult(intent, BID_AWARD_ACTIVITY);
        //context2.startActivityForResult(intent,5);
    }
    public void openBidRateActivity(String id, String name) {
        System.out.println("selectMenuItem Called in PublicBidFragment................................");
        selected_item_id = id;
        selected_item_name = name;
        Intent intent;
        intent = new Intent(context, BidRateActivity.class);
        intent.putExtra("v_name", selected_item_name);
        intent.putExtra("bid_id",id);
//        intent.putExtra("vname",selected_item_name);
        startActivityForResult(intent, BID_RATE_ACTIVITY);
        //context2.startActivityForResult(intent,5);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("onActivityResult Call back.......................................................");
        switch(requestCode) {
            case (BID_AWARD_ACTIVITY):  //return from Award Form
                if (resultCode == Activity.RESULT_OK) {
                    mBidTask = new GetBidList(PublicHireFragment.selected_item_id);
                    mBidTask.execute();
                }
                break;

        }

    }


    public class GetBidList extends AsyncTask<Void, Void, String> {
        private final int hire_id;

        GetBidList(String id) {
            hire_id=Integer.parseInt(id);
        }
        @Override
        protected void onPreExecute() {
//			swipeLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);

            swipeLayout.setRefreshing(true);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            try{
                ContentParser parser = new ContentParser(context);
                return parser.getPublicBidList(context,hire_id);
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
            layoutHeader.setVisibility(View.VISIBLE);
            //System.out.println(result);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
                            JSONObject nObj = new JSONObject(jObj.getString("hireData"));
                            String hire_info = nObj.getString("remarks");
                            String hire_date = nObj.getString("hire_date");
                            hireInfo.setText(hire_info);
                            hireDate.setText(hire_date);
                            hireDuration.setText(nObj.getString("hire_duration"));
                            hireMethod.setText(nObj.getString("hire_method"));
                            tenderStatus = nObj.getInt("tender_status");
                            int hire_type = nObj.getInt("hire_type");
                            data = AppUtils.drawPublicBidList(jObj, context);
                            System.out.println("Data:"+data);
                            try {
                                JSONArray boatTypes = jObj.getJSONArray("boatTypes");
                                layoutBoatType.removeAllViews();
                                for (int i = 0; i < boatTypes.length(); i++) {
                                    JSONArray jsonArray = boatTypes.getJSONArray(i);
                                    System.out.println("The Array:"+jsonArray);
                                   addBoatType(jsonArray.getString(0));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONArray destIslands = jObj.getJSONArray("destIslands");
                                layoutDest.removeAllViews();
                                for (int i = 0; i < destIslands.length(); i++) {
                                    JSONArray jsonArray = destIslands.getJSONArray(i);
                                    System.out.println("The Array:" + jsonArray);
                                    if (i == 0) {
                                        hireFrom.setText(jsonArray.getString(0));
                                    } else {
                                        addDestinations(jsonArray.getString(0));
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(hire_type==1){
                                layoutDestination.setVisibility(View.VISIBLE);
                                layoutDuration.setVisibility(View.GONE);

                            } else {
                                layoutDestination.setVisibility(View.GONE);
                                layoutDuration.setVisibility(View.VISIBLE);

                            }
                         //   lv=(ListView)findViewById(R.id.public_bid_fragment);
                            lv.setAdapter(new PublicBidAdapter(context, data, PublicBidFragment.this));
                            if(data.size()==0){
                                errorLayout.setVisibility(View.VISIBLE);
                                errorLabel.setText(jObj.getString("error"));
                            } else {
                                errorLayout.setVisibility(View.GONE);
                            }

                        } else {
                            //errmsg = jObj.getString(TAG_ERROR_MSG);
                            //AppUtils.showAlertDialog(context, "Error", errmsg);
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Error")
                                    .setMessage(jObj.getString("error"))
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            //finish();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();


                        }
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

            } else {
                //Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

            }
        }
    }

    public void addBoatType(String label) {
        View view = LayoutInflater.from(context).inflate(R.layout.tick_label,null);
        TextView labelBoatType = (TextView) view.findViewById(R.id.test_name);
//        final TextView labelBoatType = new TextView(context);
        labelBoatType.setText(label);
        layoutBoatType.addView(view);
    }
    public void addDestinations(String label) {
//        final CheckBox checkBox = new CheckBox(this);
        final TextView labelDest = new TextView(context);
        labelDest.setText(label);
//        labelBoatType.setBackgroundResource(R.drawable.border);
        //allViews.add(checkBox);
        layoutDest.addView(labelDest);
    }

}
