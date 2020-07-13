package com.modiejun.cloudfiles.FileSharing.Model;

public class SharedLinkException extends RuntimeException {
    public SharedLinkException(String s) {
        super(s);
    }

    public SharedLinkException(String s, Throwable t) {
        super(s,t);
    }
}
