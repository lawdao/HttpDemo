package example.fussen.httpdemo.netutils;

import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import example.fussen.httpdemo.utils.UiUtils;

/**
 * Created by Fussen on 16/7/19.
 * <p/>
 * 访问网络 时只需调用此类的方法
 */
public class NetworkUtils {

    private static NetworkUtils mInstance;

    private static byte[] syncByte = new byte[0];

    public static NetworkUtils getInstance() {
        if (mInstance == null) {
            synchronized (syncByte) {
                if (mInstance == null) {
                    mInstance = new NetworkUtils();
                }
            }
        }
        return mInstance;
    }


    /**
     * 支持post 带参数的请求
     *
     * @param url      访问的地址
     * @param params   访问需要加的参数
     * @param callback 访问的回调
     */
    public void sendPostRequest(String url, Map<String, String> params, final RequestCallback callback) {
        if (UiUtils.hasNetwork()) {
            CustomRequest customRequest = new RequestBuilder().url(url).params(params).post().succesListener(new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());

                        String msg = getMsg(jsonObject);

                        if (validationStatusCode(jsonObject)) {
                            callback.onSuccess(response);
                        } else {
                            callback.onFail(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onFail("服务器异常");
                    }

                }
            }).errorListener(new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if (!TextUtils.isEmpty(error.getMessage())) {
                        callback.onFail(error.getMessage());
                    } else {
                        callback.onFail("服务器异常");
                    }
                }
            }).build();
            HttpClientRequest.getInstance().addRequest(customRequest);
        } else {
            callback.onFail("网络掉线了!");
        }
    }



    /**
     * 拿到服务器返回的信息
     * msg 是服务器返回的字段 自行可修改
     *
     * @param json
     * @return
     */
    private String getMsg(JSONObject json) {
        String msg = "";
        try {
            msg = json.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return msg;
    }


    /**
     * 拿到服务器返回的状态码判断是否访问成功
     * 成功返回true 否则返回false
     *
     * @param json
     * @return
     */
    private boolean validationStatusCode(JSONObject json) {
        if (getStatusCode(json) == 200) {
            return true;
        }
        return false;
    }


    /**
     * 解析返回码
     *
     * @param json
     * @return
     */
    private int getStatusCode(JSONObject json) {
        int status = 0;
        try {
            status = json.getInt("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return status;
    }
}
