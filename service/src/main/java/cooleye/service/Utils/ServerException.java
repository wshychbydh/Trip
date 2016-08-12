package cooleye.service.Utils;

/**
 * Created by jninber on 16/6/24.
 */

public class ServerException extends Exception {
    private int mCode;

    public ServerException(int code, String message) {
        super(message);
        mCode = code;
    }

    public int getCode() {
        return mCode;
    }

    public void setCode(int code) {
        mCode = code;
    }


}
