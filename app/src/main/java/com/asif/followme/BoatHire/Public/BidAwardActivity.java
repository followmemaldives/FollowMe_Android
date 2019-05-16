package com.asif.followme.BoatHire.Public;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.asif.followme.R;
import com.asif.followme.manager.ContentParser;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 1/6/2018.
 */

public class BidAwardActivity extends Activity implements View.OnClickListener {
    public static String RESULT_IDS = "my_menu_ids";
    Context context;
    public Button awardBtn;
//    private LinearLayout btnLayout;
    //    public EditText share_email,share_email_new, share_email_edit;
//    public CheckBox checkBox_cl,checkBox_hs,checkBox_ds,checkBox_as;
//    public TextView noticeLabel;
    public EditText specialInput;
    private getBidAwardTask mBidAwardTask = null;
    //    private AsyncTask<Void, Void, String> SharedReadAsyncTask;
    private Dialog sDialog;
    private ProgressBar progressBar;
    private int bid_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bid_award_form);
        context = this;

        awardBtn = (Button) findViewById(R.id.award_btn);
     //   btnLayout = (LinearLayout) findViewById(R.id.btn_layout);

        //       share_email = new EditText(new ContextThemeWrapper(this, R.style.App_EditTextDisable), null, 0);
    //    noticeLabel = (TextView) findViewById(R.id.notice_label);
        specialInput = (EditText) findViewById(R.id.special_info);
        progressBar = (ProgressBar)  findViewById(R.id.loading_spinner);
        awardBtn.setOnClickListener(this);
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
            case R.id.award_btn:
                mBidAwardTask = new getBidAwardTask("save",bid_id,specialInput.getText().toString());
                mBidAwardTask.execute((Void) null);
                break;
        }

    }


    public class getBidAwardTask extends AsyncTask<Void, Void, String> {
        String action;
        int bid_id;
        String remarks;
        getBidAwardTask(String action,int bid_id,String remarks){
            this.action=action;
            this.bid_id = bid_id;
            this.remarks=remarks;
        }

        @Override
        protected String doInBackground(Void... arg0) {
            // TODO: attempt authentication against a network service.

            try {
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.saveBoatHire(context, "award", bid_id, "",0,"", remarks,0, new JSONObject(),0,"",0);
            } catch (Exception ex) {
                //finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String result) {
            mBidAwardTask = null;
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
            mBidAwardTask = null;
            //showProgress(false);
        }
    }

}
