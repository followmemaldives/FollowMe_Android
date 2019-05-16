package com.asif.followme;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.model.NameValuePairs;
import com.asif.followme.util.AppUtils;
import com.asif.followme.util.SettingsPreferences;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 1/6/2018.
 */

public class NewReloadForm extends Activity implements View.OnClickListener {
    public static String RESULT_IDS = "my_menu_ids";
    Context context;
    public Button sendReloadBtn, sendReloadBtn2, cancelBtn, cancelBtn2;
 //   public EditText share_email,share_email_new, share_email_edit;
//    public EditText reloadAmountInput;
    private TextView packageLabel,reloadConfirmLabel, errorLabel2;
    private Spinner boatSpinner, packageSpinner;
    private int bid_id, device_id;

    public TextView shareLabel1;
    private AsyncTask<Void, Void, String> SharedPostAsyncTask;
    private sendReloadTask mReloadTask = null;

    //    private AsyncTask<Void, Void, String> SharedReadAsyncTask;
    private Dialog sDialog;
    public static String shared_user_id,shared_user_email,action;
    public int reload_pkg =0;
    private LinearLayout reloadLayout1, reloadLayout2, reloadLayout3, btnLayout3, spinnerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_reload_form);
        context = this;

        sendReloadBtn = (Button) findViewById(R.id.send_reload_btn);
        sendReloadBtn2 = (Button) findViewById(R.id.send_reload_btn2);
        cancelBtn = (Button) findViewById(R.id.cancel_btn);
        cancelBtn2 = (Button) findViewById(R.id.cancel_btn2);
//        reloadAmountInput = (EditText) findViewById(R.id.reload_amount);
        boatSpinner = (Spinner) findViewById(R.id.boat_spinner);
        packageLabel = (TextView) findViewById(R.id.package_label);
        packageSpinner = (Spinner) findViewById(R.id.package_spinner);
        reloadLayout1 = (LinearLayout) findViewById(R.id.reload_layout1);
        reloadLayout2 = (LinearLayout) findViewById(R.id.reload_layout2);
        reloadLayout3 = (LinearLayout) findViewById(R.id.reload_layout3);
        reloadConfirmLabel = (TextView) findViewById(R.id.reload_confirm_msg);
        errorLabel2 = (TextView) findViewById(R.id.reload_error2);
        btnLayout3 = (LinearLayout) findViewById(R.id.btn_layout3);
        spinnerLayout = (LinearLayout) findViewById(R.id.spinner_layout);
        packageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reload_pkg = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });


