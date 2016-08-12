package cooleye.service.subscribers;

import android.content.Context;
import android.widget.Toast;

import cooleye.service.Utils.ServerException;


/**
 * Created by jninber on 16/6/14.
 */

public class ToastResponseListener<T> extends ResponseListener<T> {
    private Context mContext;

    public ToastResponseListener(Context context) {
        this.mContext = context;
    }


    @Override
    public void onFailure(ServerException exception) {
        super.onFailure(exception);
        if (exception.getCode() == -1 || exception.getCode() == -2 || exception.getCode() == -3) {
            Toast.makeText(mContext, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
