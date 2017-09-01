package com.hagk.dongni.bean;

/**
 * Created by Administrator on 2017-9-1.
 */

public class Answer {
    private String topicName;
    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getAnswerIndex() {
        return answerIndex;
    }

    public void setAnswerIndex(int answerIndex) {
        this.answerIndex = answerIndex;
    }

    public String getTopicName() {

        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    private int answerIndex;
}
