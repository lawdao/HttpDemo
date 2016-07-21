package example.fussen.httpdemo.netutils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import example.fussen.httpdemo.global.MyApplication;

/**
 * Created by Fussen on 16/7/21.
 */
public class MultipartRequest extends Request<String> {

    private MultipartEntity mMultiPartEntity = new MultipartEntity();

    private Response.Listener<String> mListener;

    private List<File> mFileParts;

    private String mFilePartName;

    private Map<String, String> mParams;

    public MultipartRequest(String url, String filePartName, File file, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        this(url, null, filePartName, file, listener, errorListener);
    }

    public MultipartRequest(String url, Map<String, String> params, String filePartName, File file, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        mListener = listener;
        mFileParts = new ArrayList<File>();
        if (file != null) {
            mFileParts.add(file);
        }
        mFilePartName = filePartName;
        mParams = params;

        setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        buildMultipartEntity();
    }

    public MultipartRequest(String url, String filePartName, List<File> files, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        this(url, null, filePartName, files, listener, errorListener);
    }


    public MultipartRequest(String url, Map<String, String> params, String filePartName, List<File> files, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        mListener = listener;
        mFilePartName = filePartName;
        mFileParts = files;
        mParams = params;

        setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        buildMultipartEntity();
    }

    private void buildMultipartEntity() {
        if (mFileParts != null && mFileParts.size() > 0) {
            for (File file : mFileParts) {
                mMultiPartEntity.addPart(mFilePartName, new FileBody(file));
            }
            long length = mMultiPartEntity.getContentLength();

        }


        try {
            if (mParams != null && mParams.size() > 0) {
                for (Map.Entry<String, String> entry : mParams.entrySet()) {
                    mMultiPartEntity.addPart(entry.getKey(), new StringBody(entry.getValue(), Charset.forName("UTF-8")));
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getBodyContentType() {
        return mMultiPartEntity.getContentType().getValue();
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        //在这里添加固定的头
        Map<String, String> headers = new HashMap<>();
        headers.put("BODY-X-TYPE", "2");
        headers.put("BODY-X-VERSION", "1.0");
        //添加cookie
        MyApplication.addSessionCookie(headers);
        return headers;
    }

    @Override
    public byte[] getBody() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mMultiPartEntity.writeTo(bos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        if (response.headers != null) {
            for (Map.Entry<String, String> entry : response.headers
                    .entrySet()) {
            }
        }

        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(String s) {
        mListener.onResponse(s);
    }
}
