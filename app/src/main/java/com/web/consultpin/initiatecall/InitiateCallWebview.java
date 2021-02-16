package com.web.consultpin.initiatecall;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.main.BaseActivity;


public class InitiateCallWebview extends BaseActivity {
    private ProgressBar viewprogressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initiate_call_webview);
        getSupportActionBar().hide();

        getIntent().getStringExtra(Utilclass.appointment_id);
        getIntent().getStringExtra(Utilclass.consultant_id);
        String url=getIntent().getStringExtra(Utilclass.videocall);
//System.out.println("Load url----"+url);



//        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
//        startActivity(browserIntent);

        WebView mywebview = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mywebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUserAgentString("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/80.0.3987.163 Chrome/80.0.3987.163 Safari/537.36");
        mywebview.loadUrl(url);


        viewprogressbar = findViewById(R.id.viewprogressbar);
        mywebview.setWebViewClient(new WebViewController());



    }

    public class WebViewController extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            viewprogressbar.setVisibility(View.VISIBLE);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            System.out.println("Pageurl===" + url);
            viewprogressbar.setVisibility(View.GONE);


            super.onPageFinished(view, url);

        }


        @Override
        public void onLoadResource(WebView view, String url) {

            super.onLoadResource(view, url);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }



}




