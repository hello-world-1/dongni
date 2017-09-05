package com.hagk.dongni.bean;

/**
 * Created by Administrator on 2017-9-5.
 */

public class CourseDetail {
    /**
     * 课程描述
     */
    private String describe;
    /**
     * 课程老师
     */
    private String teacher;
    /**
     * 开课时间
     */
    private String startDate;
    /**
     * 节课时间
     */
    private String endDate;
    /**
     * 限制人数
     */
    private String limitPerson;
    /**
     * 报名截止日期
     */
    private String entryDeadline;
    /**
     * 课程周期
     */
    private String period;
    /**
     * 联系方式
     */
    private String phone;
    /**
     * 课程价格
     */
    private String price;
    /**
     * 已报名人数
     */
    private String applicantNumber;
    /**
     * 课程状态
     */
    private String status;
    /**
     * 上课时间
     */
    private String classTime;

    public String getClassTime() {
        return classTime;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLimitPerson() {
        return limitPerson;
    }

    public void setLimitPerson(String limitPerson) {
        this.limitPerson = limitPerson;
    }

    public String getEntryDeadline() {
        return entryDeadline;
    }

    public void setEntryDeadline(String entryDeadline) {
        this.entryDeadline = entryDeadline;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getApplicantNumber() {
        return applicantNumber;
    }

    public void setApplicantNumber(String applicantNumber) {
        this.applicantNumber = applicantNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
