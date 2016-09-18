package com.gentle.greendaolearning.model;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by tao.j on 2016/9/15.
 */
@Entity
public class User {
    @NotNull
    @Id
    long uid;
    @NotNull
    String name;
    String email;
    boolean isOnline;

    long homeId;

    public User(long uid, String name, String email, boolean isOnline) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.isOnline = isOnline;
    }

    @Generated(hash = 2051188818)
    public User(long uid, @NotNull String name, String email, boolean isOnline,
            long homeId) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.isOnline = isOnline;
        this.homeId = homeId;
    }

    @Generated(hash = 586692638)
    public User() {
    }


    public long getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getIsOnline() {
        return this.isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public long getHomeId() {
        return this.homeId;
    }

    public void setHomeId(long homeId) {
        this.homeId = homeId;
    }

}
