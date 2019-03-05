package com.mercadopago.android.px.internal.features;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.base.PXActivity;
import com.mercadopago.android.px.tracking.internal.views.TermsAndConditionsViewTracker;

public class TermsAndConditionsActivity extends PXActivity {

    public static final String EXTRA_URL = "extra_url";

    public static void start(@NonNull final Context context, @NonNull final String url) {
        final Intent intent = new Intent(context, TermsAndConditionsActivity.class);
        intent.putExtra(EXTRA_URL, url);
        context.startActivity(intent);
    }

    private View mMPTermsAndConditionsView;
    private WebView mTermsAndConditionsWebView;
    private ViewGroup mProgressLayout;
    private String url;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.px_activity_terms_and_conditions);

        url = getIntent().getStringExtra(EXTRA_URL);

        if (savedInstanceState == null) {
            new TermsAndConditionsViewTracker(url).track();
        }

        mProgressLayout = findViewById(R.id.mpsdkProgressLayout);
        mMPTermsAndConditionsView = findViewById(R.id.mpsdkMPTermsAndConditions);
        mTermsAndConditionsWebView = findViewById(R.id.mpsdkTermsAndConditionsWebView);
        mTermsAndConditionsWebView.setVerticalScrollBarEnabled(true);
        mTermsAndConditionsWebView.setHorizontalScrollBarEnabled(true);
        initializeToolbar();
        showMPTermsAndConditions();
    }

    private void initializeToolbar() {
        final Toolbar mToolbar = findViewById(R.id.mpsdkToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void showMPTermsAndConditions() {
        mProgressLayout.setVisibility(View.VISIBLE);
        mTermsAndConditionsWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                mProgressLayout.setVisibility(View.GONE);
                mMPTermsAndConditionsView.setVisibility(View.VISIBLE);
            }
        });

        mTermsAndConditionsWebView.loadUrl(url);
    }
}
