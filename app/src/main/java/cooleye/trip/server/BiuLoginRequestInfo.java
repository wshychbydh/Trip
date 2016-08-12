package cooleye.trip.server;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class BiuLoginRequestInfo implements Serializable {

    @JSONField(name = "password")
    private String mPassword = "111111";

    @JSONField(name = "biu_channel")
    private String mBiuChannel = "bai_du";
    @JSONField(name = "mobile")
    private String mMobile = "15002850035";

    public String getBiuChannel() {
        return mBiuChannel;
    }

    public void setBiuChannel(String biuChannel) {
        mBiuChannel = biuChannel;
    }

    public String getMobile() {
        return mMobile;
    }

    public void setMobile(String mobile) {
        mMobile = mobile;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    @Override
    public String toString() {
        return "BiuLoginRequestInfo{" +
                "mBiuChannel='" + mBiuChannel + '\'' +
                ", mPassword='" + mPassword + '\'' +
                ", mMobile='" + mMobile + '\'' +
                '}';
    }
}
