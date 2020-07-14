package com.modiejun.cloudfiles.FileSharing.Model;

public class SharelinkResponse {
    private final String fileName;
    private final String shareUri;

    public SharelinkResponse(String fileName, String shareUri) {
        this.fileName = fileName;
        this.shareUri = shareUri;
    }

    public String getFileName() {
        return fileName;
    }

    public String getShareUri() {
        return shareUri;
    }
}
