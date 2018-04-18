package net.sourceforge.simcpux.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yanghu on 2018/4/12.
 */

public class AreaItem implements Serializable {
    //省
    public static final int LEVEL_PROVINCE = 0;
    //市
    public static final int LEVEL_CITY = 1;
    //县
    public static final int LEVEL_COUNTRY = 2;
    private int id;
    private String name;
    //上级code
    private int upCode;
    private int curLevel;

    public int getUpCode() {
        return upCode;
    }

    public void setUpCode(int upCode) {
        this.upCode = upCode;
    }


    public int getCurLevel() {
        return curLevel;
    }

    public void setCurLevel(int curLevel) {
        this.curLevel = curLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AreaItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", upCode=" + upCode +
                ", curLevel=" + curLevel +
                '}';
    }
}
