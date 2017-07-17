package com.example.utkarsh.todo;

import java.io.Serializable;

/**
 * Created by Utkarsh on 7/16/2017.
 */

public class Details implements Serializable{
    int id;
    String title;
    String date;
    String time;
    String desc;
    String priority;
    String category;

    public Details(int id,String title, String date, String time, String desc, String priority, String category) {
        this.id = id;
        this.title=title;
        this.date = date;
        this.time = time;
        this.desc = desc;
        this.priority = priority;
        this.category = category;
    }
}
