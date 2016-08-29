package cooleye.trip.app.routeplan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.search.route.DrivingRouteLine;

import butterknife.InjectView;
import butterknife.OnClick;
import cooleye.trip.R;
import cooleye.trip.app.base.BaseActivity;
import cooleye.trip.map.LocationHelper;
import cooleye.trip.map.RoutePlanHelper;
import cooleye.utils.utils.ToastUtil;

/**
 * Created by cool on 16-8-29.
 */
public class SearchActivity extends BaseActivity {

    @InjectView(R.id.search_route_plan)
    TextView mRoutePlanTv;

    @InjectView(R.id.start_city)
    EditText mStartCityEt;

    @InjectView(R.id.start_address)
    EditText mStartAddressEt;

    @InjectView(R.id.end_city)
    EditText mEndCityEt;

    @InjectView(R.id.end_address)
    EditText mEndAddressEt;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @OnClick(R.id.btn_search_location)
    public void onLocation(View view) {
        ToastUtil.show(this,"定位中...");
        LocationHelper.get(this).onStart(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                if (location != null) {
                    mStartCityEt.setText(location.getCity());
                    mStartAddressEt.setText(location.getStreet());
                    mEndCityEt.setText(location.getCity());
                }
            }
        });
    }

    @OnClick(R.id.btn_route_plan)
    public void onRoutePlan(View view) {

        if (TextUtils.isEmpty(mStartCityEt.getText().toString())) {
            ToastUtil.show(this, "请输入出发城市");
            return;
        }
        if (TextUtils.isEmpty(mStartAddressEt.getText().toString())) {
            ToastUtil.show(this, "请输入出发地址");
            return;
        }
        if (TextUtils.isEmpty(mEndCityEt.getText().toString())) {
            ToastUtil.show(this, "请输入目的城市");
            return;
        }
        if (TextUtils.isEmpty(mEndAddressEt.getText().toString())) {
            ToastUtil.show(this, "请输入目的地址");
            return;
        }
        RoutePlanHelper.get(this).setOnRoutePlanListener(new RoutePlanHelper.OnRoutePlanListener() {
            @Override
            public void onRoutePlaned(DrivingRouteLine routeLine) {
                mRoutePlanTv.setText(RoutePlanHelper.get(SearchActivity.this)
                        .composePrint(routeLine));
            }
        }).drivePlan(mStartCityEt.getText().toString(), mStartAddressEt.getText().toString(),
                mEndCityEt.getText().toString(), mEndAddressEt.getText().toString());
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocationHelper.get(this).onStop();
    }
}
