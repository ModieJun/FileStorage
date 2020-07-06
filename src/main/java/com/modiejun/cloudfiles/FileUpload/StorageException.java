package com.modiejun.cloudfiles.FileUpload;

public class StorageException  extends RuntimeException{
    public StorageException(String s) {
        super(s);
    }
    public StorageException(String s,Throwable t) {
        super(s,t);
    }

}
