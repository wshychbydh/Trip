package cooleye.trip.app.loading;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.baidu.mapapi.search.route.DrivingRouteLine;

import cooleye.permission.PermissionActivity;
import cooleye.service.Utils.HttpFactory;
import cooleye.service.download.AppUpdate;
import cooleye.service.response.DataByAuthExtractor;
import cooleye.service.subscribers.ResponseListener;
import cooleye.service.subscribers.ResponseSubscriber;
import cooleye.trip.R;
import cooleye.trip.app.home.HomeActivity;
import cooleye.trip.map.LocationHelper;
import cooleye.trip.map.RoutePlanHelper;
import cooleye.trip.server.BiuLoginRequestInfo;
import cooleye.trip.server.Host;
import cooleye.trip.server.LoginService;
import cooleye.trip.server.UserEntity;
import cooleye.utils.utils.ToastUtil;
import rx.Observable;

public class LoadingActivity extends PermissionActivity {

    private static int times;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        //  getPersimmions();
    }

    public void onSearch(View v) {
        routePlan();
    }

    public void onHome(View v) {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private void update() {
        new AppUpdate(this, null).checkUpdate();
    }

    private void location() {
        LocationHelper.get(this).onStart();
    }

    private void routePlan() {
        RoutePlanHelper.get(this).setOnRoutePlanListener(new RoutePlanHelper.OnRoutePlanListener() {
            @Override
            public void onRoutePlaned(DrivingRouteLine routeLine) {
                ToastUtil.show(LoadingActivity.this, RoutePlanHelper.get(LoadingActivity.this)
                        .composePrint(routeLine));
            }
        }).drivePlan("成都", "火车南", "天府新");
    }

    public void login(Object tag,
                      ResponseListener<UserEntity> listener) {
        Observable<UserEntity> info = HttpFactory.create(Host.BASE_URL)
                .create(LoginService.class)
                .login(HttpFactory.formatRequest(new BiuLoginRequestInfo()))
                .map(new DataByAuthExtractor<UserEntity>());
        HttpFactory.subscribe(tag, info, new ResponseSubscriber<>(listener));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RoutePlanHelper.get(this).onDestroy();
    }

    public void onSnackbarClick(View v) {
        Snackbar.make(v, "Replace with your own action -- >" + (++times), Snackbar.LENGTH_LONG)
                .setAction("Action", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.show(LoadingActivity.this, "action clicked");
                    }
                }).setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);
            }
        }).setDuration(50000000).show();
    }
}
