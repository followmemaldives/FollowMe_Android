package com.asif.followme.PublicBoats;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.asif.followme.R;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.util.SettingsPreferences;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 1/6/2018.
 */

public class PublicNoticeActivity extends Activity {
    public static String RESULT_IDS = "my_menu_ids";
    Context context;
    private LinearLayout btnLayout;
    //    public EditText share_email,share_email_new, share_email_edit;
//    public CheckBox checkBox_cl,checkBox_hs,checkBox_ds,checkBox_as;
    public TextView noticeLabel, noticeDate;
    public EditText noticeInput;
    private AsyncTask<Void, Void, String> SharedPostAsyncTask;
    //    private AsyncTask<Void, Void, String> SharedReadAsyncTask;
    private Dialog sDialog;
    private NoticeTask postTask = null;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_form);
        context = this;

        btnLayout = (LinearLayout) findViewById(R.id.btn_layout);

        //       share_email = new EditText(new ContextThemeWrapper(this, R.style.App_EditTextDisable), null, 0);
        noticeLabel = (TextView) findViewById(R.id.notice_label);
        noticeInput = (EditText) findViewById(R.id.notice_input);
        noticeDate = (TextView) findViewById(R.id.notice_date);
        progressBar = (ProgressBar)  findViewById(R.id.loading_spinner);
    }
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        try{
            Bundle extras = getIntent().getExtras();
            setTitle(extras.getString("v_name"));
                btnLayout.setVisibility(View.GONE);
                noticeDate.setVisibility(View.VISIBLE);
                noticeInput.setVisibility(View.GONE);
                noticeLabel.setVisibility(View.VISIBLE);


            NoticeTask noticeTask=new NoticeTask(SettingsPreferences.getSelectedItemID(context), "read", "");
            noticeTask.execute();



        } catch (Exception e){
            // System.out.println(e);
            // vname="";
        }

    }

    public class NoticeTask extends AsyncTask<Void, Void, String> {
        private final String device_id;
        private final String notice;
        private final String action;

        NoticeTask(String device_id, String act, String notice) {
            this.action = act;
            this.device_id=device_id;
            this.notice = notice;
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
            // TODO Auto-generated method stub
            try{
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.getNotice(context,device_id,action, notice);
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
                                noticeLabel.setText(jObj.getString("notice").toString());
                                noticeInput.setText(jObj.getString("notice").toString());
                                noticeDate.setText(jObj.getString("foot"));
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
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {
                //Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

            }
        }
    }


}
