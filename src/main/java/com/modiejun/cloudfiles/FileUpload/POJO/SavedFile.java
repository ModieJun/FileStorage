package com.modiejun.cloudfiles.FileUpload.POJO;

import javax.annotation.Nonnull;
import javax.persistence.*;

@Entity(name = "files")
@Table
public class SavedFile  {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String fileName;

    @Nonnull
    @Column
    private  String owner;

    @Nonnull
    @Column
    private  String filePath;

    public SavedFile() {
    }

    public SavedFile( String fileName, @Nonnull String owner, @Nonnull String filePath) {
        this.fileName = fileName;
        this.owner = owner;
        this.filePath = filePath;
    }


    public String getFileName() {
        return fileName;
    }

    public SavedFile setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public SavedFile(@Nonnull String owner, @Nonnull String filePath) {
        this.owner = owner;
        this.filePath = filePath;
    }

    @Nonnull
    public String getOwner() {
        return owner;
    }

    @Nonnull
    public String getFilePath() {
        return filePath;
    }

    public SavedFile setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getId() {
        return id;
    }

    public SavedFile setOwner(@Nonnull String owner) {
        this.owner = owner;
        return this;
    }

    public SavedFile setFilePath(@Nonnull String filePath) {
        this.filePath = filePath;
        return this;
    }
}
