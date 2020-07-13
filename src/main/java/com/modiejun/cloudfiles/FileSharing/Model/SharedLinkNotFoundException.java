package com.modiejun.cloudfiles.FileSharing.Model;

public class SharedLinkNotFoundException extends SharedLinkException{
    public SharedLinkNotFoundException(String s) {
        super(s);
    }

    public SharedLinkNotFoundException(String s, Throwable t) {
        super(s, t);
    }
}
