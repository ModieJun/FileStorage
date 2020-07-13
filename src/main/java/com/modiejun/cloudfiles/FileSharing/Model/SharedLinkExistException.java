package com.modiejun.cloudfiles.FileSharing.Model;

public class SharedLinkExistException  extends SharedLinkException{
    public SharedLinkExistException(String s) {
        super(s);
    }

    public SharedLinkExistException(String s, Throwable t) {
        super(s, t);
    }
}
