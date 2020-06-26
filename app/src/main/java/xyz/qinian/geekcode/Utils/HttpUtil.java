package xyz.qinian.geekcode.Utils;

import java.io.File;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void sendOkHttpRequest(String address, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendPostRequest(String url, String data, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = FormBody.create(JSON, data);
        Request request = new Request.Builder()//创建Request 对象
                .url(url)
                .post(formBody)//传递请求体   //与get的区别在这里
                .build();
        client.newCall(request).enqueue(callback);
    }

}
