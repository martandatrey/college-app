package com.pimpmyapp.collegeapp.pojo;

/**
 * Created by choud on 20-07-2017.
 */

public class NoticePojo {

    String image = "";
    String date = "";
    String title = "";
    String noticeID = "";
    String desc = "";
    String imageSizeString = "";
    String addedBy = "";
    boolean published = false;
    String addedOn = "";
    String category = "";
    long imageSize = 0;

    @Override
    public String toString() {
        return "NoticePojo{" +
                "image='" + image + '\'' +
                ", date='" + date + '\'' +
                ", title='" + title + '\'' +
                ", noticeID='" + noticeID + '\'' +
                ", desc='" + desc + '\'' +
                ", imageSizeString='" + imageSizeString + '\'' +
                ", addedBy='" + addedBy + '\'' +
                ", published=" + published +
                ", addedOn='" + addedOn + '\'' +
                ", category='" + category + '\'' +
                ", imageSize=" + imageSize +
                '}';
    }

    public long getImageSize() {
        return imageSize;
    }

    public void setImageSize(long imageSize) {
        this.imageSize = imageSize;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageSizeString() {
        return imageSizeString;
    }

    public void setImageSizeString(String imageSizeString) {
        this.imageSizeString = imageSizeString;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }


    public String getNoticeID() {
        return noticeID;
    }

    public void setNoticeID(String noticeID) {
        this.noticeID = noticeID;
    }


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
