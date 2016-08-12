package cooleye.service.download;

import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by cool on 16-6-3.
 */
public interface FileDownloadService {
    @GET("android.apk")
    @Headers({"Content-Type: */*"})
    CallAdapter<Response> getFile();
}
