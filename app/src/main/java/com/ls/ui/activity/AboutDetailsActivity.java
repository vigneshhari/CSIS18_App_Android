package com.ls.ui.activity;

import android.view.View;
import android.webkit.WebViewClient;
import com.ls.drupalcon.R;
import com.ls.drupalcon.model.Model;
import com.ls.drupalcon.model.UpdateRequest;
import com.ls.drupalcon.model.UpdatesManager;
import com.ls.utils.AnalyticsManager;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ScrollView;

import com.ls.utils.WebviewUtils;

import java.util.List;

public class AboutDetailsActivity extends StateActivity {

    public static final String EXTRA_DETAILS_ID = "EXTRA_DETAILS_ID";
    public static final String EXTRA_DETAILS_CONTENT = "EXTRA_DETAILS_CONTENT";
    public static final String EXTRA_DETAILS_TITLE = "EXTRA_SCHEDULE_NAME";

    private UpdatesManager.DataUpdatedListener updateListener = new UpdatesManager.DataUpdatedListener() {
        @Override
        public void onDataUpdated( List<UpdateRequest> requests) {
            Log.d("UPDATED", "AboutDetailsActivity");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_about_detail);

        String aboutTitle = getIntent().getStringExtra(EXTRA_DETAILS_TITLE);
        String aboutContent = getIntent().getStringExtra(EXTRA_DETAILS_CONTENT);
        long id = getIntent().getLongExtra(EXTRA_DETAILS_ID, -1);
        initView(aboutContent);
        initToolbar(aboutTitle);
        Model.instance().getUpdatesManager().registerUpdateListener(updateListener);

        AnalyticsManager.detailsScreenTracker(this, R.string.about, aboutTitle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Model.instance().getUpdatesManager().unregisterUpdateListener(updateListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView(String content) {
        ScrollView scrollView = (ScrollView) findViewById(R.id.scroll_view);
        View layoutPlaceholder = findViewById(R.id.layout_placeholder);
        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        if (content == null || content.isEmpty()) {
            scrollView.setVisibility(View.GONE);
            layoutPlaceholder.setVisibility(View.VISIBLE);
        } else {
            scrollView.setVisibility(View.VISIBLE);
            layoutPlaceholder.setVisibility(View.GONE);

            String html = content ;
            webView.loadData( html, "text/html", "UTF-8");
            webView.setWebViewClient(new WebViewClient()
            {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url)
                {
                    WebviewUtils.openUrl(AboutDetailsActivity.this, url);
                    return true;
                }
            });
        }
    }

    private void initToolbar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        if (toolbar != null) {
            toolbar.setTitle(title);
            setSupportActionBar(toolbar);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }
}
