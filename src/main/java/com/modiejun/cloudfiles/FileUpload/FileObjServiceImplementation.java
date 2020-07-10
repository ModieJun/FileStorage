package com.modiejun.cloudfiles.FileUpload;

import com.modiejun.cloudfiles.FileUpload.POJO.SavedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *  This service will store files and owner of files into a db
 *  This can be called to help with SEARCHING for specific File names and Immediately download them
 *
 */
@Service
public class FileObjServiceImplementation implements FileObjService {

    private final FileObjDAO fileRepository;

    @Autowired
    public FileObjServiceImplementation(@Qualifier("fileRepository") FileObjDAO fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public boolean saveFile(SavedFile file) {
        fileRepository.save(file);
        return true;
    }

    @Override
    public boolean deleteFile(Long id) {
        try {
            fileRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean updateFile(SavedFile file) {
        fileRepository.save(file);
        return true;
    }

    @Override
    public Optional<SavedFile> findFirstByFileName(String fileName) {
        return fileRepository.findFirstByFileName(fileName);
    }

    /*
        Searches the file by file name that contains the wildcard
     */
    @Override
    public Optional<List<SavedFile>> searchByFileName(String fileName) {
        return fileRepository.findAllByFileNameLike(fileName);
    }
}
