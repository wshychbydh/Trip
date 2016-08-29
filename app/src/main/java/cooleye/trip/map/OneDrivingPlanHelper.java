package cooleye.trip.map;


import android.app.Activity;
import android.widget.Toast;

import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import java.util.List;

import cooleye.logger.Printer;
import cooleye.utils.utils.ToastUtil;

/**
 * 此demo用来展示如何进行驾车、步行、公交路线搜索并在地图使用RouteOverlay、TransitOverlay绘制
 * 同时展示如何进行节点浏览并弹出泡泡
 */
public class OneDrivingPlanHelper implements OnGetRoutePlanResultListener {

    private static OneDrivingPlanHelper sInstance;
    private Activity mActivity;
    // 搜索相关
    private RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用

    private OnRoutePlanListener mOnRoutePlanListener;

    private OneDrivingPlanHelper(Activity activity) {
        this.mActivity = activity;
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
    }

    public static OneDrivingPlanHelper get(Activity activity) {
        if (sInstance == null) {
            sInstance = new OneDrivingPlanHelper(activity);
        }
        return sInstance;
    }

    public OneDrivingPlanHelper setOnRoutePlanListener(OnRoutePlanListener onRoutePlanListener) {
        mOnRoutePlanListener = onRoutePlanListener;
        return this;
    }

    /**
     * 发起路线规划搜索
     */
    public void drivePlan(String startCity, String startAddress, String endCity, String
            endAddress) {
        // 设置起终点信息，对于tranist search 来说，城市名无意义
        PlanNode stNode = PlanNode.withCityNameAndPlaceName(startCity, startAddress);
        PlanNode enNode = PlanNode.withCityNameAndPlaceName(endCity, endAddress);
        // 实际使用中请对起点终点城市进行正确的设定
        mSearch.drivingSearch((new DrivingRoutePlanOption())
                .from(stNode).to(enNode));
        ToastUtil.show(mActivity, "路线规划中...");
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult result) {
    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(mActivity, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            if (mOnRoutePlanListener != null) {
                mOnRoutePlanListener.onRoutePlaned(result.getRouteLines().get(0));
            }
            printAll(result);
        }
    }

    public void printAll(DrivingRouteResult result) {
        List<DrivingRouteLine> routeLines = result.getRouteLines();
        StringBuilder sb = new StringBuilder();
        for (DrivingRouteLine routeLine : routeLines) {
            sb.append(composePrint(routeLine));
        }
        Printer.printI(sb.toString());
    }

    public String composePrint(DrivingRouteLine routeLine) {
        StringBuilder sb = new StringBuilder();
        sb.append("标题 ： " + routeLine.getTitle()).append("\n");
        sb.append("拥堵距离 ： " + routeLine.getCongestionDistance() + "米").append("\n");
        sb.append("红绿灯 ： " + routeLine.getLightNum() + "个").append("\n");
        sb.append("距离 ： " + routeLine.getDistance() / 1000d + "km").append("\n");
        sb.append("用时 ： " + routeLine.getDuration() / 60d + "分").append("\n");
        sb.append("************************×××××××××××××××××××××××××××××××*");
        return sb.toString();
    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    public void onDestroy() {
        mSearch.destroy();
    }


    public interface OnRoutePlanListener {
        void onRoutePlaned(DrivingRouteLine routeLine);
    }
}
