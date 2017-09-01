package com.hagk.dongni.bean;

import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2017-9-1.
 */

public class AnswerJson {

    private String surveyID;

    public Collection<Answer> getAnswer() {
        return answer;
    }

    public void setAnswer(Collection<Answer> answer) {
        this.answer = answer;
    }

    private Collection<Answer> answer;

    public String getSurveyID() {
        return surveyID;
    }

    public void setSurveyID(String surveyID) {
        this.surveyID = surveyID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    private String userID;
}
