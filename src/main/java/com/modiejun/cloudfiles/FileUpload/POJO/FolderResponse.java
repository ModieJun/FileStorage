package com.modiejun.cloudfiles.FileUpload.POJO;

public class FolderResponse {
    private final String path;
    private final String folderName;
    private final String folderDelete;

    public FolderResponse(String folderName, String path, String folderDelete) {
        this.path = path;
        this.folderName = folderName;
        this.folderDelete = folderDelete;
    }

    public String getPath() {
        return path;
    }

    public String getFolderName() {
        return folderName;
    }

    public String getFolderDelete() {
        return folderDelete;
    }
}
