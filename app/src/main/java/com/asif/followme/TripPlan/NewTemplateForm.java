package com.asif.followme.TripPlan;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.asif.followme.R;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.util.AppConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 1/6/2018.
 */

public class NewTemplateForm extends Activity implements View.OnClickListener {
    public static String RESULT_IDS = "my_menu_ids";
    Context context;
    public Button saveBtn,cancelBtn;
//    public EditText share_email,share_email_new, share_email_edit;
//    public CheckBox checkBox_cl,checkBox_hs,checkBox_ds,checkBox_as;
    public TextView destinationsLabel;
    private Dialog sDialog;
    public static String shared_user_id,shared_user_email;
    public String action;
    int route_id;
    int client_id;
    String route_status="";
    String destinations ="[]";
    WebView webView;
    private saveRouteTask mRouteTask = null;
    private Spinner statusSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_new_template_form);
        context = this;

        saveBtn = (Button) findViewById(R.id.save_btn);
        cancelBtn = (Button) findViewById(R.id.cancel_btn);
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new NewTemplateForm.WebAppInterface(this), "Android");
        destinationsLabel = (TextView) findViewById(R.id.destinations_label);
        statusSpinner = (Spinner) findViewById(R.id.route_status_input);

        saveBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        this.setFinishOnTouchOutside(false);
    }
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        try{
            Bundle extras = getIntent().getExtras();
//          action = extras.getString("action");
            route_id = extras.getInt("route_id");
            client_id = extras.getInt("client_id");
            route_status = extras.getString("route_status");
            System.out.println("Route Status:"+route_status);
            if(route_status.equalsIgnoreCase("Draft")){
                statusSpinner.setSelection(0);
            } else {
                statusSpinner.setSelection(1);
            }
            webView.loadUrl(AppConstants.TRIP_PLAN_WEBVIEW_URL+"?route="+route_id);
            if(route_id > 0){
                setTitle("Edit Route");
            } else {
                setTitle("New Route");
            }
        } catch (Exception e){
            client_id=0;
           // System.out.println(e);
           // vname="";
        }

    }
    @Override
    public void onClick(View view) {
        System.out.println("Clicked:"+view.getId());
        switch (view.getId() /*to get clicked view id**/) {
            case R.id.save_btn:
                prepareSave();
                break;
            case R.id.cancel_btn:
                finish();
                break;
        }

    }
    public class WebAppInterface {
        Context mContext;

        WebAppInterface(Context ctx){
            this.mContext=ctx;
        }


        @JavascriptInterface
        public void sendData(String element, String data) {
            //Get the string value to process
            destinations=data;
            System.out.println("Destinations:"+destinations);
        }
    }

    public void prepareSave(){
        int routeStatus = 0;
        String route_status = statusSpinner.getSelectedItem().toString();
        if(route_status.equalsIgnoreCase("Active")){
            routeStatus = 2;
        } else {
            routeStatus = 1;
        }
        System.out.println("Route Statu:"+route_status);
        try {
            JSONArray destObject = new JSONArray(destinations);
            if(destObject.length() < 2){
                showError(destinationsLabel,"Please enter Departure and Arrival Islands");
                return;
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Please enter Destinations", Toast.LENGTH_LONG).show();
            return;
        }
        mRouteTask = new saveRouteTask("save", route_id, client_id, destinations, routeStatus);
        mRouteTask.execute((Void) null);

    }

    private void showError(TextView v,String error){
        v.setError(error);
        v.requestFocus();
    }

    public class saveRouteTask extends AsyncTask<Void, Void, String> {
        int route_id;
        int client_id;
        int route_status;
        String action;
        String route_data;
        saveRouteTask(String action, int route_id, int client_id, String route_data, int route_status) {
            this.action = action;
            this.route_id = route_id;
            this.route_status = route_status;
            this.client_id = client_id;
            this.route_data = route_data;

        }

        @Override
        protected String doInBackground(Void... arg0) {
            try {
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.saveRoutePlans(context, action, route_id, client_id, route_data, route_status);
            } catch (Exception ex) {
                //finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String result) {
            mRouteTask = null;
            //showProgress(false);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {

                            Intent intent = getIntent();
                            intent.putExtra("action", "reload");
                            setResult(RESULT_OK,intent );
                            finish();

                        } else {
                            String errmsg = jObj.getString("error");
                            //AppUtils.showAlertDialog(context, "Error", errmsg);
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
            mRouteTask = null;
            //showProgress(false);
        }
    }



    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public static boolean isNumeric(String str){
        return str.matches("\\d+?");  //match a number with optional '-' and decimal.
    }
}
