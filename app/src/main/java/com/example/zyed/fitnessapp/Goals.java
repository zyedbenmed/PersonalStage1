package com.example.zyed.fitnessapp;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

/**
 * Created by Z'YED on 24/07/2018.
 */

public class Goals extends SugarRecord {

    private String title, actual , goal ;

    private  Member member;

    private String old_actual , old_goal , note;

    public Goals(){}

    public Goals ( String title , String actual , String goal , Member member){
        this.title = title;
        this.actual = actual;
        this.goal=goal;
        this.member = member;
    }

    public Goals(String title, String actual, String goal, Member member, String old_actual, String old_goal, String note) {
        this.title = title;
        this.actual = actual;
        this.goal = goal;
        this.member = member;
        this.old_actual = old_actual;
        this.old_goal = old_goal;
        this.note=note;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getOld_actual() {
        return old_actual;
    }

    public void setOld_actual(String old_actual) {
        this.old_actual = old_actual;
    }

    public String getOld_goal() {
        return old_goal;
    }

    public void setOld_goal(String old_goal) {
        this.old_goal = old_goal;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
