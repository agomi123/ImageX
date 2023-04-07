package com.example.myapplication;

public class ImageClass {

    // variables for storing
    // our image and name.
    private String name;
    private String imgUrl;

    public ImageClass() {

    }
    public ImageClass(String imgUrl) {
          this.imgUrl=imgUrl;
    }

    // constructor for our object class.
    public ImageClass(String name, String imgUrl) {
        this.name = name;
        this.imgUrl = imgUrl;
    }

    // getter and setter methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
