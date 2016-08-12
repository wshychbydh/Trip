package cooleye.service.response;

import android.text.TextUtils;

import cooleye.service.Utils.SudiyiDataError;
import rx.functions.Func1;

/**
 * Created by jninber on 16/6/20.
 */

public class DataByAuthExtractor<T> implements Func1<GenericResponse<T>, T> {
    public static final int RESPONSE_LOGIN_STATE_ERROR = 10001;
    public static final int RESPONSE_USER_FORBIDDEN = 10013;
    public static final int RESPONSE_AUTH_ERROR = 13000;
    public static final int RESPONSE_AUTH_RESET_PASSWORD = 13001;

    @Override
    public T call(GenericResponse<T> tGenericResponse) {
        int code = tGenericResponse.getCode();
        if (code != 0) {
            if (isAuthError(code)) {
              //  EventBus.getDefault().post(new AuthErrorEvent(code));
            }
            throw new SudiyiDataError(code, TextUtils.isEmpty(tGenericResponse.getDisplay()) ?
                    tGenericResponse.getMessage() :
                    tGenericResponse.getDisplay());

        }
        return tGenericResponse.getData();
    }

    public boolean isAuthError(int code) {
        return code == RESPONSE_LOGIN_STATE_ERROR
                || code == RESPONSE_USER_FORBIDDEN
                || code == RESPONSE_AUTH_ERROR
                || code == RESPONSE_AUTH_RESET_PASSWORD;
    }
}
