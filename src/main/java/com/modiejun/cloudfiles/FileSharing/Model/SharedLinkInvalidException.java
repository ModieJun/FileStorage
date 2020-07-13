package com.modiejun.cloudfiles.FileSharing.Model;

public class SharedLinkInvalidException  extends SharedLinkException{
    public SharedLinkInvalidException(String s) {
        super(s);
    }

    public SharedLinkInvalidException(String s, Throwable t) {
        super(s, t);
    }
}
