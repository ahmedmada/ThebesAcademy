package com.eaapps.thebesacademy.Chat;

/**
 * Created by AkshayeJH on 19/06/17.
 */

public class Users {

    public String name;
    public String master;
    public String imageUrl;




    public Users(){

    }

    public Users(String name, String master) {
        this.name = name;
        this.master = master;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
