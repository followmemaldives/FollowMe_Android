package com.asif.followme.BoatHire.Public;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.asif.followme.R;
import com.asif.followme.manager.ContentParser;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 1/6/2018.
 */

public class BidRateActivity extends Activity implements View.OnClickListener {
    public static String RESULT_IDS = "my_menu_ids";
    Context context;
    public Button saveBtn;
//    private LinearLayout btnLayout;
    //    public EditText share_email,share_email_new, share_email_edit;
//    public CheckBox checkBox_cl,checkBox_hs,checkBox_ds,checkBox_as;
//    public TextView noticeLabel;
    public EditText specialInput;
    private getBidRateTask mBidRateTask = null;
    //    private AsyncTask<Void, Void, String> SharedReadAsyncTask;
    private Dialog sDialog;
    private ProgressBar progressBar;
    private int bid_id;
    private ImageView rateStar1,rateStar2,rateStar3,rateStar4,rateStar5;
    private int selectedRate =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bid_rate_form);
        context = this;

        saveBtn = (Button) findViewById(R.id.btn_save_rate);
     //   btnLayout = (LinearLayout) findViewById(R.id.btn_layout);

        //       share_email = new EditText(new ContextThemeWrapper(this, R.style.App_EditTextDisable), null, 0);
    //    noticeLabel = (TextView) findViewById(R.id.notice_label);
        specialInput = (EditText) findViewById(R.id.special_info);
        progressBar = (ProgressBar)  findViewById(R.id.loading_spinner);
        rateStar1 = (ImageView) findViewById(R.id.rate_star1);
        rateStar2 = (ImageView) findViewById(R.id.rate_star2);
        rateStar3 = (ImageView) findViewById(R.id.rate_star3);
        rateStar4 = (ImageView) findViewById(R.id.rate_star4);
        rateStar5 = (ImageView) findViewById(R.id.rate_star5);
        rateStar1.setOnClickListener(this);
        rateStar2.setOnClickListener(this);
        rateStar3.setOnClickListener(this);
        rateStar4.setOnClickListener(this);
        rateStar5.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
    }
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        try{
            Bundle extras = getIntent().getExtras();
            String action = extras.getString("action");
            setTitle(extras.getString("v_name"));
            System.out.println("Get Extra BID ID:"+extras.getString("bid_id"));
            bid_id=Integer.parseInt(extras.getString("bid_id"));
             //   btnLayout.setVisibility(View.VISIBLE);
             //   noticeDate.setVisibility(View.GONE);
             //   noticeInput.setVisibility(View.VISIBLE);
            //    noticeLabel.setVisibility(View.GONE);




        } catch (Exception e){
            // System.out.println(e);
            // vname="";
        }

    }
    @Override
    public void onClick(View view) {
        System.out.println("Clicked:"+view.getId());
        switch (view.getId() /*to get clicked view id**/) {
            case R.id.btn_save_rate:
                mBidRateTask = new getBidRateTask("rate",bid_id,selectedRate,specialInput.getText().toString());
                mBidRateTask.execute((Void) null);
                break;
            case R.id.rate_star1:
                doRate(1);
                break;
            case R.id.rate_star2:
                doRate(2);
                break;
            case R.id.rate_star3:
                doRate(3);
                break;
            case R.id.rate_star4:
                doRate(4);
                break;
            case R.id.rate_star5:
                doRate(5);
                break;
        }

    }

    public void doRate(int rate){
        selectedRate = rate;
        if(rate>=1){
            rateStar1.setBackgroundResource(R.drawable.star1);
        } else {
            rateStar1.setBackgroundResource(R.drawable.star0);
        }
        if(rate>=2){
            rateStar2.setBackgroundResource(R.drawable.star1);
        } else {
            rateStar2.setBackgroundResource(R.drawable.star0);
        }
        if(rate>=3){
            rateStar3.setBackgroundResource(R.drawable.star1);
        } else {
            rateStar3.setBackgroundResource(R.drawable.star0);
        }
        if(rate>=4){
            rateStar4.setBackgroundResource(R.drawable.star1);
        } else {
            rateStar4.setBackgroundResource(R.drawable.star0);
        }
        if(rate==5){
            rateStar5.setBackgroundResource(R.drawable.star1);
        } else {
            rateStar5.setBackgroundResource(R.drawable.star0);
        }

    }
    public class getBidRateTask extends AsyncTask<Void, Void, String> {
         int bid_id;
        String remarks;
        int rating;
        getBidRateTask(String action,int bid_id,int rating,String remarks){
            this.bid_id = bid_id;
            this.remarks=remarks;
            this.rating = rating;
        }

        @Override
        protected String doInBackground(Void... arg0) {
            // TODO: attempt authentication against a network service.

            try {
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.rateBidActions(context, "rate", bid_id,rating, remarks);
            } catch (Exception ex) {
                //finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String result) {
            mBidRateTask = null;
            //showProgress(false);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
                                Intent intent = new Intent();
                                intent.putExtra("reload", true);
                                setResult(RESULT_OK, intent);
                                finish();
                        } else {
                            String errmsg = jObj.getString("error");
                        //    AppUtils.showAlertDialog(context, "Error", errmsg);
							Toast.makeText(getApplicationContext(), errmsg, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection 1", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection 2", Toast.LENGTH_LONG).show();
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Unable to reach server. Please check your internet connection 2", Toast.LENGTH_LONG).show();
                //AppUtils.showAlertDialog(context, "Timeout", "Unable to reach Server. Please try again!");
            }


        }

        @Override
        protected void onCancelled() {
            mBidRateTask = null;
            //showProgress(false);
        }
    }

}
