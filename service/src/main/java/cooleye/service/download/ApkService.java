package cooleye.service.download;


import retrofit2.Call;
import retrofit2.http.GET;

public interface ApkService  {
    //http://139.129.60.39/
    @GET("/app/version/android")
    Call<DownloadBean> checkUpdate();
}

