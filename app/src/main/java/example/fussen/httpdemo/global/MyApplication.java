package example.fussen.httpdemo.global;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Fussen on 16/7/18.
 */
public class MyApplication extends Application {

    private static Context context;
    private static SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
    }


    public static Context getContext() {
        return context;
    }

    /**
     * 用于检测返回头中包含的cookie
     * 并且更新本地存储的cookie
     */
    public static final void checkSessionCookie(Map<String, String> headers) {
        String mHeader = headers.toString();
        //使用正则表达式从reponse的头中提取cookie内容的子串
        Pattern pattern = Pattern.compile("Set-Cookie.*?;");
        Matcher m = pattern.matcher(mHeader);
        if (m.find()) {
            String cookieFromResponse = m.group();
            //去掉cookie末尾的分号
            cookieFromResponse = cookieFromResponse.substring(11, cookieFromResponse.length() - 1);
            setCookie(cookieFromResponse);
        }
    }

    /**
     * 向请求头中加入cookie
     *
     * @param headers
     */
    public  static final void addSessionCookie(Map<String, String> headers) {
        String cookie = getCookie();
        if (cookie.length() > 0) {
            headers.put("Cookie", cookie);
        }
    }

    /**
     * 用sp存储cookie
     * @param cookieFromResponse
     */
    private static void setCookie(String cookieFromResponse) {
        sp.edit().putString("cookie", cookieFromResponse).commit();
    }



    private static String getCookie() {
        return sp.getString("cookie", "");
    }

}
