package com.app.admin.sellah.model.extra;

public class NotificationMessModel {

    public int profileImage;
    public String Heading;
    public String SubHeading;

    public NotificationMessModel(int profileImage, String heading, String subHeading) {
        this.profileImage = profileImage;
        Heading = heading;
        SubHeading = subHeading;
    }

    public int getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(int profileImage) {
        this.profileImage = profileImage;
    }

    public String getHeading() {
        return Heading;
    }

    public void setHeading(String heading) {
        Heading = heading;
    }

    public String getSubHeading() {
        return SubHeading;
    }

    public void setSubHeading(String subHeading) {
        SubHeading = subHeading;
    }
}
