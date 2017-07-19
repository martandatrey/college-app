package com.pimpmyapp.collegeapp.pojo;

/**
 * Created by choud on 20-07-2017.
 */

public class NoticePojo {

    String image;
    String date;
    String title;
    String addedBy;

    @Override
    public String toString() {
        return "NoticePojo{" +
                "image='" + image + '\'' +
                ", date='" + date + '\'' +
                ", title='" + title + '\'' +
                ", addedBy='" + addedBy + '\'' +
                ", noticeID='" + noticeID + '\'' +
                ", published=" + published +
                '}';
    }

    public String getNoticeID() {
        return noticeID;
    }

    public void setNoticeID(String noticeID) {
        this.noticeID = noticeID;
    }

    String noticeID;
    boolean published  = false;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }
}
