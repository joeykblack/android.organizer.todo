package joeykblack.organizer.todo.model;

/**
 * Created by joey on 6/5/2016.
 */
public enum Group {
    ONE("Top Priority"),
    TWO("Next up"),
    THREE("Soon"),
    FOUR("Eventually"),
    FIVE("Meh"),
    LOW("Maybe");

    private String message;
    Group(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static Group valueOf(int groupIndex) {
        Group group = LOW;
        if (groupIndex < values().length) {
            group = values()[groupIndex];
        }
        return group;
    }
}
