package cooleye.service.subscribers;

import com.alibaba.fastjson.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import cooleye.service.Utils.ServerException;
import cooleye.service.Utils.SubscriberMapper;
import cooleye.service.Utils.SudiyiDataError;
import rx.Subscriber;

/**
 * 调用者自己对请求数据进行处理
 * Created by XiongxinJiang on 16/3/10.
 */
public class ResponseSubscriber<T> extends Subscriber<T> {

    private ResponseListener<T> mResponseListener;


    public ResponseSubscriber(ResponseListener<T> listener) {
        this.mResponseListener = listener;
    }


    /**
     * 订阅开始时调用
     * 用于加载一些初始化信息
     */
    @Override
    public void onStart() {
        if (mResponseListener != null) {
            mResponseListener.onStart();
        }
    }

    /**
     * 完成，
     */
    @Override
    public void onCompleted() {
        mResponseListener.onComplete();
        SubscriberMapper.getInstance().remove(this);
    }

    /**
     * 对错误进行统一处理
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        if (mResponseListener != null) {
            mResponseListener.onFailure(parseException(e));
            mResponseListener.onComplete();
        }

    }

    private ServerException parseException(Throwable e) {
        if (e instanceof UnknownHostException) {
            return new ServerException(-1, "网络异常,请检查网络连接状态");
        } else if (e instanceof SocketTimeoutException) {
            return new ServerException(-2, "网络连接超时,请检查网络连接状态后重试");
        } else if (e instanceof ConnectException) {
            return new ServerException(-3, "网络连接失败,请检查网络连接状态后重试");
        } else if (e instanceof JSONException) {
            return new ServerException(-4, "数据解析失败");
        } else if (e instanceof SudiyiDataError) {
            return new ServerException(((SudiyiDataError) e).getCode(), e.getMessage());
        }
        return new ServerException(-5, "连接失败");
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        if (mResponseListener != null) {
            mResponseListener.onSuccess(t);
        }
    }

    /**
     * 取消对observable的订阅，同时也取消了http请求
     */
    public void onCancelSubscribe() {
        if (!this.isUnsubscribed()) {
            if (mResponseListener != null) {
                mResponseListener.onCancel();
            }
            this.unsubscribe();
        }
    }
}