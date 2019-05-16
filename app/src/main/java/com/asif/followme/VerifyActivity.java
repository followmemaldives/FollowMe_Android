package com.asif.followme;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.asif.followme.manager.ContentParser;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 1/6/2018.
 */

public class VerifyActivity extends AppCompatActivity implements View.OnClickListener {
    public static String RESULT_IDS = "my_menu_ids";
    Context context;
    public Button saveBtn,cancelBtn;
    private LinearLayout btnLayout;
    //    public EditText share_email,share_email_new, share_email_edit;
//    public CheckBox checkBox_cl,checkBox_hs,checkBox_ds,checkBox_as;
//    public TextView noticeLabel, noticeDate;
    public EditText nameInput, contactInput;
    private AsyncTask<Void, Void, String> SharedPostAsyncTask;
    //    private AsyncTask<Void, Void, String> SharedReadAsyncTask;
    private Dialog sDialog;
    private VerifyTask verifyTask = null;
    private ProgressBar progressBar;
    private LinearLayout layoutStep1, layoutStep2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_form);
        context = this;
        this.setFinishOnTouchOutside(false);

        saveBtn = (Button) findViewById(R.id.save_btn);
        cancelBtn = (Button) findViewById(R.id.cancel_btn);
        btnLayout = (LinearLayout) findViewById(R.id.btn_layout);
        layoutStep1 = (LinearLayout) findViewById(R.id.layout_step1);
        layoutStep2 = (LinearLayout) findViewById(R.id.layout_step2);
        layoutStep2.setVisibility(View.GONE);

        //       share_email = new EditText(new ContextThemeWrapper(this, R.style.App_EditTextDisable), null, 0);
        nameInput = (EditText) findViewById(R.id.display_name);
        contactInput = (EditText) findViewById(R.id.display_contact);
        progressBar = (ProgressBar)  findViewById(R.id.loading_spinner);
        saveBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }
    @Override
    public void onStart() {
        super.onStart();

        VerifyTask verifyTask=new VerifyTask("read", "", "");
        verifyTask.execute();

    }
    @Override
    public void onClick(View view) {
        System.out.println("Clicked:"+view.getId());
        switch (view.getId() /*to get clicked view id**/) {
            case R.id.save_btn:
                if(nameInput.getText().toString().length()<3){
                    nameInput.setError("Please Enter Display Name");
                    return;
                }
                if(contactInput.getText().toString().length()<8){
                    contactInput.setError("Please enter contact with country code");
                    return;
                }
                VerifyTask verifyTask=new VerifyTask("send_sms", nameInput.getText().toString(),contactInput.getText().toString());
                verifyTask.execute();
                break;
            case R.id.cancel_btn:
                finish();
                break;
        }

    }

    public class VerifyTask extends AsyncTask<Void, Void, String> {
        private final String name;
        private final String contact;
        private final String action;

        VerifyTask(String action, String name, String contact) {
            this.action = action;
            this.name = name;
            this.contact = contact;
        }
        @Override
        protected void onPreExecute() {
            // swipeLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);
            // swipeLayout.setRefreshing(true);
            //sDialog= ProgressDialog.show(NoticeActivity.this, null, "Please wait...");
            //sDialog.show();
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... arg0) {
            try{
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.verifyUser(context,action, name, contact);
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onCancelled() {
            // if(sDialog.isShowing()){
            //     sDialog.dismiss();
            // }
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //if(sDialog.isShowing()){
            //     sDialog.dismiss();
            //}
            progressBar.setVisibility(View.GONE);
//            swipeLayout.setRefreshing(false);
            //System.out.println(result);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        if (jObj.getString("status").equalsIgnoreCase("ok")) {
                            System.out.println("Action:"+action);
                            if(action.equalsIgnoreCase("read")) {
                                System.out.println("A");
                                nameInput.setText(jObj.getString("name").toString());
                                contactInput.setText(jObj.getString("contact").toString());
                            } else {
                                finish();
                            }
                        } else {
                            //  notice_input.setError(jObj.getString("error"));
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
                //Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

            }
        }
    }


}
