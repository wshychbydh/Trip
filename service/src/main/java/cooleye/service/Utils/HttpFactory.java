package cooleye.service.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cooleye.service.converter.FastJsonConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class HttpFactory {

    private static int HTTP_CONNECT_TIMEOUT = 5000;
    private static int HTTP_READ_TIMEOUT = 5000;

    private HttpFactory() {
    }

    public static Retrofit create(String host) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(host);//设置远程地址
        builder.addConverterFactory(FastJsonConverterFactory.create());
        builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        builder.client(create());
        return builder.build();
    }

    private static OkHttpClient create() {
        return Factory.sClient;
    }

    public static <T> void subscribe(Object tag, Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
        SubscriberMapper.getInstance().add(tag, s);
    }

    public static <T> Map<String, String> formatRequest(T request) {
        Map<String, String> mapValue = new HashMap<>();
        JSONObject requestJson = (JSONObject) JSON.toJSON(request);
        for (Object o : requestJson.keySet()) {
            String key = o.toString();
            String value = requestJson.getString(key);
            if (value != null) {
                mapValue.put(key, value);
            }
        }
        return mapValue;
    }

    private static class Factory {

        private static OkHttpClient sClient;

        static {
            OkHttpClient.Builder singleton = new OkHttpClient.Builder();
            singleton.connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
            singleton.readTimeout(HTTP_READ_TIMEOUT, TimeUnit.MILLISECONDS);
            singleton.interceptors().add(new HttpLoggingInterceptor().setLevel
                    (HttpLoggingInterceptor.Level.BODY));
            sClient = singleton.build();
        }
    }
}