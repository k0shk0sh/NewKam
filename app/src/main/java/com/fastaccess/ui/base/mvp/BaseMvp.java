package com.fastaccess.ui.base.mvp;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

/**
 * Created by Kosh on 25 May 2016, 9:09 PM
 */

public interface BaseMvp {

    interface View {
        void onShowMessage(@StringRes int resId);

        void onShowMessage(@NonNull String msg);

        void onShowHideProgress(boolean show);
    }

    interface Presenter<V> {

        void attachView(V view);

        void onDestroy();
    }
}
