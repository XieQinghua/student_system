package com.stu.system.eventbus;

public class ShowStuManClassEvent {
    private int classIndex;

    public ShowStuManClassEvent(int classIndex) {
        this.classIndex = classIndex;
    }

    public int getClassIndex() {
        return classIndex;
    }

    public void setClassIndex(int classIndex) {
        this.classIndex = classIndex;
    }
}
