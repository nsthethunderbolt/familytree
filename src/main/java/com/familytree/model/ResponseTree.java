package com.familytree.model;

import java.util.ArrayList;
import java.util.List;

public class ResponseTree {
    private long id;
    private char gender;
    private String nickName;
    private String name;
    //Member spouse;
    private String level;
    public List<ResponseTree> children = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public Member getSpouse() {
        return spouse;
    }
    public void setSpouse(Member spouse) {
        this.spouse = spouse;
    }*/
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<ResponseTree> getChildren() {
        return children;
    }

    public void setChildren(List<ResponseTree> children) {
        this.children = children;
    }

}
