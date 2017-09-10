package com.example.dontknow.acses;

/**
 * Created by Dont know on 27-05-2017.
 */
public class Blog_accept {
    private String description;
    private String title;
    private String userName;
    private String postDate;
    private String userUID;
    private String image;
    private long TimeStamp;

    public Blog_accept(){}

    public Blog_accept(String description, String title, String image,String postDate,String userName,String userUID,long TimeStamp) {
        this.userName=userName;
        this.postDate=postDate;
        this.description = description;
        this.title = title;
        this.image = image;
        this.userUID= userUID;
        this.TimeStamp = TimeStamp;
    }

    public String getUserUID() {
        return userUID;
    }

    public long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        TimeStamp = timeStamp;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
