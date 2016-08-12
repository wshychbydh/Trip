package cooleye.service.download;

import cooleye.service.converter.FastJsonConverterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by cool on 16-5-26.
 */
public class HttpUtil {
    public static Retrofit getRetrofit() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://139.129.60.39/")
                //retrofit已经把Json解析封装在内部了 你需要传入你想要的解析工具就行了 默认支持Gson解析
                .addConverterFactory(FastJsonConverterFactory.create())
                .client(new OkHttpClient()).build();
        return retrofit;
    }
}
