package com.modiejun.cloudfiles.FileUpload.POJO;

public class FileResponse {
    public final String filelink;
    public final String fileName;
    public final String fileDeleteLink;

    public FileResponse(String filelink, String fileName, String fileDeleteLink) {
        this.filelink = filelink;
        this.fileName = fileName;
        this.fileDeleteLink = fileDeleteLink;
    }

    public String getFilelink() {
        return filelink;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileDeleteLink() {
        return fileDeleteLink;
    }

}
