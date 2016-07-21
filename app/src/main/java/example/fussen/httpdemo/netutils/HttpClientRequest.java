package example.fussen.httpdemo.netutils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import example.fussen.httpdemo.utils.UiUtils;

/**
 * Created by Fussen on 16/7/19.
 */
public class HttpClientRequest {

    private static HttpClientRequest mInstance;

    private RequestQueue mRequestQueue;

    public HttpClientRequest() {

    }

    public static synchronized HttpClientRequest getInstance() {
        if (mInstance == null) {
            mInstance = new HttpClientRequest();
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(UiUtils.getContext(), new OkHttpStack());
        }
        return mRequestQueue;
    }

    /**
     * 取消所有请求
     *
     * @param tag
     */
    public void cancelAllRequest(String tag) {
        if (getRequestQueue() != null) {
            getRequestQueue().cancelAll(tag);
        }
    }


    public <T> void addRequest(Request<T> request, String tag) {
        request.setTag(tag);
        getRequestQueue().add(request);
    }

    /**
     * 添加一个请求
     * @param request
     * @param <T>
     */
    public <T> void addRequest(Request<T> request) {
        getRequestQueue().add(request);
    }

}
