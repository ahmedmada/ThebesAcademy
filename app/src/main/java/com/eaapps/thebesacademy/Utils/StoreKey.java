package com.eaapps.thebesacademy.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class StoreKey {
    private static final String PREF_NAME = "ThebesAcademy";
    private static final String NAME = "name";
    private static final String PHONE = "phone";
    private static final String ADDRESS = "address";
    private static final String USER = "user";
    private static final String TOKEN = "tokenId";
    private static final String LAT = "lat";
    private static final String LNG = "lng";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;
    private int PRIVATE_MODE = 0;

    public StoreKey(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }

    public String getToken() {
        return pref.getString(TOKEN, null);
    }

    public void setToken(String id) {
        editor.putString(TOKEN, id);
        editor.commit();
    }

    public String getUser() {
        return pref.getString(USER, null);
    }

    public void setUser(String user) {
        editor.putString(USER, user);
        editor.commit();
    }

    public float getLng() {
        return pref.getFloat(LNG, 0.0f);
    }

    public void setLng(float lng) {
        editor.putFloat(LNG, lng);
        editor.commit();
    }

    public float getLat() {
        return pref.getFloat(LAT, 0.0f);
    }

    public void setLat(float lat) {
        editor.putFloat(LAT, lat);
        editor.commit();
    }


}
