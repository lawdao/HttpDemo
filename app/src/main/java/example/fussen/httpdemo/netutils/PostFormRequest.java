package example.fussen.httpdemo.netutils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import example.fussen.httpdemo.global.MyApplication;


public class PostFormRequest<T> extends Request<T> {

    private Response.Listener mListener;

    /*请求 数据通过参数的形式传入*/
    private List<FormText> mListItem ;

    private String BOUNDARY = "---------8888888888888"; //数据分隔线
    private String MULTIPART_FORM_DATA = "multipart/form-data";

    public PostFormRequest(String url, List<FormText> listItem, Response.Listener listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        mListener = listener;
        mListItem = listItem;
        setShouldCache(false);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        String parsed;
        try {
            parsed = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            return (Response<T>) Response.success(parsed, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T t) {
        mListener.onResponse(t);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        //加入固定的头
        Map<String, String> headers = new HashMap<>();
       // headers.put("BODY-X-TYPE", "2");
        //加入cookie
        MyApplication.addSessionCookie(headers);
        return headers;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if(mListItem == null || mListItem.size() == 0){
            return super.getBody();
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FormText formText;
        for(int i = 0; i < mListItem.size(); i++){
            formText = mListItem.get(i);
            StringBuffer sb = new StringBuffer();
            /*第一行:"--" + boundary + "\r\n" ;*/
            sb.append("--" + BOUNDARY);
            sb.append("\r\n");
            /*第二行:"Content-Disposition: form-data; name="参数的名称"" + "\r\n" ;*/
            sb.append("Content-Disposition: form-data;");
            sb.append("name=\"");
            sb.append(formText.getName());
            sb.append("\"");
            sb.append("\r\n");
            /*第三行:"\r\n" ;*/
            sb.append("\r\n");
            /*第四行:"参数的值" + "\r\n" ;*/
            sb.append(formText.getValue());
            sb.append("\r\n");
            try {
                bos.write(sb.toString().getBytes("utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /*结尾行:"--" + boundary + "--" + "\r\n" ;*/
        String endLine = "--" + BOUNDARY + "--" + "\r\n";
        try {
            bos.write(endLine.toString().getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    @Override
    public String getBodyContentType() {
        return MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY;
    }
}
