package net.simplifiedcoding.shelounge.models;

/**
 * Created by tjuhi on 6/1/2017.
 */

public class Feedback {
    private String user;
    private String rating;
    private String comment;
    private String date;

    public Feedback() {

    }

    public Feedback(String user, String rating, String comment, String date) {
        super();
        this.user = user;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
