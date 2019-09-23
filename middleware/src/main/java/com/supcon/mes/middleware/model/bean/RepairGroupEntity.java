package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.EamApplication;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * RepairGroupEntity 维修组
 * created by zhangwenshuai1 2018/8/13
 */
@Entity
public class RepairGroupEntity extends BaseEntity {

    @Id
    public Long id;
    public String name;
    public Boolean isUse;
    public String ip = EamApplication.getIp();
    @ToOne(joinProperty  = "id")
    public Department owerDepart;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 74598430)
    private transient RepairGroupEntityDao myDao;
    @Generated(hash = 2026390346)
    private transient Long owerDepart__resolvedKey;
    @Generated(hash = 1319214821)
    public RepairGroupEntity(Long id, String name, Boolean isUse, String ip) {
        this.id = id;
        this.name = name;
        this.isUse = isUse;
        this.ip = ip;
    }
    @Generated(hash = 1385037438)
    public RepairGroupEntity() {
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 510038998)
    public Department getOwerDepart() {
        Long __key = this.id;
        if (owerDepart__resolvedKey == null || !owerDepart__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DepartmentDao targetDao = daoSession.getDepartmentDao();
            Department owerDepartNew = targetDao.load(__key);
            synchronized (this) {
                owerDepart = owerDepartNew;
                owerDepart__resolvedKey = __key;
            }
        }
        return owerDepart;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 275593131)
    public void setOwerDepart(Department owerDepart) {
        synchronized (this) {
            this.owerDepart = owerDepart;
            id = owerDepart == null ? null : owerDepart.getId();
            owerDepart__resolvedKey = id;
        }
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
    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setIsUse(Boolean isUse) {
        this.isUse = isUse;
    }
    public Boolean getIsUse() {
        return this.isUse;
    }
    public String getIp() {
        return this.ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 33065919)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRepairGroupEntityDao() : null;
    }

}
