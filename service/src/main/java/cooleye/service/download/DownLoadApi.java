package cooleye.service.download;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by cool on 16-6-3.
 */
public interface DownLoadApi {
//    @GET("android.apk")
//    @Headers({"Content-Type: */*"})
//    Call<ResponseBody> getFile();
    @GET("{url}")
    @Headers({"Content-Type: */*"})
    Call<ResponseBody> getFile(@Path("url") String url);
}
