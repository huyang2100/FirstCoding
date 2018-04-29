package net.sourceforge.simcpux.bean;

import java.util.Date;
import java.util.UUID;

/**
 * Created by yanghu on 2018/4/29.
 */

public class Criminal {
    //id
    private UUID id;
    //日期
    private Date date;
    //标题
    private String title;
    //是否解决
    private boolean solved;

    public Criminal(){
        id = UUID.randomUUID();
        date = new Date();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }
}
