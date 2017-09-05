package com.hagk.dongni.bean;

/**
 * Created by Administrator on 2017-9-5.
 */

public class CourseDigest {
    private String title;
    private String time;
    private String lessonID;

    public String getLessonID() {
        return lessonID;
    }

    public void setLessonID(String lessonID) {
        this.lessonID = lessonID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
