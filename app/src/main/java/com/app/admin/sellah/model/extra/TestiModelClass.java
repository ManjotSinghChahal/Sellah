package com.app.admin.sellah.model.extra;

public class TestiModelClass {
    int profileImage,ratingImage;
    String profile_name;
    String comment_desc;
    String profile_time;

    public TestiModelClass(int profileImage, int ratingImage, String profile_name, String comment_desc, String profile_time) {
        this.profileImage = profileImage;
        this.ratingImage = ratingImage;
        this.profile_name = profile_name;
        this.comment_desc = comment_desc;
        this.profile_time = profile_time;
    }


    public int getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(int profileImage) {
        this.profileImage = profileImage;
    }

    public int getRatingImage() {
        return ratingImage;
    }

    public void setRatingImage(int ratingImage) {
        this.ratingImage = ratingImage;
    }

    public String getProfile_name() {
        return profile_name;
    }

    public void setProfile_name(String profile_name) {
        this.profile_name = profile_name;
    }

    public String getComment_desc() {
        return comment_desc;
    }

    public void setComment_desc(String comment_desc) {
        this.comment_desc = comment_desc;
    }

    public String getProfile_time() {
        return profile_time;
    }

    public void setProfile_time(String profile_time) {
        this.profile_time = profile_time;
    }


}
