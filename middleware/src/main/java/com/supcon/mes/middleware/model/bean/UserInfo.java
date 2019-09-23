package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by wangshizhan on 2018/7/10
 * Email:wangshizhan@supcom.com
 */
@Entity
public class UserInfo extends BaseEntity implements CommonSearchEntity {

    public String name;
    public String host;
    public String namePinyin;
    public boolean clicked;
    @Id
    public Long id;

    public Long staffId;
    public String staffName;
    public String staffCode;

    @ToOne
    public Staff staff;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 437952339)
    private transient UserInfoDao myDao;




    @Generated(hash = 2033891023)
    public UserInfo(String name, String host, String namePinyin, boolean clicked,
            Long id, Long staffId, String staffName, String staffCode) {
        this.name = name;
        this.host = host;
        this.namePinyin = namePinyin;
        this.clicked = clicked;
        this.id = id;
        this.staffId = staffId;
        this.staffName = staffName;
        this.staffCode = staffCode;
    }

    @Generated(hash = 1279772520)
    public UserInfo() {
    }

    @Generated(hash = 218067949)
    private transient boolean staff__refreshed;




    @Override
    public String getSearchId() {
        return String.valueOf(staffId);
    }

    @Override
    public String getSearchName() {
        return  staffName;
    }

    @Override
    public String getSearchProperty() {
        return staffCode;
    }

    @Override
    public String getSearchPinyin() {
        return namePinyin;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getNamePinyin() {
        return this.namePinyin;
    }

    public void setNamePinyin(String namePinyin) {
        this.namePinyin = namePinyin;
    }

    public boolean getClicked() {
        return this.clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStaffId() {
        return this.staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return this.staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffCode() {
        return this.staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 465354656)
    public Staff getStaff() {
        if (staff != null || !staff__refreshed) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            StaffDao targetDao = daoSession.getStaffDao();
            targetDao.refresh(staff);
            staff__refreshed = true;
        }
        return staff;
    }

    /** To-one relationship, returned entity is not refreshed and may carry only the PK property. */
    @Generated(hash = 1969539554)
    public Staff peakStaff() {
        return staff;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1978989256)
    public void setStaff(Staff staff) {
        synchronized (this) {
            this.staff = staff;
            staff__refreshed = true;
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 821180768)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserInfoDao() : null;
    }



}
