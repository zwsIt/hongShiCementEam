package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by wangshizhan on 2017/12/14.
 * Email:wangshizhan@supcon.com
 */

@Entity
public class AccountInfo extends BaseEntity {

    public long id;

    @Id
    public long userId;    //用户id
    public String userName; //用户名

    public long staffId;   //用户的人员id
    public String staffCode;   //用户的人员编码
    public String staffName; //用户的真实姓名
    public long departmentId;  //用户部门id
    public String departmentName;  //用户部门id
    public long cid;   //公司id
    public String companyName; //公司名称
    public long positionId;    //岗位id
    public String positionName;    //岗位名称
    public String password = "";
    public String ip = "";
    public String date = "";
    public long imageId;		//头像img id
    public String imagePath;		//头像地址
    public String roleIds;		//角色Id
    public String roleNames;	//角色名字
    public String uuid;		//uuid
    public String loginTime;
    public String mobile;
    public String email;
    public long firstDepartmentId;      //第一部门id
    public String firstDepartmentName;  //第一部门名称
    @Generated(hash = 108112198)
    public AccountInfo(long id, long userId, String userName, long staffId,
            String staffCode, String staffName, long departmentId,
            String departmentName, long cid, String companyName, long positionId,
            String positionName, String password, String ip, String date,
            long imageId, String imagePath, String roleIds, String roleNames,
            String uuid, String loginTime, String mobile, String email,
            long firstDepartmentId, String firstDepartmentName) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.staffId = staffId;
        this.staffCode = staffCode;
        this.staffName = staffName;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.cid = cid;
        this.companyName = companyName;
        this.positionId = positionId;
        this.positionName = positionName;
        this.password = password;
        this.ip = ip;
        this.date = date;
        this.imageId = imageId;
        this.imagePath = imagePath;
        this.roleIds = roleIds;
        this.roleNames = roleNames;
        this.uuid = uuid;
        this.loginTime = loginTime;
        this.mobile = mobile;
        this.email = email;
        this.firstDepartmentId = firstDepartmentId;
        this.firstDepartmentName = firstDepartmentName;
    }
    @Generated(hash = 1230968834)
    public AccountInfo() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getUserId() {
        return this.userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public long getStaffId() {
        return this.staffId;
    }
    public void setStaffId(long staffId) {
        this.staffId = staffId;
    }
    public String getStaffCode() {
        return this.staffCode;
    }
    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }
    public String getStaffName() {
        return this.staffName;
    }
    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
    public long getDepartmentId() {
        return this.departmentId;
    }
    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }
    public String getDepartmentName() {
        return this.departmentName;
    }
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    public long getCid() {
        return this.cid;
    }
    public void setCid(long cid) {
        this.cid = cid;
    }
    public String getCompanyName() {
        return this.companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public long getPositionId() {
        return this.positionId;
    }
    public void setPositionId(long positionId) {
        this.positionId = positionId;
    }
    public String getPositionName() {
        return this.positionName;
    }
    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getIp() {
        return this.ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public long getImageId() {
        return this.imageId;
    }
    public void setImageId(long imageId) {
        this.imageId = imageId;
    }
    public String getImagePath() {
        return this.imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public String getRoleIds() {
        return this.roleIds;
    }
    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }
    public String getRoleNames() {
        return this.roleNames;
    }
    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }
    public String getUuid() {
        return this.uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getLoginTime() {
        return this.loginTime;
    }
    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }
    public long getFirstDepartmentId() {
        return this.firstDepartmentId;
    }
    public void setFirstDepartmentId(long firstDepartmentId) {
        this.firstDepartmentId = firstDepartmentId;
    }
    public String getFirstDepartmentName() {
        return this.firstDepartmentName;
    }
    public void setFirstDepartmentName(String firstDepartmentName) {
        this.firstDepartmentName = firstDepartmentName;
    }
    public String getMobile() {
        return this.mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

}
