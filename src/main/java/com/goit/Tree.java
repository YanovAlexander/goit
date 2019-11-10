package com.goit;

import java.io.Serializable;
import java.util.List;

public class Tree implements Serializable {
    private Integer id;
    private String text;
    private String icon;
    private List<Tree> children;

    public Tree() {
    }

    public Tree(Integer id, String text, String icon, List<Tree> children) {
        this.id = id;
        this.text = text;
        this.icon = icon;
        this.children = children;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Tree> getChildren() {
        return children;
    }

    public void setChildren(List<Tree> children) {
        this.children = children;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
