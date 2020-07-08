package com.modiejun.cloudfiles.FileUpload;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    void store(String directory, MultipartFile file);

    void store(String directory, MultipartFile[] files);

    Stream<Path> loadAllFilesInDirectory(String directory);

    Stream<Path> loadAllSubdirectories(String directory);

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

    void delete(String filename);

    void createNewDirectory(String directoryName);

    void deleteDirectory(String directoryName);

    String getParentFolderLink(String directory);

}
