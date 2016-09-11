package com.soapwang.databasedemoapp;

/**
 * Created by Acer on 2016/7/1.
 */
public class MyNote {

    private String title;
    private String content;
    private String date;
    private String id;
    public MyNote(String id, String t, String c, String d) {
        this.id = id;
        title = t;
        content = c;
        date = d;
    }

    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
    public String getDate() {
        return date;
    }
    public String getId() {
        return id;
    }
}
