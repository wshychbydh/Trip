package cooleye.trip.map;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.search.route.DrivingRouteLine;

import java.util.List;

import cooleye.trip.R;

/**
 * Created by cool on 16-8-11.
 */
public class RouteDialog extends Dialog implements AdapterView.OnItemClickListener {

    private List<DrivingRouteLine> mRouteLines;
    private RoutePlanHelper.OnRoutePlanListener mOnRoutePlanListener;

    public RouteDialog(Context context) {
        super(context, R.style.dialog);
    }

    public RouteDialog setData(@NonNull List<DrivingRouteLine> routeLines) {
        this.mRouteLines = routeLines;
        initView();
        return this;
    }

    public RouteDialog setOnRoutePlanListener(RoutePlanHelper.OnRoutePlanListener
                                                      routePlanListener) {
        mOnRoutePlanListener = routePlanListener;
        return this;
    }

    private void initView() {
        ListView listView = new ListView(getContext());
        listView.setAdapter(new RouteAdapter());
        listView.setItemsCanFocus(false);
        listView.setOnItemClickListener(this);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
        setContentView(listView);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mOnRoutePlanListener != null) {
            mOnRoutePlanListener.onRoutePlaned(mRouteLines.get(position));
        }
        dismiss();
    }

    private class RouteAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private Resources mRes;

        public RouteAdapter() {
            mInflater = LayoutInflater.from(getContext());
            mRes = getContext().getResources();
        }

        @Override
        public int getCount() {
            return mRouteLines.size();
        }

        @Override
        public DrivingRouteLine getItem(int position) {
            return mRouteLines.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.dialog_route, null);
                holder.mNameTv = (TextView) convertView.findViewById(R.id.route_name);
                holder.mCongestionDistanceTv = (TextView) convertView.findViewById(R.id
                        .route_congestion_distance);
                holder.mLightNumTv = (TextView) convertView.findViewById(R.id.route_light_num);
                holder.mDistanceTv = (TextView) convertView.findViewById(R.id.route_distance);
                holder.mDurationTv = (TextView) convertView.findViewById(R.id.route_duration);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            DrivingRouteLine routeLine = mRouteLines.get(position);

            holder.mNameTv.setText(mRes.getString(R.string.route_name, position + 1));
            holder.mCongestionDistanceTv.setText(mRes.getString(R.string
                    .route_congestion_distance, getDistance(routeLine.getCongestionDistance())));
            holder.mLightNumTv.setText(mRes.getString(R.string.route_light_num, routeLine
                    .getLightNum()));
            holder.mDistanceTv.setText(mRes.getString(R.string.route_distance, getDistance
                    (routeLine.getDistance())));
            holder.mDurationTv.setText(mRes.getString(R.string.route_duration, formatTime
                    (routeLine.getDuration())));
            return convertView;
        }

        private String getDistance(int distance) {
            if (distance > 1000) {
                return String.format(mRes.getString(R.string.route_km), distance / 1000f);
            } else {
                return String.format(mRes.getString(R.string.route_m), distance);
            }
        }

        private String formatTime(int minute) {
            long hour = minute / 60;

            if (hour > 0)
                return hour + "小时" + minute % hour + "分钟";
            else
                return minute + "分钟";

        }

        private class ViewHolder {
            TextView mNameTv;
            TextView mCongestionDistanceTv;
            TextView mLightNumTv;
            TextView mDistanceTv;
            TextView mDurationTv;
        }
    }
}
