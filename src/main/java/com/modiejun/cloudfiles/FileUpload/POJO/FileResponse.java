package com.modiejun.cloudfiles.FileUpload.POJO;

public class FileResponse {
    public final String fileLink;
    public final String fileName;
    public final String fileDeleteLink;

    public FileResponse(String filelink, String fileName, String fileDeleteLink) {
        this.fileLink = filelink;
        this.fileName = fileName;
        this.fileDeleteLink = fileDeleteLink;
    }

    public String getFileLink() {
        return fileLink;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileDeleteLink() {
        return fileDeleteLink;
    }

}
