package com.xrj.ntp;

import android.assist.Shell;
import android.content.Context;
import android.reflect.ClazzLoader;
import android.text.format.DateFormat;

public final class Sntp {
    public static final String TAG = "Sntp";

    public static final String NTP_SERVER = "ntp_server";
    public static final String NTP_ADDRESS = "ntp3.aliyun.com";

    public static Sntp Impl = new Sntp();

    private final SntpClient mSntpClient = new SntpClient();

    private Context mContext;

    private String mNtpAddress = NTP_ADDRESS;
    private int mTimeout;


    public void load(Context ctx) {
        mContext = ctx;

        Class rcls = ClazzLoader.forName("com.android.internal.R$integer");
        int timeoutRid = ClazzLoader.getFieldValue(rcls, "config_ntpTimeout");
        mTimeout = mContext.getResources().getInteger(timeoutRid);
    }

    public SntpClient client() {
        return mSntpClient;
    }

    public boolean forceRefresh() {

        return mSntpClient.requestTime(mNtpAddress, mTimeout);
    }

    public Shell.CommandResult setTimeMillis(long time) {
        return Shell.execute(String.format("date -s %s",
                DateFormat.format("yyyyMMdd.HHmmss", time)), true);
    }

    public Shell.CommandResult setTimeMillis() {
        return setTimeMillis(client().getNtpTime());
    }

    public String getNtpAddress() {
        return mNtpAddress;
    }

    public Sntp setNtpAddress(String ntp) {
        mNtpAddress = ntp;

        return this;
    }

    public int getTimeout() {
        return mTimeout;
    }

    public Sntp setTimeout(int timeout) {
        mTimeout = timeout;

        return this;
    }
}
