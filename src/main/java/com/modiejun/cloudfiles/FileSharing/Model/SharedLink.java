package com.modiejun.cloudfiles.FileSharing.Model;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "sharedLink")
@Table
public class SharedLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(nullable = false,length = 64,unique = true)
    private String shareCode;

    @Column(nullable = false)
    private String sharedBy;

    @Column(nullable = false)
    private Date created= new Date(System.currentTimeMillis());

    // In Seconds
    @Column(nullable = false)
    private Integer validDuration= 30;

    @Column(nullable = false)
    private String fileToBeAccessed;

    @Column(nullable = false)
    private String downloadLink;

    @Column(nullable = false)
    private boolean currentlyValid =true;

    public SharedLink() {
    }

    public SharedLink(String shareCode, String sharedBy,String fileToBeAccessed,String downloadLink) {
        this.shareCode = shareCode;
        this.sharedBy = sharedBy;
        this.fileToBeAccessed = fileToBeAccessed;
        this.downloadLink=downloadLink;
    }

    public String getId() {
        return id;
    }

    public SharedLink setId(String id) {
        this.id = id;
        return this;
    }

    public String getShareCode() {
        return shareCode;
    }

    public SharedLink setShareCode(String shareCode) {
        this.shareCode = shareCode;
        return this;
    }

    public String getSharedBy() {
        return sharedBy;
    }

    public SharedLink setSharedBy(String sharedBy) {
        this.sharedBy = sharedBy;
        return this;
    }

    public Date getCreated() {
        return created;
    }

    public SharedLink setCreated(Date created) {
        this.created = created;
        return this;
    }

    public Integer getValidDuration() {
        return validDuration;
    }

    public SharedLink setValidDuration(Integer validDuration) {
        this.validDuration = validDuration;
        return this;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public SharedLink setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
        return this;
    }

    public String getFileToBeAccessed() {
        return fileToBeAccessed;
    }

    public SharedLink setFileToBeAccessed(String fileToBeAccessed) {
        this.fileToBeAccessed = fileToBeAccessed;
        return this;
    }

    public boolean isCurrentlyValid() {
        return currentlyValid;
    }

    public SharedLink setCurrentlyValid(boolean currentlyValid) {
        this.currentlyValid = currentlyValid;
        return this;
    }
}

