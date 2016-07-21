package example.fussen.httpdemo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import example.fussen.httpdemo.global.MyApplication;

/**
 * 提供ui操作的帮助类
 *
 * @author Fussen
 */
public class UiUtils {

    // 返回context对象
    public static Context getContext() {
        return MyApplication.getContext();
    }


    /**
     * 是否有可用网络
     */
    public static boolean hasNetwork() {
        boolean result = false;
        ConnectivityManager cm = (ConnectivityManager) UiUtils.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected()) {
            result = true;
        }
        return result;
    }

}
