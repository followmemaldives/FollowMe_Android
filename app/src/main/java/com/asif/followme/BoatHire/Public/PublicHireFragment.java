package com.asif.followme.BoatHire.Public;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;
import com.asif.followme.R;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.util.AppUtils;
import com.asif.followme.util.SettingsPreferences;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PublicHireFragment extends Fragment implements AdapterView.OnItemClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    public static ListView lv;
    private GetPublicBoatHires mHireTask = null;
    private ChangeBoatHireTask mChangeTask = null;
    private LinearLayout errorLayout;
    private TextView errorTextView;
    static SwipeRefreshLayout swipeLayout;
    int HireState = 0;
    static int HireNav = 0;
    private static Context context, context2;
    public static List<DataPublicHire> data = new ArrayList<DataPublicHire>();
    private static Activity activity;
    public static String selected_item_id,selected_item_name;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PublicHireFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PublicHireFragment newInstance(int columnCount) {
        PublicHireFragment fragment = new PublicHireFragment();
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
        View view = inflater.inflate(R.layout.public_hire_fragment, container, false);
        setHasOptionsMenu(true);
        lv= (ListView) view.findViewById(R.id.hire_list1);
        lv.setOnItemClickListener(this);
//        lv.setOnClickListener((AdapterView.OnClickListener) context);



//        context = getActivity().getApplicationContext();
        getActivity().setTitle("My Boat Hire/All");
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
                        mHireTask = new GetPublicBoatHires(HireState, HireNav);
                        mHireTask.execute((Void) null);
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
        mHireTask = new GetPublicBoatHires(HireState, HireNav);
        mHireTask.execute((Void) null);
        //    mHireTask = new GetPublicBoatHires(HireState, HireNav);
        //    mHireTask.execute((Void) null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.hire_public_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_new_hire){
            Intent intent;
            if(SettingsPreferences.getContactVerified(context)) {
                intent = new Intent(context, NewHireForm.class);
                startActivityForResult(intent, 4);
            } else {
                intent = new Intent(context, ContactVerifyForm.class);
                startActivityForResult(intent, 3);
            }
            //startActivity(intent);
            //return true;
        }
        if(id == R.id.action_all){
            getActivity().setTitle("My Boat Hire/All");
            HireState = 0;
            HireNav = 0;
            mHireTask = new GetPublicBoatHires(HireState, HireNav);
            mHireTask.execute((Void) null);
            return true;
        }
        if(id == R.id.action_draft){
            getActivity().setTitle("My Boat Hire/Draft");
            HireState = 1;
            HireNav = 0;
            mHireTask = new GetPublicBoatHires(HireState, HireNav);
            mHireTask.execute((Void) null);
            return true;
        }
        if(id == R.id.action_published){
            getActivity().setTitle("My Boat Hire/Published");
            HireState = 2;
            HireNav = 0;
            mHireTask = new GetPublicBoatHires(HireState, HireNav);
            mHireTask.execute((Void) null);
            return true;
        }
        if(id == R.id.action_hired){
            getActivity().setTitle("My Boat Hire/Awarded");
            HireState =3;
            HireNav = 0;
            mHireTask = new GetPublicBoatHires(HireState, HireNav);
            mHireTask.execute((Void) null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        DataBids object = (DataBids) adapterView.getItemAtPosition(i);
        DataPublicHire object = (DataPublicHire) adapterView.getItemAtPosition(i);
        selected_item_name = object.getName();
        selected_item_id = object.getId();
        System.out.println("On Item Click................");
  //      Fragment newFragment = new PublicBidFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, new PublicBidFragment());
        transaction.addToBackStack(null);
        transaction.commit();

    }




    public void selectMenuItem(String id, int nav) {
        System.out.println("selectMenuItem Called from PublicHireFragment................................");
        selected_item_id = id;
        HireNav = nav;
        Intent intent;
        intent = new Intent(context, PublicHireMenuActivity.class);
        intent.putExtra("vname",selected_item_name);
        startActivityForResult(intent,5);
        //context2.startActivityForResult(intent,5);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("onActivityResult Call back....................................................... Result Code: "+resultCode);
        switch(requestCode) {
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
                    final String hire_id = PublicHireFragment.selected_item_id;
                    String title = selected_item_name;
                    String message ="";
                    if(action.equalsIgnoreCase("hire_publish")){
                        message = "Do you want to Publish the Boat Hire Request?";
                    }
                    if(action.equalsIgnoreCase("hire_unpublish")){
                        message = "Do you want to remove the Boat Hire request from Published list Now?";
                    }
                    if(action.equalsIgnoreCase("hire_cancel")){
                        message = "People had started bidding for the request. Do you want to Cancel the Boat Hire Request?";
                    }
                    if(action.equalsIgnoreCase("hire_delete")){
                        message = "Do you want to Delete this Boat Hire request Now?";
                    }//                    System.out.println("Action:"+action);
                    AppUtils.showConfirmDialog(context,title,message,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    System.out.println("Deleting Boat Hire...................................................");
                                    mChangeTask = new ChangeBoatHireTask(action,hire_id);
                                    mChangeTask.execute((Void) null);
                                    //DeleteDevice mDeleteTask = new DeleteDevice(context,device_id,action);
                                    //mDeleteTask.execute((Void) null);
                                }
                            });
                }
                break;
            }


        }

    }



    public class GetPublicBoatHires extends AsyncTask<Void, Void, String> {
        int state;
        int nav;
        GetPublicBoatHires(int state, int nav) {
            this.state = state;
            this.nav = nav;
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
                String result = parser.GetPublicBoatHireList1(context, state);
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
                        errorTextView.setVisibility(View.GONE);
                        data = AppUtils.drawPublicBoatHireList1(jObj, context);
                        //    lv=(ListView)findViewById(R.id.hire_list);
                        lv.setAdapter(new PublicHireAdapter(context, data, PublicHireFragment.this));
                 //       lv.setOnItemClickListener((AdapterView.OnItemClickListener) context);
                        //   lv.setOnItemClickListener((AdapterView.OnItemClickListener) context);
                        //   layoutList.setVisibility(View.VISIBLE);
                        //    layoutWelcome.setVisibility(View.GONE);
                    } else {
                        if (statuss.equalsIgnoreCase("verify")) {
                            //      layoutList.setVisibility(View.GONE);
                            //      layoutWelcome.setVisibility(View.VISIBLE);
                        } else {

                            errorLayout.setVisibility(View.VISIBLE);
                            errorTextView.setText(jObj.getString("error_message"));
                            //AppUtils.showAlertDialog(context, jObj.getString("error_title"), jObj.getString("error_message"));
                        }
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
    public class ChangeBoatHireTask extends AsyncTask<Void, Void, String> {
        String hire_id;
        String action;
        ChangeBoatHireTask(String action, String hire_id) {
            this.action = action;
            this.hire_id = hire_id;
        }

        @Override
        protected String doInBackground(Void... arg0) {
            // TODO: attempt authentication against a network service.

            try {
                ContentParser parser = new ContentParser(context);
                return parser.getAJAX(context,action, hire_id,"0","");
            } catch (Exception ex) {
                //finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String result) {
            mChangeTask = null;
            //showProgress(false);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
                            mHireTask = new GetPublicBoatHires(HireState, HireNav);
                            mHireTask.execute((Void) null);
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
            mChangeTask = null;
            //showProgress(false);
        }
    }

}
