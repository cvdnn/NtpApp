package com.xrj.demo.ntp;

import android.annotation.SuppressLint;
import android.assist.Shell;
import android.concurrent.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;

import com.xrj.ntp.Sntp;

import static android.assist.Shell.CommandResult.SUCCESS;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "NtpApp";

    private TextView mLabelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLabelView = findViewById(R.id.label);


        mLabelView.setText(DateFormat.format("当前时间：yyyy-MM-dd HH:mm:ss\n", System.currentTimeMillis()));
    }

    @UiThread
    public void onGetNtpClicked(View v) {
        Shell.CommandResult cmdResult = Sntp.Impl.setTimeMillis(System.currentTimeMillis() - 5 * 60 * 1000);

        mLabelView.append(String.format("--------------------\n[%s]: 更改时间：%s\n", cmdResult != null && cmdResult.result == SUCCESS ? "SUCCESS" : "ERROR",
                DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis())));
    }

    @SuppressLint("StaticFieldLeak")
    @UiThread
    public void onSetNtpClicked(View v) {
        new AsyncTask() {
            private Shell.CommandResult mCmdResult;

            @Override
            protected void doInBackground() {
                boolean ntpResult = Sntp.Impl.forceRefresh();
                if (ntpResult) {
                    mCmdResult = Sntp.Impl.setTimeMillis();
                }
            }

            @Override
            protected void onPostExecute() {
                if (mCmdResult != null && mCmdResult.result == SUCCESS) {
                    mLabelView.append(String.format("[%s]: 校正时间：%s", "SUCCESS",
                            DateFormat.format("yyyy-MM-dd HH:mm:ss\n", System.currentTimeMillis())));
                } else {
                    mLabelView.append(String.format("[ERROR]: %s", mCmdResult != null ? mCmdResult.errorMsg : "xxxxxxxxx"));
                }
            }
        }.start();
    }

    public void onLabelClear(View v) {
        mLabelView.setText(DateFormat.format("当前时间：yyyy-MM-dd HH:mm:ss\n", System.currentTimeMillis()));
    }
}
