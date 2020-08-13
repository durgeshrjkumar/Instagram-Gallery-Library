package com.teafinn.advance_gallery.commons.models;

/*
 * Created by Guillaume on 21/11/2016.
 */

public class Session {

    private static Session sInstance = null;
    private String mFileToUpload;

    private Session() {
    }

    public static Session getInstance() {
        if (sInstance == null) {
            sInstance = new Session();
        }
        return sInstance;
    }

    public String getFileToUpload() {
        return mFileToUpload;
    }

    public void setFileToUpload(String fileToUpload) {
        mFileToUpload = fileToUpload;
    }

}