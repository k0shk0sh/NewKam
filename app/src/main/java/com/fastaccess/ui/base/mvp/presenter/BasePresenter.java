package com.fastaccess.ui.base.mvp.presenter;

import com.fastaccess.App;
import com.fastaccess.helper.Logger;
import com.fastaccess.ui.base.mvp.BaseMvp;

/**
 * Created by Kosh on 25 May 2016, 9:12 PM
 */

public class BasePresenter<V> implements BaseMvp.Presenter<V> {

    private V view;

    private BasePresenter() {throw new IllegalStateException("Cant not be initialized");}

    protected BasePresenter(V view) {
        attachView(view);
        Logger.e(view.getClass());
    }

    @Override public void attachView(V view) {
        this.view = view;
    }

    @Override public void onDestroy() {}

    protected boolean isViewAttached() {
        return view != null;
    }

    protected V getView() {
        checkViewAttached();
        return view;
    }

    protected void checkViewAttached() {
        if (!isViewAttached()) throw new NullPointerException("View is not injected to presenter");
    }

    protected App getApp() {
        return App.getInstance();
    }
}
