package com.asif.followme.BoatHire.Public;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.asif.followme.R;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.util.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PublicHistoryFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    public static ListView lv;
    private GetPublicBoatHires mHireTask = null;
    static SwipeRefreshLayout swipeLayout;
    int HireState = 0;
    static int HireNav = 1;
    private static Context context, context2;
    public static List<DataPublicHire> data = new ArrayList<DataPublicHire>();
    private static Activity activity;
    public static String selected_item_id,selected_item_name;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PublicHistoryFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PublicHistoryFragment newInstance(int columnCount) {
        PublicHistoryFragment fragment = new PublicHistoryFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hire_public_history, container, false);
        lv= (ListView) view.findViewById(R.id.hire_list);
        getActivity().setTitle("Boat Hired History");

//        context = getActivity().getApplicationContext();
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

        mHireTask = new GetPublicBoatHires(HireState, HireNav);
        mHireTask.execute((Void) null);

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





    public static void selectMenuItem(String id, int nav) {
        selected_item_id = id;
        HireNav = nav;
        Intent intent;
        intent = new Intent(activity, PublicHireMenuActivity.class);
        intent.putExtra("vname",selected_item_name);
        activity.startActivityForResult(intent,5);
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
                String result = parser.GetPublicBoatHireList2(context, state);
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
            //SettingsPreferences.setSelectedMyAccount(context, 0);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
                            data = AppUtils.drawPublicBoatHireList2(jObj, context);
                        //    lv=(ListView)findViewById(R.id.hire_list);
                            lv.setAdapter(new PublicHistoryAdapter(context, data, PublicHistoryFragment.this));
//                            lv.setOnItemClickListener((AdapterView.OnItemClickListener) context);
                         //   layoutList.setVisibility(View.VISIBLE);
                        //    layoutWelcome.setVisibility(View.GONE);
                        } else {
                            if(statuss.equalsIgnoreCase("verify")){
                          //      layoutList.setVisibility(View.GONE);
                          //      layoutWelcome.setVisibility(View.VISIBLE);
                            } else {
                                AppUtils.showAlertDialog(context, jObj.getString("error_title"), jObj.getString("error_message"));
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

            } else {
                //Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

            }
        }
    }

}
