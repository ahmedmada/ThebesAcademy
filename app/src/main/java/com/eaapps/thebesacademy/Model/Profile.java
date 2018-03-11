package com.eaapps.thebesacademy.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by eslamandroid on 2/21/18.
 */

public class Profile {
    private static String ID = "id";
    private static String NAME = "name";
    private static String EMAIL = "email";
    private static String USERID = "userId";
    private static String PHONE = "phone";
    private static String MASTER = "master";
    private static String SECTION = "section";
    private static String URL = "imageUrl";

    private String id;
    private String name;
    private String email;
    private String userId;
    private String phone;
    private String master;
    private String section;
    private String url;

    public Profile() {
    }

    public Profile(String id, String name, String email, String userId, String phone, String master, String section, String url) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.userId = userId;
        this.phone = phone;
        this.master = master;
        this.section = section;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> addProfile() {
        Map<String, Object> profile = new HashMap<>();
        if (url != null && id != null && name != null && email != null && userId != null && phone != null && master != null && section != null) {
            profile.put(URL, this.url);
            profile.put(ID, this.id);
            profile.put(NAME, this.name);
            profile.put(EMAIL, this.email);
            profile.put(USERID, this.userId);
            profile.put(PHONE, this.phone);
            profile.put(MASTER, this.master);
            profile.put(SECTION, this.section);
        }


        return profile;
    }
}
