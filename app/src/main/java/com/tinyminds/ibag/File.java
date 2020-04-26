package com.tinyminds.ibag;

public class File {
    private int mID;
    private String mTitle, mFile, mWeight;

    public File(int id, String title, String file, String weight) {
        mID = id;
        mTitle = title;
        mFile = file;
        mWeight = weight;
    }

    public int getId() {
        return mID;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getFile() {
        return mFile;
    }

    public String getWeight() {
        return mWeight;
    }

}
