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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.asif.followme.MyAccount.MyGroupsFragment;
import com.asif.followme.manager.ContentParser;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 1/6/2018.
 */

public class GroupDeviceFormActivity extends Activity implements View.OnClickListener {
    public static String RESULT_IDS = "my_menu_ids";
    Context context;
    public Button addDeviceBtn,cancelShare;
    public EditText device_id_input;
//    public EditText share_email,share_email_new, share_email_edit;
//    public CheckBox checkBox_cl,checkBox_hs,checkBox_ds,checkBox_as;
    public TextView shareLabel1;
    private AsyncTask<Void, Void, String> SharedPostAsyncTask;
//    private AsyncTask<Void, Void, String> SharedReadAsyncTask;
    private Dialog sDialog;
    public static String shared_user_id,shared_user_email,action;
    private ChangeGroup postTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_device_form);
        context = this;

        addDeviceBtn = (Button) findViewById(R.id.addDeviceBtn);
        cancelShare = (Button) findViewById(R.id.cancelShare);
        device_id_input = (EditText) findViewById(R.id.group_device_id);

 //       share_email = new EditText(new ContextThemeWrapper(this, R.style.App_EditTextDisable), null, 0);
 //       shareLabel1 = (TextView) findViewById(R.id.share_label1);
        addDeviceBtn.setOnClickListener(this);
        cancelShare.setOnClickListener(this);
    }
    @Override
    public void onStart() {
        super.onStart();
       // String vname = "Opt...";
        try{
         //   Bundle extras = getIntent().getExtras();
         //   vname = extras.getString("vname");
            setTitle(MyGroupsFragment.selected_item_name);
           // String device_id= SettingsPreferences.getSelectedDevice(context);
          //  shared_user_email = extras.getString("user_email");
          //  shared_user_id = extras.getString("user_id");
          //  if(shared_user_email.equalsIgnoreCase("")){


          //  } else {

          //      getTask = new getSharedUser(context);
          //      getTask.execute((Void) null);

          //  }


        } catch (Exception e){
           // vname="";
        }

    }
    @Override
    public void onClick(View view) {
        System.out.println("Clicked:"+view.getId());
        switch (view.getId() /*to get clicked view id**/) {
            case R.id.addDeviceBtn:
                String group_device_id = device_id_input.getText().toString();
                if (isNumeric(group_device_id)) {
                    //String device_id=MyActivity.selected_item_id;
                    postTask = new ChangeGroup("add", MyGroupsFragment.selected_item_id, group_device_id);
                    postTask.execute((Void) null);

                    break;
                } else {
                    device_id_input.setError("Please Enter a valid Device ID");
                    device_id_input.requestFocus();
                    return;
                }
            case R.id.cancelShare:
                finish();
                break;
        }

    }
    public class ChangeGroup extends AsyncTask<Void, Void, String> {
        private final int fleet_id;
        private final int device_id;
        private final String action;

        ChangeGroup(String act, String fid, String did) {
            action = act;
            fleet_id=Integer.parseInt(fid);
            device_id = Integer.parseInt(did);
        }
        @Override
        protected void onPreExecute() {
           // swipeLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);
           // swipeLayout.setRefreshing(true);
            sDialog= ProgressDialog.show(GroupDeviceFormActivity.this, null, "Posting, Please wait...");
            sDialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... arg0) {
            try{
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.changeFleet(context,action,fleet_id, device_id);
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
//           swipeLayout.setRefreshing(false);
//			System.out.println("Cancelled.....................");
//		    onMyAsyncTaskCompletedListener(null);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(sDialog.isShowing()){
                sDialog.dismiss();
            }
//            swipeLayout.setRefreshing(false);
            //System.out.println(result);
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
                            device_id_input.setError(jObj.getString("error"));
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


    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public static boolean isNumeric(String str){
        return str.matches("\\d+?");  //match a number with optional '-' and decimal.
    }
}
