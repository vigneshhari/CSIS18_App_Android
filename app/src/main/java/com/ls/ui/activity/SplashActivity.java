package com.ls.ui.activity;

import com.ls.drupalcon.R;
import com.ls.drupalcon.model.Model;
import com.ls.drupalcon.model.PreferencesManager;
import com.ls.drupalcon.model.UpdateCallback;
import com.ls.drupalcon.model.UpdatesManager;
import com.ls.drupalcon.model.managers.SharedScheduleManager;
import com.ls.drupalcon.model.managers.ToastManager;
import com.ls.ui.dialog.NoConnectionDialog;
import com.ls.util.L;
import com.ls.utils.NetworkUtils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 1500;
    private Handler mHandler;


    public static void startThisActivity(Activity activity) {
        Intent intent = new Intent(activity, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_splash);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Model.instance().getSharedScheduleManager().initialize();
            }
        }).run();

        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startSplash();
            }
        }, SPLASH_DURATION);


    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private void startSplash() {
        String lastUpdate = PreferencesManager.getInstance().getLastUpdateDate();
        boolean isOnline = NetworkUtils.isOn(SplashActivity.this);
        if (isOnline) {
            checkForUpdates();
        } else if (TextUtils.isEmpty(lastUpdate)) {
            showNoNetworkDialog();
        } else {
            startMainActivity();
        }
    }

    private void checkForUpdates() {
        new AsyncTask<Void, Void, UpdatesManager>() {
            @Override
            protected UpdatesManager doInBackground(Void... params) {
                UpdatesManager manager = Model.instance().getUpdatesManager();
                manager.checkForDatabaseUpdate();
                return manager;
            }

            @Override
            protected void onPostExecute(UpdatesManager manager) {
                loadData(manager);
            }
        }.execute();

    }

    private void loadData(UpdatesManager manager) {
        manager.startLoading(new UpdateCallback() {
            @Override
            public void onDownloadSuccess() {
                L.d("onDownloadSuccess");
                startMainActivity();
            }

            @Override
            public void onDownloadError() {
                L.d("onDownloadError");
                showNoNetworkDialog();
            }
        });
    }

    private void startMainActivity() {
        Uri data = this.getIntent().getData();
        if (data != null && data.isHierarchical()) {
            String uriString = this.getIntent().getDataString();
            Uri uri = Uri.parse(uriString);
            String codeString = uri.getQueryParameter("code");
            if(TextUtils.isEmpty(codeString)){
                ToastManager.message(this, getString(R.string.url_is_corrupted));
                HomeActivity.startThisActivity(this, SharedScheduleManager.MY_DEFAULT_SCHEDULE_CODE);
            }else {
                Long code = Long.valueOf(codeString);
                HomeActivity.startThisActivity(this, code);
            }
        } else {
            HomeActivity.startThisActivity(this, SharedScheduleManager.MY_DEFAULT_SCHEDULE_CODE);
        }

        finish();
    }

    private void showNoNetworkDialog() {
        if (!isFinishing()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(new NoConnectionDialog(), NoConnectionDialog.TAG);
            ft.commitAllowingStateLoss();
        }
    }
}
