package joeykblack.organizer.todo.model;

import java.io.Serializable;
import java.util.Date;

import joeykblack.organizer.todo.database.TaskDbHelper;
import joeykblack.organizer.todo.util.DateUtil;
import joeykblack.organizer.todo.util.RankCalculator;
import joeykblack.organizer.todo.util.impl.ContractDateUtil;
import joeykblack.organizer.todo.util.impl.PriorityAndDateRankCalculator;

/**
 * Created by joey on 5/21/2016.
 */
public class Task implements Comparable<Task>, Serializable {

    public static final String TAG = "Task";

    private long id;
    private String title;
    private int priority;
    private Date date;
    private Group group;
    private RankCalculator rankCalculator = new PriorityAndDateRankCalculator();
    private DateUtil dateUtil = new ContractDateUtil();

    private static final long NO_RANK = -1l;
    private long rank = NO_RANK;

    public long getId() {
        return id;
    }

    public Task setId(long id) {
        this.id = id;
        return this;
    }

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

    public Group getGroup() {
        return group;
    }

    public Task setGroup(Group group) {
        this.group = group;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public Task setDate(Date date) {
        this.date = date;
        return this;
    }

    public long getRank() {
        rank = rank != NO_RANK ? rank : rankCalculator.getRank(this);
        return rank;
    }

    @Override
    public String toString() {
        return title;
    }
    public String getDetails() {
        String dateString = "";
        if ( this.date != null ) {
            dateString = " - Due: " + dateUtil.serializeDateDisplay(this.date);
        }
        return group.getMessage() + dateString;
    }

    @Override
    public int compareTo(Task another) {
        int order = 0;
        long rank1 = this.getRank();
        long rank2 = another.getRank();
        if ( rank1 > rank2 ) {
            order = -1;
        }
        else if ( rank1 < rank2 ) {
            order = 1;
        }
        return order;
    }


}