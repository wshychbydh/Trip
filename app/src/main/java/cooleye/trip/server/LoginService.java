package cooleye.trip.server;

import java.util.Map;

import cooleye.service.response.GenericResponse;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by cool on 16-8-8.
 */
public interface LoginService {
    @GET("weather")
    Call<Weather> loadeather(@Query("cityname") String cityname, @Query("key") String apiKey);

    /**
     * retrofit 支持 rxjava 整合
     * 这种方法适用于新接口
     */
    @GET("weather")
    Observable<Weather> getWeatherData(@Query("cityname") String cityname, @Query("key") String apiKey);

    @FormUrlEncoded
    //登录接口
    @POST("api/biu/app/v1/0/nothing/0/users/login")
    Observable<GenericResponse<UserEntity>> login(@FieldMap Map<String, String> params);
}
