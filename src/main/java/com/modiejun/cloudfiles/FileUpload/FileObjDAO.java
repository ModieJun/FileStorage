package com.modiejun.cloudfiles.FileUpload;

import com.modiejun.cloudfiles.FileUpload.POJO.SavedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("fileRepository")
public interface FileObjDAO  extends CrudRepository<SavedFile,Long> {
    Optional<SavedFile> findAllByOwner( String owner);

    Optional<SavedFile>findAllByOwnerAndFileNameContains(String owner, String fileName);
}
