package com.modiejun.cloudfiles.FileUpload;

import com.modiejun.cloudfiles.FileUpload.POJO.SavedFile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("fileRepository")
public interface FileObjDAO  extends CrudRepository<SavedFile,Long> {
    Optional<SavedFile> findAllByOwner( String owner);

    @Query(value="select f from files as f  where lower(f.fileName) like lower(concat('%', ?1,'%'))")
    Optional<List<SavedFile>> findAllByFileNameLike( String searchFile);

    Optional<SavedFile>findFirstByFileName(String fileName);
}
