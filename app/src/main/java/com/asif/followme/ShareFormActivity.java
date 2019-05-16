package com.asif.followme;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.asif.followme.MyAccount.MyBoatsFragment;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.util.SettingsPreferences;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 1/6/2018.
 */

public class ShareFormActivity extends Activity implements View.OnClickListener {
    public static String RESULT_IDS = "my_menu_ids";
    Context context;
    public Button shareNow,cancelShare;
    public EditText share_email,share_email_new, share_email_edit;
    public CheckBox checkBox_cl,checkBox_hs,checkBox_ds,checkBox_as,checkBox_tp, checkBox_bh;
    private CheckBox checkBox_t1,checkBox_t2,checkBox_t4;
    public TextView shareLabel1;
    private AsyncTask<Void, Void, String> SharedPostAsyncTask;
//    private AsyncTask<Void, Void, String> SharedReadAsyncTask;
    private Dialog sDialog;
    public static String shared_user_id,shared_user_email,action;
    private postSharedUser postTask = null;
    private getSharedUser getTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_form);
        context = this;

        shareNow = (Button) findViewById(R.id.shareButton);
        cancelShare = (Button) findViewById(R.id.cancelShare);
        share_email_new = (EditText) findViewById(R.id.share_email_new);
        share_email_edit = (EditText) findViewById(R.id.share_email_edit);
        checkBox_cl = (CheckBox) findViewById(R.id.checkBox_cl);    // current location
        checkBox_hs = (CheckBox) findViewById(R.id.checkBox_hs);    // history and stat
        checkBox_ds = (CheckBox) findViewById(R.id.checkBox_ds);    // device settings
        checkBox_as = (CheckBox) findViewById(R.id.checkBox_as);       // allow sharing
        checkBox_tp = (CheckBox) findViewById(R.id.checkBox_tp);    // trip plan
        checkBox_bh = (CheckBox) findViewById(R.id.checkBox_bh);    // boat hire

        checkBox_t1 = (CheckBox) findViewById(R.id.checkBox_t1);
        checkBox_t2 = (CheckBox) findViewById(R.id.checkBox_t2);
        checkBox_t4 = (CheckBox) findViewById(R.id.checkBox_t4);
        this.setFinishOnTouchOutside(false);

 //       share_email = new EditText(new ContextThemeWrapper(this, R.style.App_EditTextDisable), null, 0);
 //       shareLabel1 = (TextView) findViewById(R.id.share_label1);
        shareNow.setOnClickListener(this);
        cancelShare.setOnClickListener(this);
    }
    @Override
    public void onStart() {
        super.onStart();
        String vname = "Opt...";
        try{
            Bundle extras = getIntent().getExtras();
            vname = extras.getString("vname");
           // String device_id= SettingsPreferences.getSelectedDevice(context);
            shared_user_email = extras.getString("user_email");
            shared_user_id = extras.getString("user_id");
            if(shared_user_email.equalsIgnoreCase("")){
                share_email_edit.setVisibility(View.GONE);
                share_email_new.setVisibility(View.VISIBLE);
                share_email = share_email_new;
                checkBox_cl.setChecked(true);
                checkBox_hs.setChecked(true);
                setTitle("New Share");

            } else {
                share_email_edit.setVisibility(View.VISIBLE);
                share_email_new.setVisibility(View.GONE);
                share_email = share_email_edit;
                setTitle(vname);
                getTask = new getSharedUser(context);
                getTask.execute((Void) null);

            }


        } catch (Exception e){
            vname="";
        }

    }
    @Override
    public void onClick(View view) {
        System.out.println("Clicked:"+view.getId());
        switch (view.getId() /*to get clicked view id**/) {
            case R.id.shareButton:
                if(!isValidEmail(share_email.getText())){
                    share_email.setError("Please Enter a valid Email address");
                    share_email.requestFocus();
                    return;
                }
                int access = 0;
                int trip_access = 0;
                String user_email = share_email.getText().toString();
                if(checkBox_cl.isChecked()){access+=1;}
                if(checkBox_hs.isChecked()){access+=2;}
                if(checkBox_tp.isChecked()){access+=4;}
                if(checkBox_ds.isChecked()){access+=8;}
                if(checkBox_as.isChecked()){access+=16;}
                if(checkBox_bh.isChecked()){access+=32;}
                if(checkBox_t1.isChecked()){trip_access+=1;}
                if(checkBox_t2.isChecked()){trip_access+=2;}
                if(checkBox_t4.isChecked()){trip_access+=4;}

                String device_id= MyBoatsFragment.selected_item_id;
                postTask = new postSharedUser(user_email,device_id,access,trip_access,shared_user_id);
                postTask.execute((Void) null);

                break;
            case R.id.cancelShare:
                finish();
                break;
        }

    }

    public class postSharedUser extends AsyncTask<Void, Void, String> {
        private final String email;
        private final String device_id;
        private final int access;
        private final int trip_access;
        private final  String shared_user;
        private Context con;

        postSharedUser(String email,String device_id, int access, int trip_access, String shared_user) {
            this.email = email;
            this.device_id = device_id;
            this.access = access;
            this.trip_access = trip_access;
            this.shared_user = shared_user;

        }
        @Override
        protected void onPreExecute() {
        //    swipeLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);
        //    swipeLayout.setRefreshing(true);
            sDialog= ProgressDialog.show(ShareFormActivity.this, null, "Posting, Please wait...");
            sDialog.show();

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... arg0) {
            try{
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.postSharedUser(context,email,device_id,access,trip_access,shared_user);
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onCancelled() {
            if(sDialog.isShowing()){
                sDialog.dismiss();
            }
//			System.out.println("Cancelled.....................");
//		    onMyAsyncTaskCompletedListener(null);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(sDialog.isShowing()){
                sDialog.dismiss();
            }
            //System.out.println(result);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        if (jObj.getString("status").equalsIgnoreCase("ok")) {
                            Intent intent = new Intent();
                            intent.putExtra("reload", true);
                            setResult(RESULT_OK, intent);
                            finish();

                        } else {
                            share_email_new.setError(jObj.getString("error"));
                            //AppUtils.showAlertDialog(getApplicationContext(), "Error", jObj.getString("error"));
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                        //	System.out.println("Result"+result);

                    }
                } catch (JSONException e) {
                    //loading.setText("Unable to reach Server...");
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

            }
        }
    }



    public class getSharedUser extends AsyncTask<Void, Void, String> {
        getSharedUser(Context context){

        }
            @Override
            protected void onPreExecute() {
                sDialog =ProgressDialog.show(context, null, "Please wait...");
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(Void... arg0) {
                try{
                    System.out.println("Device ID: "+ SettingsPreferences.getSelectedItemID(context)+", Shared User: "+SettingsPreferences.getSelectedSharedUser(context));
                    ContentParser parser = new ContentParser(getBaseContext());
                    return parser.readSharedUser(context,SettingsPreferences.getSelectedItemID(context),SettingsPreferences.getSelectedSharedUser(context));
                    //GetText();
                }
                catch(Exception ex)
                {
                    //finish();
//				System.out.println("err??????????????????????");
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if(sDialog.isShowing()){
                    sDialog.dismiss();
                }
                System.out.println(result);
                //		SettingsPreferences.setSelectedAccount(context, id2);
                if (!TextUtils.isEmpty(result)) {
                    try {
                        JSONObject jObj = new JSONObject(result);
                        if (jObj.has("status")) {
                            if (jObj.getString("status").equalsIgnoreCase("ok")) {
                                int access=jObj.getInt("access");
                                int trip_access = jObj.getInt("trip");
                                //share_email.setText(access);
								//share_email = (EditText) shareDialog.findViewById(R.id.share_email);
								share_email_edit.setText(shared_user_email);
								//share_email.setTextAppearance(R.style.App_EditTextDisable);
								share_email_edit.setEnabled(false);

	//							shareLabel1.setVisibility(View.GONE);

                                if((access & 1) == 1) {checkBox_cl.setChecked(true);} else {checkBox_cl.setChecked(false);}     // current location
                                if((access & 2) == 2) {checkBox_hs.setChecked(true);} else {checkBox_hs.setChecked(false);}     // history and stat
                                if((access & 4) == 4) {checkBox_tp.setChecked(true);} else {checkBox_tp.setChecked(false);}     // trip plan
                                if((access & 8) == 8) {checkBox_ds.setChecked(true);} else {checkBox_ds.setChecked(false);}     // device settings
                                if((access & 16) == 16) {checkBox_as.setChecked(true);} else {checkBox_as.setChecked(false);}   //allow sharing
                                if((access & 32) == 32) {checkBox_bh.setChecked(true);} else {checkBox_bh.setChecked(false);}   // boat hire & bidding

                                if((trip_access & 1) == 1) {checkBox_t1.setChecked(true);} else {checkBox_t1.setChecked(false);}
                                if((trip_access & 2) == 2) {checkBox_t2.setChecked(true);} else {checkBox_t2.setChecked(false);}
                                if((trip_access & 4) == 4) {checkBox_t4.setChecked(true);} else {checkBox_t4.setChecked(false);}
                                //                      shareDialog.setTitle("Change Permission");
         //                       shareDialog.show();

                            } else {
                                //AppUtils.showAlertDialog(context, "Error", jObj.getString("error"));
                                //showAlert(context,"Error",jObj.getString("error"));
                                //alert.show();
                                Toast.makeText(getApplicationContext(), jObj.getString("error"), Toast.LENGTH_LONG).show();
                            }


                        } else {
                            Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

                        }
                    } catch (JSONException e) {
                        //loading.setText("Unable to reach Server...");
                        Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

                }
            }
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
