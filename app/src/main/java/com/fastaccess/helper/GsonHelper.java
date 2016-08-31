package com.fastaccess.helper;

import com.fastaccess.App;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Kosh on 29 May 2016, 5:09 AM
 */

public class GsonHelper {

    public static <T> T getObject(String json, Class<T> clazz) {
        return App.gson().fromJson(json, clazz);
    }

    public static <T> List<T> getList(String json, Class<T[]> clazz) {
        return Arrays.asList(App.gson().fromJson(json, clazz));
    }

}
