package com.hagk.dongni.bean;

/**
 * Created by Administrator on 2017-9-5.
 */

public class SurveyHistory {
    private String surveyName;

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public String getSurveyTime() {
        return surveyTime;
    }

    public void setSurveyTime(String surveyTime) {
        this.surveyTime = surveyTime;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    private String surveyTime;
    private int score;
}
