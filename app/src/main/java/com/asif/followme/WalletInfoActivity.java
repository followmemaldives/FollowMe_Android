package com.asif.followme;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.asif.followme.manager.ContentParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 1/6/2018.
 */

public class WalletInfoActivity extends Activity implements View.OnClickListener {
    public static String RESULT_IDS = "my_menu_ids";
    Context context;
    public Button cancelBtn;
    private GetWalletInfo mWalletInfoTask = null;
    private TextView walletMobile,walletBoat,walletAmount, walletDesc1,walletDesc2,walletDesc3, walletDate1,walletDate2,walletDate3;


    //    private AsyncTask<Void, Void, String> SharedReadAsyncTask;
     public int reload_pkg =0;
    private LinearLayout reloadLayout1, reloadLayout2, reloadLayout3, btnLayout3, spinnerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_info_activity);
        context = this;
        walletBoat = (TextView) findViewById(R.id.wallet_boat);
        walletMobile = (TextView) findViewById(R.id.wallet_mobile);
        walletAmount = (TextView) findViewById(R.id.wallet_amount);
        walletDesc1 = (TextView) findViewById(R.id.wallet_desc1);
        walletDesc2 = (TextView) findViewById(R.id.wallet_desc2);
        walletDesc3 = (TextView) findViewById(R.id.wallet_desc3);
        walletDate1 = (TextView) findViewById(R.id.wallet_date1);
        walletDate2 = (TextView) findViewById(R.id.wallet_date2);
        walletDate3 = (TextView) findViewById(R.id.wallet_date3);



    }
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        int wid =0;

        try{
            Bundle extras = getIntent().getExtras();
            wid = Integer.parseInt(extras.getString("wid"));
            mWalletInfoTask = new GetWalletInfo(wid);
            mWalletInfoTask.execute();
            //setTitle("New Bid");

        } catch (Exception e){
            e.printStackTrace();
        }
        setTitle("Reload Info");

    }
    @Override
    public void onClick(View view) {
        System.out.println("Clicked:"+view.getId());
        switch (view.getId() /*to get clicked view id**/) {
            case R.id.cancel_btn:
                finish();
                break;
        }
    }


    public class GetWalletInfo extends AsyncTask<Void, Void, String> {
        int wid;
        GetWalletInfo(int id){
            this.wid = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            try{
                ContentParser parser = new ContentParser(context);
                return parser.getWalletInfo(context,wid);
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onCancelled() {
//			System.out.println("Cancelled.....................");
//		    onMyAsyncTaskCompletedListener(null);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //System.out.println(result);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
                            JSONObject wObj = new JSONObject(jObj.getString("walletData"));
                           // String balance = wObj.getString("balance");
                            walletMobile.setText(wObj.getString("mobile"));
                            walletAmount.setText(wObj.getString("amount"));
                            walletBoat.setText(wObj.getString("v_name"));
                            try {
                                JSONArray alldata = new JSONArray(jObj.getString("aaData"));
                                for (int i = 0; i < alldata.length(); i++) {
                                    //DataBids data = new DataBids();
                                    JSONArray jsonArray = alldata.getJSONArray(i);
                                    System.out.println("Response:"+jsonArray.getString(2));
                                    switch(i){
                                        case 0:
                                            walletDate1.setText(jsonArray.getString(1));
                                            walletDesc1.setText(jsonArray.getString(2));
                                            break;
                                        case 1:
                                            walletDate2.setText(jsonArray.getString(1));
                                            walletDesc2.setText(jsonArray.getString(2));
                                            break;
                                        case 2:
                                            walletDate3.setText(jsonArray.getString(1));
                                            walletDesc3.setText(jsonArray.getString(2));
                                            break;



                                    }
                                    //for (int j = 0; j < jsonArray.length(); j++) {
                                     //   String ss = jsonArray.getString(j);
                                    //}
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                           // JSONObject dObj = new JSONObject(jObj.getString("aaData"));
//                            data = AppUtils.drawWalletList(jObj, context);
//                            System.out.println("Data:"+data);
                            //   lv=(ListView)findViewById(R.id.public_bid_fragment);


                        } else {
                            //errmsg = jObj.getString(TAG_ERROR_MSG);
                            //AppUtils.showAlertDialog(context, "Error", errmsg);



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



}
