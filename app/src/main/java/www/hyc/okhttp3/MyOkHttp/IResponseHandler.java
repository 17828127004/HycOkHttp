package www.hyc.okhttp3.MyOkHttp;

import okhttp3.Response;

/**
 * Created by Administrator on 2018/11/1.
 */

public interface IResponseHandler {
    void onSuccess(Response response);

    void onFailure(int statusCode, String error_msg);

    void onProgress(long currentBytes, long totalBytes);
}
