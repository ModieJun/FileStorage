package com.modiejun.cloudfiles.FileUpload;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/*
    TODO : save file into the DB from the service (Need to create the obj   )
 */

@Service
public class FileStorageService implements StorageService {

    private final Path rootLocation;

    public FileStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void init() {
        try {
            if (!Files.exists(rootLocation))
                Files.createDirectory(rootLocation);

        } catch (IOException e) {
            throw new StorageException("Could not init Storage", e);
        }
    }

    @Override
    public void store(String directory, MultipartFile file) {
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty File : " + filename);
            }
            if (filename.contains("..")) {
                throw new StorageException("Cannot store file with two .. : " + filename);
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, Paths.get(directory).resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file: " + filename, e);
        }
    }

    /**
     * This function stores the array of files that has been uploaded
     *
     * @param directory - the current directory where the files are to be stored relative to the root
     * @param files     - An array of MulitPartFile
     */
    @Override
    public void store(String directory, MultipartFile[] files) {
        for (MultipartFile file : files) {
//            System.out.println(file.getOriginalFilename());
            this.store(directory, file);
        }
    }

        @Override
        public Stream<Path> loadAllFilesInDirectory (String directory){
            try {
                return Files.walk(Paths.get(directory), 1)
                        .filter(path -> (!path.toString().equals(directory) && path.toString().contains(".")))
                        .map(this.rootLocation::relativize);
            } catch (IOException e) {
                throw new StorageException("Failed to read stored files", e);
            }
        }

        @Override
        public Stream<Path> loadAllSubdirectories (String directory){
            try {
                return Files.walk(Paths.get(directory), 1)
                        .filter(path -> {
//                            System.out.println(path);
                            return !path.toString().equals("") && !path.toString().equals(directory) && !path.toString().contains(".");
                        }).map(this.rootLocation::relativize);
            } catch (IOException e) {
                throw new StorageException("Failed to read subdirectory Files", e);
            }
        }

        @Override
        public Path load (String filename){
            return rootLocation.resolve(filename);
        }

        @Override
        public Resource loadAsResource (String filename){
            try {
                Path file = load(filename);
                Resource resource = new UrlResource(file.toUri());
                if (resource.exists() || resource.isReadable()) {
                    return resource;
                } else {
                    throw new StorageFileNotFoundException("File could not be read" + filename);
                }
            } catch (MalformedURLException e) {
                throw new StorageFileNotFoundException("Could not read file " + filename);
            }
        }

        @Override
        public void deleteAll () {
            FileSystemUtils.deleteRecursively(rootLocation.toFile());
        }

        @Override
        public void delete (String filename){
//    TODO delete the folder if empty ?
            try {
//            System.out.println("Delete filename: " + filename);
                System.out.println("Delete Filename: " + load(filename).toString());
                if (Files.deleteIfExists(this.load(filename)))
                    System.out.println("Deleted");
            } catch (IOException e) {
                throw new StorageFileNotFoundException("Cold not find the file to delete");
            }
        }

        @Override
        public void deleteDirectory (String directoryName){
            try {
                System.out.println("Delete Directory Name : " + directoryName);
                if (Files.deleteIfExists(this.load(directoryName)))
                    System.out.println("Deleted Folder: " + directoryName);
            } catch (IOException e) {
                throw new StorageFileNotFoundException("Folder not Found");
            }
        }

        @Override
        public void createNewDirectory (String directoryName){
            try {
                Path directoryLocation = Paths.get(directoryName);
                Files.createDirectory(directoryLocation);
            } catch (IOException e) {
                throw new StorageException("Could not create new directory");
            }
        }

        @Override
        public String getParentFolderLink (String directoryName){
            if (directoryName.contains(File.separator)) {
                //has a parent directory
                return directoryName.substring(0, directoryName.lastIndexOf(File.separator));
            }
            return null;
        }
}
