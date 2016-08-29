package cooleye.trip.app.base;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

import cooleye.trip.map.LocationService;


/**
 * Created by cool on 16-8-11.
 */
public class TripApplication extends Application {

    public LocationService mLocationService;

    @Override
    public void onCreate() {
        super.onCreate();

        SDKInitializer.initialize(getApplicationContext());
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        mLocationService = new LocationService(getApplicationContext());
    }
}
