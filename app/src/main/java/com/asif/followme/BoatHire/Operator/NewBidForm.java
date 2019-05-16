package com.asif.followme.BoatHire.Operator;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.asif.followme.R;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.model.NameValuePairs;
import com.asif.followme.util.AppUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 1/6/2018.
 */

public class NewBidForm extends Activity implements View.OnClickListener {
    public static String RESULT_IDS = "my_menu_ids";
    Context context;
    public Button saveBidBtn,cancelForm;
 //   public EditText share_email,share_email_new, share_email_edit;
    public EditText bidPrice;
    private Spinner boatSpinner;
    private int bid_id, device_id;

    public TextView bidAmountLabel;
    private AsyncTask<Void, Void, String> SharedPostAsyncTask;
    private getBidInfoTask mBidInfoTask = null;

    //    private AsyncTask<Void, Void, String> SharedReadAsyncTask;
    private Dialog sDialog;
    public static String shared_user_id,shared_user_email,action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_bid_form);
        context = this;

        saveBidBtn = (Button) findViewById(R.id.save_bid_btn);
        cancelForm = (Button) findViewById(R.id.cancel_form);
        bidPrice = (EditText) findViewById(R.id.bid_price);
        boatSpinner = (Spinner) findViewById(R.id.boat_spinner);
        bidAmountLabel = (TextView) findViewById(R.id.bid_amount_label);

//        share_email_edit = (EditText) findViewById(R.id.share_email_edit);
 //       share_email = new EditText(new ContextThemeWrapper(this, R.style.App_EditTextDisable), null, 0);
 //       shareLabel1 = (TextView) findViewById(R.id.share_label1);
        saveBidBtn.setOnClickListener(this);
        cancelForm.setOnClickListener(this);
    }
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
//        String vname = "Opt...";
        bid_id=0;
        int hire_type =0;
        try{
            Bundle extras = getIntent().getExtras();
//            vname = extras.getString("vname");
//            bid_id = Integer.parseInt(HireMyFragment.selected_item_id);
            bid_id=extras.getInt("bid_id");
            hire_type = MyBidActivity.hire_type;
            setTitle("New Bid");
            String amountLabel ="";
            switch(hire_type){
                case 2:
                    amountLabel = "Your bid Amount per Hour";
                    break;
                case 3:
                    amountLabel = "Your bid Amount per Day";
                    break;
                case 4:
                    amountLabel = "Your bid Amount per Month";
                    break;
                default:
                    amountLabel = "Your bid Amount";
            }
            bidAmountLabel.setText(amountLabel);

        } catch (Exception e){
//            vname="";
        }
        if(bid_id > 0) {   //edit
            setTitle("Edit Bid");
        } else {
            setTitle("New Bid");
        }
        mBidInfoTask = new getBidInfoTask("read",bid_id,0,0);
        mBidInfoTask.execute((Void) null);

    }
    @Override
    public void onClick(View view) {
        System.out.println("Clicked:"+view.getId());
        switch (view.getId() /*to get clicked view id**/) {
            case R.id.save_bid_btn:
             //   String bidAmount0 = bidPrice.getText().toString();
                double bidAmount = Double.parseDouble(bidPrice.getText().toString());
                if(bidAmount<1){
                    bidPrice.setError("Please enter Bid Amount");
                    return;
                }
                mBidInfoTask = new getBidInfoTask("save",bid_id,bidAmount,device_id);
                mBidInfoTask.execute((Void) null);
            //    if(!isValidEmail(share_email.getText())){
            //        share_email.setError("Please Enter a valid Email address");
            //        share_email.requestFocus();
            //        return;
            //    }
                int access = 0;
            //    String user_email = share_email.getText().toString();
            //    String device_id= MyActivity.selected_item_id;
             //   postTask = new postSharedUser(user_email,device_id,access,shared_user_id);
             //   postTask.execute((Void) null);

                break;
            case R.id.cancel_form:
                finish();
                break;
        }

    }


    public class getBidInfoTask extends AsyncTask<Void, Void, String> {
        String action;
        int bid_id;
        double amount;
        int boat;
        getBidInfoTask(String action,int bid_id,double amount,int boat){
            this.action=action;
            this.bid_id = bid_id;
            this.amount=amount;
            this.boat=boat;
        }

        @Override
        protected String doInBackground(Void... arg0) {
            // TODO: attempt authentication against a network service.

            try {
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.operatorBidActions(context, action, bid_id, amount, boat);
            } catch (Exception ex) {
                //finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String result) {
            mBidInfoTask = null;
            //showProgress(false);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
                            if(action.equalsIgnoreCase("read")) {
                                ArrayAdapter<NameValuePairs> adapter = new ArrayAdapter<NameValuePairs>(getBaseContext(), android.R.layout.simple_spinner_item, AppUtils.setNameValuePairs(jObj, getBaseContext()));
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                boatSpinner.setAdapter(adapter);
                                if (bid_id > 0) {

                                    for (int position = 0; position < adapter.getCount(); position++) {
                                        if (adapter.getItem(position).getId() == jObj.getInt("device_id")) {
                                            boatSpinner.setSelection(position);
                                        }
                                    }
                                }
                                device_id = adapter.getItem(0).getId();
                                // tripStatusSpinner.setSelection(trip_status-1);

                                boatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                        NameValuePairs pair_id = (NameValuePairs) parent.getSelectedItem();
                                        device_id = pair_id.getId();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                    }
                                });
                            } else {
                                Intent intent = new Intent();
                                intent.putExtra("reload", true);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        } else {
                            String errmsg = jObj.getString("error");
                            TextView errorText = (TextView)boatSpinner.getSelectedView();
                            errorText.setError("");
                            errorText.setTextColor(Color.RED);//just to highlight that this is an error
                            errorText.setText(errmsg);
//                            AppUtils.showAlertDialog(context, "Error", errmsg);
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
            mBidInfoTask = null;
            //showProgress(false);
        }
    }


}
