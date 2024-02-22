package io.github.taikonaut3.model;

import java.io.Serializable;

public class ChildObject implements Serializable {

    private String someValue;

    private GrandchildObject grandchild;

    // getters 和 setters

    public String getSomeValue() {
        return someValue;
    }

    public void setSomeValue(String someValue) {
        this.someValue = someValue;
    }

    public GrandchildObject getGrandchild() {
        return grandchild;
    }

    public void setGrandchild(GrandchildObject grandchild) {
        this.grandchild = grandchild;
    }

}
