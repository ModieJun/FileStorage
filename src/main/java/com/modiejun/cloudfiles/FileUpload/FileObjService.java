package com.modiejun.cloudfiles.FileUpload;

import com.modiejun.cloudfiles.FileUpload.POJO.SavedFile;

import java.util.Optional;

public interface FileObjService {
    boolean saveFile(SavedFile file);

    boolean deleteFile(Long id);

    boolean updateFile(SavedFile file);

    Optional<SavedFile> search(String owner, String fileName);
}
