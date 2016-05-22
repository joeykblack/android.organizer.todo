package joeykblack.organizer.todo.model;

import java.util.Date;

import joeykblack.organizer.todo.util.RankCalculator;

/**
 * Created by joey on 5/21/2016.
 */
public class Task implements Comparable<Task> {

    private String title;
    private int priority;
    private Date date;

    private int rank = -1;

    public String getTitle() {
        return title;
    }

    public Task setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getPriority() {
        return priority;
    }

    public Task setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public Task setDate(Date date) {
        this.date = date;
        return this;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int compareTo(Task another) {
        int order = 0;
        int rank1 = rank != -1 ? rank : RankCalculator.getRank(this);
        int rank2 = another.rank != -1 ? another.rank : RankCalculator.getRank(another);
        if ( rank1 < rank2 ) {
            order = -1;
        }
        else if ( rank1 > rank2 ) {
            order = 1;
        }
        return order;
    }
}
