package com.web.consultpin.interfaces;
public interface RxAPICallback<P> {
    void onSuccess(P t);

    void onFailed(Throwable throwable);
}

