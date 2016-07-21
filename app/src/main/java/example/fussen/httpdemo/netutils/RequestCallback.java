package example.fussen.httpdemo.netutils;

/**
 * Created by Fussen on 16/7/19.
 *
 * 访问网络回调
 */
public interface RequestCallback {
    void onSuccess(Object object);

    void onFail(String string);
}
