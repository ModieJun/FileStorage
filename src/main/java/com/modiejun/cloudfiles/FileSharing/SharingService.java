package com.modiejun.cloudfiles.FileSharing;

import com.modiejun.cloudfiles.FileSharing.Model.SharedLink;

import java.util.List;
import java.util.Optional;

public interface SharingService {
    SharedLink fetchFilePathForSharing(String code);

    Iterable<SharedLink> fetchAllLinks();

    Optional<List<SharedLink>> fetchAllValidLinks();

    void inValidateLink(String fileName);

    String createSharableLink(String username,String fileName, String filePath);

    List<SharedLink> getListOfSharedLinks(String username);

    String  encode(String value);
}
