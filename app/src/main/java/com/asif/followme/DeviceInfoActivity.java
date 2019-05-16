package com.asif.followme;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.asif.followme.MyAccount.MyBoatsFragment;
import com.asif.followme.manager.ContentParser;

/**
 * Created by user on 1/26/2018.
 */

public class DeviceInfoActivity extends AppCompatActivity {
    ProgressDialog dialog;
    private AsyncTask<Void, Void, String> InfoAsyncTask;
    Context context;
    WebView webView;
    ProgressDialog Dialog;
    private deviceInfoTask mInfoTask = null;
    SwipeRefreshLayout swipeLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_layout);
        webView = (WebView) findViewById(R.id.web_view);
        context=this;
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);
        swipeLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mInfoTask = new deviceInfoTask(MyBoatsFragment.selected_item_id);
                        mInfoTask.execute((Void) null);
                    }
                }
        );
        mInfoTask = new deviceInfoTask(MyBoatsFragment.selected_item_id);
        mInfoTask.execute((Void) null);
    }

    public class deviceInfoTask extends AsyncTask<Void, Void, String> {
        private final String device_id;

        deviceInfoTask(String id) {
            this.device_id = id;
        }

        @Override
        protected void onPreExecute() {
            //loading.setText("Attempting to Authenticate...");
        //    dialog=ProgressDialog.show(DeviceInfoActivity.this, null, "Loading, Please wait...");
            swipeLayout.setRefreshing(true);

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... arg0) {
            try{
                //webView.loadUrl("http://followme.mv/android/v51/device_info.php?id="+device_id);
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.getDeviceInfo(context,device_id);
            }
            catch(Exception ex)
            {
                //finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
           // System.out.println(result);
           // dialog.hide();
            swipeLayout.setRefreshing(false);
            if (!TextUtils.isEmpty(result)) {
                webView.setWebViewClient(new WebViewClient(){

                    @Override
                    public void onPageFinished(WebView view, final String url) {
                        dialog.hide();
                    }
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        dialog=ProgressDialog.show(DeviceInfoActivity.this, null, "Loading, Please wait...");
                    }
                });

                webView.loadDataWithBaseURL("", result, "text/html", "UTF-8", "");
            } else {
                //AppUtils.showAlertDialog(context, "Timeout", "Unable to reach Server. Please try again!");
                //	Toast.makeText(getApplicationContext(), "Timed Out?", Toast.LENGTH_LONG).show();
                //	System.out.println("Timed Out?");
            }
        }

        @Override
        protected void onCancelled() {
            mInfoTask = null;
            swipeLayout.setRefreshing(false);
            //showProgress(false);
        }
    }

}
