package cooleye.service.response;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by liukun on 16/3/5.
 */
public class GenericResponse<T> implements Serializable {

    @JSONField(name = "code")
    private int mCode;
    @JSONField(name = "message")
    private String mMessage;
    @JSONField(name = "display")
    private String mDisplay;
    //用来模仿Data
    @JSONField(name = "data")
    private T mData;

    public int getCode() {
        return mCode;
    }

    public void setCode(int code) {
        this.mCode = code;
    }

    public T getData() {
        return mData;
    }

    public void setData(T data) {
        this.mData = data;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    public String getDisplay() {
        return mDisplay;
    }

    public void setDisplay(String display) {
        this.mDisplay = display;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("code=" + mCode + " codeMessage=" + mMessage + " display=" + mDisplay);
        if (null != mData) {
            sb.append(" data:" + mData.toString());
        }
        return sb.toString();
    }
}
