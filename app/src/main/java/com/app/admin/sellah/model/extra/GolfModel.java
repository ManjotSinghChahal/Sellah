package com.app.admin.sellah.model.extra;

public class GolfModel {
    int image;
    String profile_name;
    String comment_desc;
    String profile_time;

    public int getImage() {
        return image;
    }

    public String getProfile_time() {
        return profile_time;
    }

    public void setProfile_time(String profile_time) {
        this.profile_time = profile_time;
    }

    public void setImage(int image) {
        this.image = image;

    }

    public String getComment_desc() {
        return comment_desc;
    }

    public void setComment_desc(String comment_desc) {
        this.comment_desc = comment_desc;
    }

    public GolfModel(int image, String profile_name, String profile_time, String comment_desc) {
        this.image = image;
        this.profile_name = profile_name;
        this.profile_time = profile_time;
        this.comment_desc = comment_desc;
    }

    public String getProfile_name() {
        return profile_name;
    }

    public void setProfile_name(String profile_name) {
        this.profile_name = profile_name;
    }
}
