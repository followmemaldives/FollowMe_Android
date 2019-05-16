package com.asif.followme;

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
import android.view.*;
import android.widget.*;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.model.DataWallet;
import com.asif.followme.util.AppUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WalletFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    public static ListView lv;
    private GetWallet mWalletTask = null;

    static SwipeRefreshLayout swipeLayout;
    int HireState = 0;
    static int HireNav = 0;
    private static Context context, context2;
    public static List<DataWallet> data = new ArrayList<>();
    private static Activity activity;
    public static String selected_wallet_id,selected_item_name;
    private TextView walletBalance;
    public static int tenderStatus;
    private LinearLayout errorLayout;
    private TextView errorLabel;
    private LinearLayout layoutBoatType, layoutDest;
    private Button reloadBtn;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WalletFragment() {
    }

    @SuppressWarnings("unused")
    public static WalletFragment newInstance(int columnCount) {
        WalletFragment fragment = new WalletFragment();
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
        View view = inflater.inflate(R.layout.wallet_fragment, container, false);
        setHasOptionsMenu(true);
        lv= (ListView) view.findViewById(R.id.wallet_list);
        lv.setOnItemClickListener(this);
        walletBalance = (TextView) view.findViewById(R.id.wallet_balance);
        errorLabel = (TextView) view.findViewById(R.id.error_label);
        errorLayout = (LinearLayout) view.findViewById(R.id.error_layout);
        layoutBoatType = (LinearLayout) view.findViewById(R.id.layout_boat_type);
        layoutDest = (LinearLayout) view.findViewById(R.id.layout_dest);
        reloadBtn = (Button) view.findViewById(R.id.reload_btn);
        reloadBtn.setOnClickListener(this);

//        errorLayout.setVisibility(View.GONE);
//        lv.setOnClickListener((AdapterView.OnClickListener) context);



//        context = getActivity().getApplicationContext();
        getActivity().setTitle("Prepaid Reload");

        context = view.getContext();
        activity = getActivity();
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);
        swipeLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mWalletTask = new GetWallet();
                        mWalletTask.execute();
                    }
                }
        );

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

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        mWalletTask = new GetWallet();
        mWalletTask.execute();
    }






    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("onActivityResult Call back......................................................."+requestCode+","+resultCode);
        switch(requestCode) {
            case (14):  //return from New Device form
                if (resultCode == Activity.RESULT_OK) {
                    System.out.println("Result Code OK");
                    String action = data.getStringExtra("action");
                    mWalletTask = new GetWallet();
                    mWalletTask.execute();
                }
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reload_btn:
                Intent intent = new Intent(context, NewReloadForm.class);
                startActivityForResult(intent, 14);
                break;
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.my_wallet_menu, menu);
        MenuItem walletRefresh = menu.findItem(R.id.action_refresh);
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.public_map_menu,menu);
//        if(SettingsPreferences.getSelectedPublicAccount(context)==0){

        //      }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            System.out.println("Refresh Clicked......");
            mWalletTask = new GetWallet();
            mWalletTask.execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("onItemClick......");
        DataWallet object = (DataWallet) parent.getItemAtPosition(position);
        selected_wallet_id = object.getId();
        Intent intent = new Intent(getActivity(), WalletInfoActivity.class);
        intent.putExtra("wid",selected_wallet_id);
        startActivity(intent);
    }


    public class GetWallet extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
//			swipeLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);
            swipeLayout.setRefreshing(true);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            try{
                ContentParser parser = new ContentParser(context);
                return parser.getWallet(context);
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
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
                            JSONObject wObj = new JSONObject(jObj.getString("walletData"));
                            String balance = wObj.getString("balance");
                            walletBalance.setText(balance);
                            data = AppUtils.drawWalletList(jObj, context);
                            System.out.println("Data:"+data);
                         //   lv=(ListView)findViewById(R.id.public_bid_fragment);
                            lv.setAdapter(new WalletFragmentAdapter(context, data));
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
