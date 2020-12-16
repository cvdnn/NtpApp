package android.edge.ntp;

import android.assist.Shell;
import android.content.Context;
import android.reflect.Clazz;
import android.reflect.ClazzLoader;
import android.text.format.DateFormat;

public final class Sntp {
    public static final String TAG = "Sntp";

    public static final String NTP_SERVER = "ntp_server";
    public static final String NTP_ADDRESS = "ntp3.aliyun.com";

    public final static Sntp Impl = new Sntp();

    private final SntpClient mSntpClient = new SntpClient();

    private Context mContext;

    private String mNtpAddress = NTP_ADDRESS;
    private int mTimeout;


    protected void load(Context ctx) {
        mContext = ctx;

        Class rcls = Clazz.forName("com.android.internal.R$integer");
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
        Shell.CommandResult result = Shell.execute(String.format("date -s %s", DateFormat.format("yyyyMMdd.HHmmss", time)), true);
        if (!Shell.CommandResult.success(result)) {
            // 尝试第2种方法
            String cmd = String.format("date %s", DateFormat.format("MMddHHmmyyyy", Sntp.Impl.client().getNtpTime()));
            result = Shell.execute(cmd, true);
        }

        return result;
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

    public static final class Initializer extends AbstractInitializer {

        @Override
        public boolean onCreate() {
            Impl.load(getContext());

            return true;
        }
    }
}
