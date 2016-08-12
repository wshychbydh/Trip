package cooleye.service.Utils;

/**
 * Created by jninber on 16/7/26.
 */

public class SudiyiDataError extends Error {
    public static final int RESPONSE_LOGIN_STATE_ERROR = 10001;
    public static final int RESPONSE_USER_FORBIDDEN = 10013;
    public static final int RESPONSE_AUTH_ERROR = 13000;
    private int mCode;

    public SudiyiDataError(int code, String message) {
        super(message);
        mCode = code;
    }

    public int getCode() {
        return mCode;
    }

    public void setCode(int code) {
        mCode = code;
    }

    public boolean isAuthError(int code) {
        return code == RESPONSE_LOGIN_STATE_ERROR
                || code == RESPONSE_USER_FORBIDDEN
                || code == RESPONSE_AUTH_ERROR;
    }
}
