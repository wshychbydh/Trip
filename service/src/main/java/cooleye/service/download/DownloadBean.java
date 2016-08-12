package cooleye.service.download;

import java.io.Serializable;

/**
 * Created by cool on 16-5-3.
 */
public class DownloadBean implements Serializable{
    int code;
    String msg;
    String version;
    String softwareUrl;
    boolean isForce = true;

    public boolean isForce() {
        return isForce;
    }

    public void setForce(boolean forceDownload) {
        isForce = forceDownload;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSoftwareUrl() {
        return softwareUrl;
    }

    public void setSoftwareUrl(String softwareUrl) {
        this.softwareUrl = softwareUrl;
    }

    @Override
    public String toString() {
        return "DownloadBean{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", version='" + version + '\'' +
                ", softwareUrl='" + softwareUrl + '\'' +
                ", isForce=" + isForce +
                '}';
    }
}
