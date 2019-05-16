package com.asif.followme.BoatHire;

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
import com.asif.followme.util.AppUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 1/6/2018.
 */

public class BidConfirmActivity extends Activity implements View.OnClickListener {
    public static String RESULT_IDS = "my_menu_ids";
    Context context;
    public Button saveBtn,cancelBtn;
    private LinearLayout btnLayout;
    //    public EditText share_email,share_email_new, share_email_edit;
//    public CheckBox checkBox_cl,checkBox_hs,checkBox_ds,checkBox_as;
    public TextView noticeLabel, noticeDate;
    public EditText noticeInput;
    private getBidAwardTask mBidAwardTask = null;
    //    private AsyncTask<Void, Void, String> SharedReadAsyncTask;
    private Dialog sDialog;
    private ProgressBar progressBar;
    private int bid_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bid_confirm_form);
        context = this;

        saveBtn = (Button) findViewById(R.id.save_btn);
        cancelBtn = (Button) findViewById(R.id.cancel_btn);
        btnLayout = (LinearLayout) findViewById(R.id.btn_layout);

        //       share_email = new EditText(new ContextThemeWrapper(this, R.style.App_EditTextDisable), null, 0);
        noticeLabel = (TextView) findViewById(R.id.notice_label);
        noticeInput = (EditText) findViewById(R.id.notice_input);
        noticeDate = (TextView) findViewById(R.id.notice_date);
        progressBar = (ProgressBar)  findViewById(R.id.loading_spinner);
        saveBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        try{
            Bundle extras = getIntent().getExtras();
            String action = extras.getString("action");
            setTitle(extras.getString("v_name"));
            bid_id=extras.getInt("bid_id");
                btnLayout.setVisibility(View.VISIBLE);
             //   noticeDate.setVisibility(View.GONE);
                noticeInput.setVisibility(View.VISIBLE);
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
            case R.id.save_btn:
                mBidAwardTask = new getBidAwardTask("save",bid_id,noticeInput.getText().toString());
                mBidAwardTask.execute((Void) null);
                break;
            case R.id.cancel_btn:
                finish();
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
                return parser.saveBoatHire(context, "award", bid_id, "",0,"", remarks,0, null,0,"",0);
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
                            AppUtils.showAlertDialog(context, "Error", errmsg);
//							Toast.makeText(getApplicationContext(), errmsg, Toast.LENGTH_LONG).show();
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
