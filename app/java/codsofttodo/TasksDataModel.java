package com.example.codsofttodo;

public class TasksDataModel {

    private String tId, tTitle, tSubtask, tAddedDate, tDueDate, tDueTime, tStatus;
    private boolean isRemainderSet;

    TasksDataModel(){

    }

    public TasksDataModel(String tId, String tTitle, String tSubtask, String tAddedDate, String tDueDate, String tDueTime, boolean isRemainderSet, String tStatus) {
        this.tId = tId;
        this.tTitle = tTitle;
        this.tSubtask = tSubtask;
        this.tAddedDate = tAddedDate;
        this.tDueDate = tDueDate;
        this.tDueTime = tDueTime;
        this.isRemainderSet = isRemainderSet;
        this.tStatus = tStatus;
    }

    public String gettId() {
        return tId;
    }

    public void settId(String tId) {
        this.tId = tId;
    }

    public String gettTitle() {
        return tTitle;
    }

    public void settTitle(String tTitle) {
        this.tTitle = tTitle;
    }

    public String gettSubtask() {
        return tSubtask;
    }

    public void settSubtask(String tSubtask) {
        this.tSubtask = tSubtask;
    }

    public String gettAddedDate() {
        return tAddedDate;
    }

    public void settAddedDate(String tAddedDate) {
        this.tAddedDate = tAddedDate;
    }

    public String gettDueDate() {
        return tDueDate;
    }

    public void settDueDate(String tDueDate) {
        this.tDueDate = tDueDate;
    }

    public String gettDueTime() {
        return tDueTime;
    }

    public void settDueTime(String tDueTime) {
        this.tDueTime = tDueTime;
    }

    public boolean isRemainderSet() {
        return isRemainderSet;
    }

    public void setRemainderSet(boolean remainderSet) {
        isRemainderSet = remainderSet;
    }

    public String gettStatus() {
        return tStatus;
    }

    public void settStatus(String tStatus) {
        this.tStatus = tStatus;
    }
}
