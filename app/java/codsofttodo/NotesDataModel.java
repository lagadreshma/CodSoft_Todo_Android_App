package com.example.codsofttodo;

public class NotesDataModel {

    String nId, nTitle, nSubpoint, nDesc, nAddDate;

    public NotesDataModel(){

    }

    public NotesDataModel(String nId, String nTitle, String nSubpoint, String nDesc, String nAddDate) {
        this.nId = nId;
        this.nTitle = nTitle;
        this.nSubpoint = nSubpoint;
        this.nDesc = nDesc;
        this.nAddDate = nAddDate;
    }

    public String getnId() {
        return nId;
    }

    public void setnId(String nId) {
        this.nId = nId;
    }

    public String getnTitle() {
        return nTitle;
    }

    public void setnTitle(String nTitle) {
        this.nTitle = nTitle;
    }

    public String getnSubpoint() {
        return nSubpoint;
    }

    public void setnSubpoint(String nSubpoint) {
        this.nSubpoint = nSubpoint;
    }

    public String getnDesc() {
        return nDesc;
    }

    public void setnDesc(String nDesc) {
        this.nDesc = nDesc;
    }

    public String getnAddDate() {
        return nAddDate;
    }

    public void setnAddDate(String nAddDate) {
        this.nAddDate = nAddDate;
    }
}
