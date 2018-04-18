package net.sourceforge.simcpux.bean;

import java.io.Serializable;

/**
 * Created by yanghu on 2018/4/5.
 */

public class Fruit implements Serializable{
    //标题
    public static final int ITEM_TYPE_TITLE = 0;
    //内容
    public static final int ITEM_TYPE_CONTENT = 1;

    public Fruit() {
    }

    public Fruit(String name) {
        this.name = name;
    }

    private String name;
    private String category;
    private int resId;
    private int type;

    public Fruit(String name, int resId) {
        this.name = name;
        this.resId = resId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
