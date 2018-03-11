package com.eaapps.thebesacademy.Utils;

import com.eaapps.thebesacademy.Model.Profile;

import java.util.Map;

/**
 * Created by eslamandroid on 2/22/18.
 */

public interface ThebesInterface {
    public interface CallBackFindUser<T> {
        void hasUser(T object);

        void hasUser(boolean f);
    }
}
