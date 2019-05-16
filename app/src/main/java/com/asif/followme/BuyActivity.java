package com.asif.followme;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.asif.followme.util.AppConstants;
import com.asif.followme.util.SettingsPreferences;


public class BuyActivity extends AppCompatActivity{
//    ProgressDialog dialog;
    private SwipeRefreshLayout swipeLayout;
    private String buy_url;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_layout);
        webView = (WebView) findViewById(R.id.web_view);
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        String country = SettingsPreferences.getCountry(this);
        if(country.equalsIgnoreCase("mv")) {
            buy_url = AppConstants.BUY_LOCAL_URL;
        } else {
            buy_url = AppConstants.BUY_WORLD_URL;
        }
        webView.loadUrl(buy_url);
//        loadPage(webView,buy_url);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                System.out.println("shouldOverrideUrlLoading: "+url);
                String ext = url.substring(url.length() - 3);
                if (ext.equalsIgnoreCase("pdf")){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(url), "application/pdf");
                    try{
                        view.getContext().startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        //user does not have a pdf viewer installed
                    }
                }
                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
                //dialog.hide();
                swipeLayout.setRefreshing(false);
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                swipeLayout.setRefreshing(true);
                //dialog= ProgressDialog.show(BuyActivity.this, null, "Loading Help, Please wait...");
            }
        });
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        webView.reload();
                    }
                }
        );
    }

    public boolean loadPDF(WebView view,String url) {
        System.out.println("loadPage:"+url);
        String ext = url.substring(url.length() - 3);
        if (ext.equalsIgnoreCase("pdf")){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(url), "application/pdf");
            try{
                view.getContext().startActivity(intent);
            } catch (ActivityNotFoundException e) {
                //user does not have a pdf viewer installed
            }
        }
        return true;
    }
}
