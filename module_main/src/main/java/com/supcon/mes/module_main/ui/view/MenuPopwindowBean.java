package com.supcon.mes.module_main.ui.view;

/**
 * @author yangfei.cao
 * @ClassName depot
 * @date 2018/8/13
 * ------------- Description -------------
 */
public class MenuPopwindowBean {

    private String name;
    private int num;
    private int type;
    private String router;
    private String tag;
    private boolean isPower;
    private String menuOperateCodes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRouter() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isPower() {
        return isPower;
    }

    public void setPower(boolean power) {
        isPower = power;
    }

    public String getMenuOperateCodes() {
        return menuOperateCodes;
    }
}
