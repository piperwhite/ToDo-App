package com.piperwhite.todoapp;

/**
 * Created by BLANCA on 24/01/2016.
 */
public class Item {
    private long id;
    private String text;

    public Item(String text){
        this.text= text;
        id= -1;
    }

    public Item(String text, int id){
        this.text= text;
        this.id= id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
