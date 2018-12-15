package com.stu.system.eventbus;

public class StuAddEvent {
    public String cid;

    public StuAddEvent(String cid) {
        this.cid = cid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
