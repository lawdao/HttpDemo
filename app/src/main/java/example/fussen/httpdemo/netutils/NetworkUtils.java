package example.fussen.httpdemo.netutils;

import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Map;

import example.fussen.httpdemo.utils.UiUtils;

/**
 * Created by Fussen on 16/7/19.
 *
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
     * 上传单个文件 不带params
     * @param url   上传地址
     * @param filePartName 服务端定义上传文件的参数名字
     * @param file 要上传的文件
     * @param callback
     */
    public void uploadFile(String url, String filePartName, File file, final RequestCallback callback){
        if(UiUtils.hasNetwork()){

            Request request = new MultipartRequest(url, filePartName, file, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject json = new JSONObject(s);
                        String msg = getMsg(json);
                        if(validationStatusCode(json)){
                            callback.onSuccess(json);
                        }else{
                            callback.onFail(msg);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(UiUtils.getContext(),VolleyErrorHelper.getMessage(volleyError, UiUtils.getContext()),Toast.LENGTH_SHORT).show();
                    callback.onFail(volleyError.getMessage());
                }
            });

            HttpClientRequest.getInstance().addRequest(request);
        }else{
            callback.onFail("无网络连接~！");
        }
    }



    /**
     * 上传单个文件 带params
     * @param url   上传地址
     * @param params   参数
     * @param filePartName 服务端定义上传文件的参数名字
     * @param file 要上传的文件
     * @param callback
     */
    public void uploadFile(String url, Map<String, String> params, String filePartName, File file, final RequestCallback callback){
        if(UiUtils.hasNetwork()){
            Request request = new MultipartRequest(url, params, filePartName, file, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject json = new JSONObject(s);
                        String msg = getMsg(json);
                        if(validationStatusCode(json)){
                            callback.onSuccess(json);
                        }else{
                            callback.onFail(msg);
                        }

                    } catch (JSONException e) {
                        callback.onFail("服务器异常");
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if(!TextUtils.isEmpty(volleyError.getMessage())){
                        callback.onFail(volleyError.getMessage());
                    }else{
                        callback.onFail("服务器异常");
                    }
                }
            });

            HttpClientRequest.getInstance().addRequest(request);
        }else{
            callback.onFail("无网络连接~！");
        }
    }


    /**
     * 上传多个文件 不带params
     * @param url   上传地址
     * @param filePartName 服务端定义上传文件的参数名字
     * @param files 要上传的文件列表
     * @param callback
     */
    public void uploadFile(String url, String filePartName, List<File> files, final RequestCallback callback){
        if(UiUtils.hasNetwork()){

            Request request = new MultipartRequest(url, filePartName, files, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject json = new JSONObject(s);
                        String msg = getMsg(json);
                        if(validationStatusCode(json)){
                            callback.onSuccess(json);
                        }else{
                            callback.onFail(msg);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(UiUtils.getContext(),VolleyErrorHelper.getMessage(volleyError, UiUtils.getContext()),Toast.LENGTH_SHORT).show();
                    callback.onFail(volleyError.getMessage());
                }
            });

            HttpClientRequest.getInstance().addRequest(request);
        }else{
            callback.onFail("无网络连接~！");
        }
    }

    /**
     * 上传多个文件 带params
     * @param url
     * @param params
     * @param filePartName 服务端定义上传文件的参数名字
     * @param files 要上传的文件列表
     * @param callback
     */
    public void uploadFile(String url, Map<String, String> params, String filePartName, List<File> files, final RequestCallback callback){
        if(UiUtils.hasNetwork()){

            Request request = new MultipartRequest(url, params, filePartName, files, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject json = new JSONObject(s);
                        String msg = getMsg(json);
                        if(validationStatusCode(json)){
                            callback.onSuccess(json);
                        }else{
                            callback.onFail(msg);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(UiUtils.getContext(),VolleyErrorHelper.getMessage(volleyError, UiUtils.getContext()),Toast.LENGTH_SHORT).show();
                    callback.onFail(volleyError.getMessage());
                }
            });

            HttpClientRequest.getInstance().addRequest(request);
        }else{
            callback.onFail("无网络连接~！");
        }
    }



    /**
     * 表单提交
     * @param formTextList
     * @param callback
     */
    public void postFormTextApi(String url, List<FormText> formTextList, final RequestCallback callback){
        if(UiUtils.hasNetwork()){

            Request request = new PostFormRequest(url, formTextList, new Response.Listener() {
                @Override
                public void onResponse(Object o) {
                    try {
                        JSONObject json = new JSONObject((String) o);
                        String msg = getMsg(json);
                        if(validationStatusCode(json)){
                            callback.onSuccess(o);
                        }else{
                            callback.onFail(msg);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(UiUtils.getContext(),VolleyErrorHelper.getMessage(volleyError, UiUtils.getContext()),Toast.LENGTH_SHORT).show();
                    callback.onFail(volleyError.getMessage());
                }
            });

            HttpClientRequest.getInstance().addRequest(request);
        }else{
            callback.onFail("无网络连接~！");
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
