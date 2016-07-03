package com.familytree.model;

import java.util.ArrayList;
import java.util.List;

public class Member {
    private long id;

    public Member() {
        this.level = "#";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    //Couple parent;
    private String nickName;
    private String name;
    private Member spouse;
    private String level;
    public List<Member> children = new ArrayList<>();
    private Member parent;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Member getSpouse() {
        return spouse;
    }

    public List<Member> getChildren() {
        return children;
    }

    public void setChildren(List<Member> children) {
        this.children = children;
    }

    public void setSpouse(Member spouse) {
        this.spouse = spouse;
    }

    private char gender;

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getName() {
        return this.name;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setParent(Member parent) {
        this.parent = parent;
    }

    public Member getParent() {
        return this.parent;
    }

    public String toString() {
        return "[" + nickName + "," + name + "," + gender + "," + level + "]";
    }
}