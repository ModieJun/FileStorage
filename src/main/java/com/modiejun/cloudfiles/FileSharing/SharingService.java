package com.modiejun.cloudfiles.FileSharing;

import com.modiejun.cloudfiles.FileSharing.Model.SharedLink;

import java.util.List;

public interface SharingService {
    SharedLink fetchFilePathForSharing(String code);

    void inValidateLink(String fileName);

    String createSharableLink(String username,String fileName, String filePath);

    List<SharedLink> getListOfSharedLinks(String username);

    String  encode(String value);
}
