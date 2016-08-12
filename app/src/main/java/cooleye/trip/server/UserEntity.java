package cooleye.trip.server;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by summerslam on 16/6/29.
 */
public class UserEntity implements Serializable {

    /**
     * id : 51363
     * mobile : 13000000001
     * email :
     * nickname :
     * avatar :
     * user_type : 0
     * balance : 0
     * vip : 0
     * verify : 0
     * ticket : e99775bec2b3437e93bc30bd68061f2f
     * push_status : 1
     * push_over_day : 1
     * biu_userid : 16
     * status : 0
     * last_login_time : 1466846526
     * name :
     * idcard :
     * gender : -1
     * birthday : 0
     * biu_conf : null
     */

    @JSONField(name = "id")
    private int mId;
    @JSONField(name = "mobile")
    private String mMobile;
    @JSONField(name = "email")
    private String mEmail;
    @JSONField(name = "nickname")
    private String mNickname;
    @JSONField(name = "avatar")
    private String mAvatar;
    @JSONField(name = "user_type")
    private int mUserType;
    @JSONField(name = "balance")
    private int mBalance;
    @JSONField(name = "vip")
    private int mVip;
    @JSONField(name = "verify")
    private int mVerify;
    @JSONField(name = "ticket")
    private String mTicket;
    @JSONField(name = "push_status")
    private int mPushStatus;
    @JSONField(name = "push_over_day")
    private int mPushOverDay;
    @JSONField(name = "biu_userid")
    private String mBiuUserid;
    @JSONField(name = "status")
    private int mStatus;
    @JSONField(name = "last_login_time")
    private int mLastLoginTime;
    @JSONField(name = "name")
    private String mName;
    @JSONField(name = "idcard")
    private String mIdcard;
    @JSONField(name = "gender")
    private int mGender;
    @JSONField(name = "birthday")
    private int mBirthday;

    private String mChannel = "UNKNOWN";

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getMobile() {
        return mMobile;
    }

    public void setMobile(String mobile) {
        mMobile = mobile;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getNickname() {
        return mNickname;
    }

    public void setNickname(String nickname) {
        mNickname = nickname;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }

    public int getUserType() {
        return mUserType;
    }

    public void setUserType(int userType) {
        mUserType = userType;
    }

    public int getBalance() {
        return mBalance;
    }

    public void setBalance(int balance) {
        mBalance = balance;
    }

    public int getVip() {
        return mVip;
    }

    public void setVip(int vip) {
        mVip = vip;
    }

    public int getVerify() {
        return mVerify;
    }

    public void setVerify(int verify) {
        mVerify = verify;
    }

    public String getTicket() {
        return mTicket;
    }

    public void setTicket(String ticket) {
        mTicket = ticket;
    }

    public int getPushStatus() {
        return mPushStatus;
    }

    public void setPushStatus(int pushStatus) {
        mPushStatus = pushStatus;
    }

    public int getPushOverDay() {
        return mPushOverDay;
    }

    public void setPushOverDay(int pushOverDay) {
        mPushOverDay = pushOverDay;
    }

    public String getBiuUserid() {
        return mBiuUserid;
    }

    public void setBiuUserid(String biuUserid) {
        mBiuUserid = biuUserid;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public int getLastLoginTime() {
        return mLastLoginTime;
    }

    public void setLastLoginTime(int lastLoginTime) {
        mLastLoginTime = lastLoginTime;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getIdcard() {
        return mIdcard;
    }

    public void setIdcard(String idcard) {
        mIdcard = idcard;
    }

    public int getGender() {
        return mGender;
    }

    public void setGender(int gender) {
        mGender = gender;
    }

    public int getBirthday() {
        return mBirthday;
    }

    public void setBirthday(int birthday) {
        mBirthday = birthday;
    }

    public String getChannel() {
        return mChannel;
    }

    public void setChannel(String channel) {
        mChannel = channel;
    }

    // Create Anonymous user

    private static final int ANONYMOUS_ID = 0; // 在用户未登陆的状态下 id 为 0 用于服务器占位
    private static final String ANONYMOUS_TICKET = "nothing"; // 在用户未登陆的状态下 ticket 为 nothing 用于服务器占位
    private static final String ANONYMOUS_BIU_USER_ID = "0"; // 在用户未登陆的状态下 biu_id 为 "0" 用于服务器占位
    private static final String ANONYMOUS_MOBILE = "";

    public static UserEntity createAnonymousUser() {
        UserEntity userInfo = new UserEntity();
        userInfo.setId(ANONYMOUS_ID);
        userInfo.setTicket(ANONYMOUS_TICKET);
        userInfo.setBiuUserid(ANONYMOUS_BIU_USER_ID);
        userInfo.setMobile(ANONYMOUS_MOBILE);
        return userInfo;
    }

    public boolean isAnonymous() {
        return mId == ANONYMOUS_ID;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "mAvatar='" + mAvatar + '\'' +
                ", mId=" + mId +
                ", mMobile='" + mMobile + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mNickname='" + mNickname + '\'' +
                ", mUserType=" + mUserType +
                ", mBalance=" + mBalance +
                ", mVip=" + mVip +
                ", mVerify=" + mVerify +
                ", mTicket='" + mTicket + '\'' +
                ", mPushStatus=" + mPushStatus +
                ", mPushOverDay=" + mPushOverDay +
                ", mBiuUserid='" + mBiuUserid + '\'' +
                ", mStatus=" + mStatus +
                ", mLastLoginTime=" + mLastLoginTime +
                ", mName='" + mName + '\'' +
                ", mIdcard='" + mIdcard + '\'' +
                ", mGender=" + mGender +
                ", mBirthday=" + mBirthday +
                ", mChannel='" + mChannel + '\'' +
                '}';
    }
}