//        share_email_edit = (EditText) findViewById(R.id.share_email_edit);
 //       share_email = new EditText(new ContextThemeWrapper(this, R.style.App_EditTextDisable), null, 0);
 //       shareLabel1 = (TextView) findViewById(R.id.share_label1);
        sendReloadBtn.setOnClickListener(this);
        sendReloadBtn2.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        cancelBtn2.setOnClickListener(this);
    }
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        reloadLayout2.setVisibility(View.GONE);
        reloadLayout1.setVisibility(View.VISIBLE);
        errorLabel2.setVisibility(View.GONE);
        reloadLayout3.setVisibility(View.GONE);
        reloadConfirmLabel.setText("");
        String vname = "Opt...";
        bid_id=0;
        try{
            Bundle extras = getIntent().getExtras();
            vname = extras.getString("vname");
//            bid_id = Integer.parseInt(HireMyFragment.selected_item_id);
//            setTitle("New Bid");

        } catch (Exception e){
            vname="";
        }
        setTitle("Prepaid Reload");
        mReloadTask = new sendReloadTask("read",0,0);
        mReloadTask.execute((Void) null);

    }
    @Override
    public void onClick(View view) {
        System.out.println("Clicked:"+view.getId());
        switch (view.getId() /*to get clicked view id**/) {
            case R.id.send_reload_btn:
                prepareReload();
                break;
            case R.id.send_reload_btn2:
                processReload();
                break;
            case R.id.cancel_btn:
                finish();
                break;
            case R.id.cancel_btn2:
                finish();
                break;
        }

    }
    public void prepareReload(){
        System.out.println("Package: "+reload_pkg+" , Device ID:"+device_id);
        String device_name = boatSpinner.getSelectedItem().toString();
        String msg = "";
        switch(reload_pkg){
            case 0:
                msg ="Reload MVR 10 to device "+device_name+"\nMVR 11 will be charged from your account. Please confirm";
                break;
            case 1:
                msg ="Reload MVR 30 to device "+device_name+"\nMVR 33 will be charged from your account. Please confirm";
                break;
            case 2:
                msg ="Reload MVR 50 to device "+device_name+"\nMVR 55 will be charged from your account. Please confirm";
                break;
            case 3:
                msg ="Reload MVR 70 to device "+device_name+"\nMVR 77 will be charged from your account. Please confirm";
                break;
            case 4:
                msg ="Reload MVR 90 to device "+device_name+"\nMVR 99 will be charged from your account. Please confirm";
                break;
            case 5:
                msg ="Add Data package to device "+device_name+"\nMVR 110 will be charged from your account. Please confirm";
                break;
        }
        reloadConfirmLabel.setText(msg);
        reloadLayout1.setVisibility(View.GONE);
        reloadLayout2.setVisibility(View.VISIBLE);

    }

    public void processReload(){
        System.out.println("Package: "+reload_pkg+" , Device ID:"+device_id);
        mReloadTask = new sendReloadTask("save",reload_pkg,device_id);
        mReloadTask.execute((Void) null);

    }


    public class sendReloadTask extends AsyncTask<Void, Void, String> {
        String action;
        int reload_pkg;
        int boat;
        sendReloadTask(String action, int reload_pkg,int device_id){
            this.action=action;
            this.reload_pkg=reload_pkg;
            this.boat=device_id;
        }

        @Override
        protected void onPreExecute() {
//              System.out.println("Calling GetMyVessels with id:"+id2+"++++++++++++++++++++++++++++++++++++");
            if(action.equalsIgnoreCase("save")) {
                reloadLayout2.setVisibility(View.GONE);
                btnLayout3.setVisibility(View.GONE);
                reloadLayout3.setVisibility(View.VISIBLE);
                spinnerLayout.setVisibility(View.VISIBLE);
            }
            super.onPreExecute();

        }


        @Override
        protected String doInBackground(Void... arg0) {
            // TODO: attempt authentication against a network service.

            try {
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.reloadActions(context, action, reload_pkg, boat);
            } catch (Exception ex) {
                //finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String result) {
            mReloadTask = null;
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
                                SettingsPreferences.setSessionKey(context,jObj.getString("key"));
                                boatSpinner.setAdapter(adapter);
 /*                               if (bid_id > 0) {

                                    for (int position = 0; position < adapter.getCount(); position++) {
                                        if (adapter.getItem(position).getId() == jObj.getInt("device_id")) {
                                            boatSpinner.setSelection(position);
                                        }
                                    }
                                }
                        */
                                device_id = adapter.getItem(0).getId();
                                System.out.println("Device......................................... : "+device_id);
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
                              //  Intent intent = getIntent();
                                Intent rIntent = new Intent();
                                rIntent.putExtra("reload", true);
                                setResult(RESULT_OK, rIntent);
                                finish();
                            }
                        } else {
                            String errmsg = jObj.getString("error");
                            if(action.equalsIgnoreCase("save")) {
                                errorLabel2.setText(errmsg);
                                errorLabel2.setVisibility(View.VISIBLE);
                                btnLayout3.setVisibility(View.VISIBLE);
                                spinnerLayout.setVisibility(View.GONE);
                            }
                            //packageLabel.setError(errmsg);
                           // TextView errorText = (TextView)boatSpinner.getSelectedView();
                           // errorText.setError("");
                           // errorText.setTextColor(Color.RED);//just to highlight that this is an error
                           // errorText.setText(errmsg);
                           // AppUtils.showAlertDialog(context, "Error", errmsg);
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
            mReloadTask = null;
            //showProgress(false);
        }
    }


}
