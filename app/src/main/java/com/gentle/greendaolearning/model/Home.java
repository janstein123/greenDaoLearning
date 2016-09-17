package com.gentle.greendaolearning.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Created by tao.j on 2016/9/17.
 */
@Entity
public class Home {
    @Id
    long hid;
    String homeName;

    @NotNull
    long ownerId;

    @ToOne(joinProperty = "ownerId")
    User owner;

    @ToMany(referencedJoinProperty = "homeId")
    List<User> users;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1479825551)
    private transient HomeDao myDao;

    @Generated(hash = 1636251617)
    public Home(long hid, String homeName, long ownerId) {
        this.hid = hid;
        this.homeName = homeName;
        this.ownerId = ownerId;
    }
    @Generated(hash = 858147737)
    public Home() {
    }
    public long getHid() {
        return this.hid;
    }
    public void setHid(long hid) {
        this.hid = hid;
    }
    public String getHomeName() {
        return this.homeName;
    }
    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }
    @Generated(hash = 1847295403)
    private transient Long owner__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2046543160)
    public User getOwner() {
        long __key = this.ownerId;
        if (owner__resolvedKey == null || !owner__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            User ownerNew = targetDao.load(__key);
            synchronized (this) {
                owner = ownerNew;
                owner__resolvedKey = __key;
            }
        }
        return owner;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2074386223)
    public void setOwner(@NotNull User owner) {
        if (owner == null) {
            throw new DaoException(
                    "To-one property 'ownerId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.owner = owner;
            ownerId = owner.getUid();
            owner__resolvedKey = ownerId;
        }
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1852686219)
    public List<User> getUsers() {
        if (users == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            List<User> usersNew = targetDao._queryHome_Users(hid);
            synchronized (this) {
                if(users == null) {
                    users = usersNew;
                }
            }
        }
        return users;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1027274768)
    public synchronized void resetUsers() {
        users = null;
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    public long getOwnerId() {
        return this.ownerId;
    }
    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 666555591)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getHomeDao() : null;
    }
}
