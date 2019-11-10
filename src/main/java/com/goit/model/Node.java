package com.goit.model;

import java.io.Serializable;
import java.util.List;

public class Node implements Serializable {
    private Integer id;
    private String text;
    private String icon;
    private List<Node> children;
    private byte[] bytes;

    public Node() {
    }

    public Node(Integer id, String text, String icon, List<Node> children, byte[] bytes) {
        this.id = id;
        this.text = text;
        this.icon = icon;
        this.children = children;
        this.bytes = bytes;
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

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
