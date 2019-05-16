package com.asif.followme;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.asif.followme.util.AppConstants;


public class HelpActivity extends AppCompatActivity{
//    ProgressDialog dialog;
    private SwipeRefreshLayout swipeLayout;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_layout);
        webView = (WebView) findViewById(R.id.web_view);
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(AppConstants.HELP_URL);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, final String url) {
//                dialog.hide();
                swipeLayout.setRefreshing(false);
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                swipeLayout.setRefreshing(true);
//                dialog= ProgressDialog.show(HelpActivity.this, null, "Loading Help, Please wait...");
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

}
