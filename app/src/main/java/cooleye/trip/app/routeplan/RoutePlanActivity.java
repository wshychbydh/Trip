package cooleye.trip.app.routeplan;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.search.route.DrivingRouteLine;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import cooleye.trip.R;
import cooleye.trip.app.base.BaseActivity;
import cooleye.trip.map.LocationHelper;
import cooleye.trip.map.RoutePlanHelper;

public class RoutePlanActivity extends BaseActivity {

    @InjectView(R.id.route_plan)
    TextView mRoutePlanTv;
    private boolean mIsOnCar;
    private boolean mIsRouting;
    private List<RoutePlan> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_plan);
        mList = new ArrayList<>();
    }

    @OnClick(R.id.btn_location)
    void onLocation(View view) {
        mIsOnCar = !mIsOnCar;
        ((TextView) view).setText(mIsOnCar ? "下车" : "上车");
        LocationClientOption option = mIsOnCar ? getLocationOption() : null;
        if (mIsOnCar) {
            mList.clear();
            mRoutePlanTv.setText("正在定位中...");
            mIsRouting = false;
        }
        LocationHelper.get(this).onStart(option, new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (!mIsRouting) {
                    mRoutePlanTv.setText("当前所在位置：" + bdLocation.getAddrStr());
                    mIsRouting = true;
                }
                if (insertLocation(bdLocation)) {
                    if (mList.size() >= 2) {
                        calculateDi(mList.get(0), mList.get(mList.size() - 1));
                    }
                }
            }
        });
    }

    private boolean insertLocation(BDLocation bdLocation) {
        if (bdLocation == null || TextUtils.isEmpty(bdLocation.getCity())
                || TextUtils.isEmpty(bdLocation.getStreet())) {
            return false;
        }
        return mList.add(new RoutePlan(bdLocation));
    }

    private void calculateDi(final RoutePlan start, final RoutePlan end) {
        RoutePlanHelper.get(this).setOnRoutePlanListener(new RoutePlanHelper.OnRoutePlanListener() {
            @Override
            public void onRoutePlaned(DrivingRouteLine routeLine) {
                mRoutePlanTv.setText(composePrint(routeLine, end.time - start.time));
            }
        }).drivePlan(start.city, start.address, end.city, end.address);
    }

    public String composePrint(DrivingRouteLine routeLine, long duration) {
        StringBuilder sb = new StringBuilder();
        sb.append("标题 ： 距离上一次位置").append("\n");
        sb.append("拥堵距离 ： " + routeLine.getCongestionDistance() + "米").append("\n");
        sb.append("红绿灯 ： " + routeLine.getLightNum() + "个").append("\n");
        sb.append("距离 ： " + routeLine.getDistance() / 1000d + "km").append("\n");
        sb.append("预计用时 ： " + getMinute(routeLine.getDuration() / 60d)).append("\n");
        sb.append("已用时 ： " + getMinute(duration / 1000d / 60d)).append("\n");
        return sb.toString();
    }

    private String getMinute(double time) {
        String format = "%.2f分钟";
        return String.format(format, time);
       // DecimalFormat df=new DecimalFormat("#.00");
        // return df.format(time)+"分钟";
    }

    private LocationClientOption getLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("gcj02");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        option.setScanSpan(5000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        return option;
    }

    private class RoutePlan {
        long time;
        String city;
        String address;

        public RoutePlan(BDLocation location) {
            time = System.currentTimeMillis();
            city = location.getCity();
            address = location.getStreet();
        }
    }
}
