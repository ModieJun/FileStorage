package com.modiejun.cloudfiles.FileUpload.POJO;

public class Folder {
    private final String path;
    private final String folderName;

    public Folder(String folderName,String path) {
        this.path = path;
        this.folderName = folderName;
    }

    public String getPath() {
        return path;
    }


    public String getFolderName() {
        return folderName;
    }

}
