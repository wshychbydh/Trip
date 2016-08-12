package cooleye.service.subscribers;


import cooleye.service.Utils.ServerException;

public class ResponseListener<T> {

    public void onStart() {
    }

    public void onSuccess(T t) {
    }

    public void onCancel() {
    }

    public void onFailure(ServerException exception) {
    }

    public void onComplete() {
    }
}
