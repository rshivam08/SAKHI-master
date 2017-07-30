package net.simplifiedcoding.shelounge.models;

/**
 * Created by dragoon on 18-Jan-17.
 */
public class DataModel {

    String com_date, com_id;
    int com_status;
    String image;

    public DataModel(String date, String id, String image, int status) {
        this.com_date = date;
        this.com_id = id;
        this.image = image;
        this.com_status = status;
    }

    public Integer getStatus() {
        return com_status;
    }

    public String getDate() {
        return com_date;
    }

    public String getImage() {
        return image;
    }

    public String getId() {
        return com_id;
    }
}
