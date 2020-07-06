package com.modiejun.cloudfiles.FileUpload.POJO;

public class FolderResponse {

    private String folderLink;
    private String folderName;

    public FolderResponse(String folderLink, String folderName) {
        this.folderLink = folderLink;
        this.folderName = folderName;
    }

    public String getFolderLink() {
        return folderLink;
    }

    public String getFolderName() {
        return folderName;
    }

}
