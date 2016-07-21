package example.fussen.httpdemo.netutils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import example.fussen.httpdemo.global.MyApplication;

/**
 * Created by Fussen on 16/7/18.
 * 自定义请求
 */
public class CustomRequest extends Request<JSONObject> {
    private final Map<String, String> headers;
    private final Response.Listener<JSONObject> listener;
    private Map<String, String> params;

    public CustomRequest(String url, Map<String, String> params, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.headers = params;
        this.params = params;
        this.listener = listener;
    }

    public CustomRequest(int method, String url, Response.ErrorListener errorListener, Map<String, String> headers, Response.Listener<JSONObject> listener, Map<String, String> params) {
        super(method, url, errorListener);
        this.headers = headers;
        this.listener = listener;
        this.params = params;
    }

    public CustomRequest(RequestBuilder requestBuilder) {
        super(requestBuilder.method, requestBuilder.url, requestBuilder.errorListener);
        this.headers = requestBuilder.headers;
        this.params = requestBuilder.params;
        this.listener = requestBuilder.successListener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        //在此处可以向请求头中加入cookie

        MyApplication.addSessionCookie(headers);
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    public Map<String, String> getParams() {

        //如果没有参数的话,此处传入默认的参数,不传就会报错

        if (params == null) {
            params = new HashMap<>();
            params.put("default_key", "default_value");
        }
        return params;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            //此处检测返回头中包含的cookie
            MyApplication.checkSessionCookie(response.headers);

            return Response.success(new JSONObject(parsed), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        listener.onResponse(response);
    }
}
