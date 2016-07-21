package example.fussen.httpdemo.netutils;


import com.android.volley.Request;
import com.android.volley.Response;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fussen on 16/7/18.
 */
public class RequestBuilder {

    public String url;

    public int method = Request.Method.GET;
    public Response.Listener successListener;

    public Response.ErrorListener errorListener;

    public Map<String, String> headers;
    public Map<String, String> params;

    public RequestBuilder url(String url) {
        this.url = url;
        return this;
    }

    public RequestBuilder succesListener(Response.Listener successListener) {
        this.successListener = successListener;
        return this;
    }

    public RequestBuilder errorListener(Response.ErrorListener errorListener) {
        this.errorListener = errorListener;
        return this;
    }

    public RequestBuilder post() {
        this.method = Request.Method.POST;
        return this;
    }

    public RequestBuilder get() {
        this.method = Request.Method.GET;
        return this;
    }


    public RequestBuilder put() {
        this.method = Request.Method.PUT;
        return this;
    }

    /**
     * 自定义访问网络的方式
     *
     * @param method
     * @return
     */
    public RequestBuilder method(int method) {
        this.method = method;
        return this;
    }

    /**
     * 添加头
     *
     * @param key
     * @param value
     * @return
     */
    public RequestBuilder addHeaders(String key, String value) {
        if (headers == null)
            headers = new HashMap<>();
        headers.put(key, value);
        return this;
    }

    /**
     * 添加参数
     *
     * @param key
     * @param value
     * @return
     */
    public RequestBuilder addParams(String key, String value) {
        if (params == null)
            params = new HashMap<>();
        params.put(key, value);
        return this;
    }


    public RequestBuilder headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public RequestBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public CustomRequest build() {

        //在这里 加入 固定头,也可以加入cookie
        Map<String, String> headers = new HashMap<>();
        //headers.put("key", "value");
        headers.put("BODY-X-TYPE", "2");
        headers.put("BODY-X-VERSION", "1.0");
        headers(headers);

        if (method != Request.Method.POST) {
            StringBuffer sb = new StringBuffer();

            sb.append(url);

            if (params != null && !params.isEmpty()) {
                sb.append("?");

                try {
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        sb.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(), "UTF-8")).append('&');
                    }
                    sb.deleteCharAt(sb.length() - 1);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            url = sb.toString();
        }
        return new CustomRequest(this);
    }
}
