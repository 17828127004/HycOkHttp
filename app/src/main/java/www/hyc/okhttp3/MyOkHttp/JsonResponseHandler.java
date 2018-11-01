package www.hyc.okhttp3.MyOkHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * json类型的回调接口
 * Created by tsy on 16/8/15.
 */
public abstract class JsonResponseHandler implements IResponseHandler {

    @Override
    public final void onSuccess(final Response response) {
        ResponseBody responseBody = response.body();
        String responseBodyStr = "";

        try {
            responseBodyStr = responseBody.string();
        } catch (final IOException e) {
            e.printStackTrace();

            MyOkHttp.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onFailure(response.code(), e.getMessage());
                }
            });
            return;
        } finally {
            responseBody.close();
        }

        final String finalResponseBodyStr = responseBodyStr;

        try {
            final Object result = new JSONTokener(finalResponseBodyStr).nextValue();
            if (result instanceof JSONObject) {
                MyOkHttp.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onSuccess(response.code(), (JSONObject) result);
                    }
                });
            } else if (result instanceof JSONArray) {
                MyOkHttp.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onSuccess(response.code(), (JSONArray) result);
                    }
                });
            } else {
                MyOkHttp.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onFailure(response.code(), finalResponseBodyStr);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
            MyOkHttp.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onFailure(response.code(), finalResponseBodyStr);
                }
            });
        }
    }

    public void onSuccess(int statusCode, JSONObject response) {
    }

    public void onSuccess(int statusCode, JSONArray response) {
    }

    @Override
    public void onProgress(long currentBytes, long totalBytes) {

    }
}
