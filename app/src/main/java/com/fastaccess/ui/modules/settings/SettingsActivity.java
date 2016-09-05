package com.fastaccess.ui.modules.settings;

import com.fastaccess.R;
import com.fastaccess.ui.base.BaseActivity;

/**
 * Created by Kosh on 31 Aug 2016, 11:45 AM
 */

public class SettingsActivity extends BaseActivity {
    @Override protected int layout() {
        return R.layout.settings_layout;
    }

    @Override protected boolean isTransparent() {
        return false;
    }

    @Override protected boolean canBack() {
        return true;
    }
}
