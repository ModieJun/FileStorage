package com.modiejun.cloudfiles.FileSharing;

import com.google.common.hash.Hashing;
import com.modiejun.cloudfiles.FileSharing.Model.SharedLink;
import com.modiejun.cloudfiles.FileSharing.Model.SharedLinkExistException;
import com.modiejun.cloudfiles.FileSharing.Model.SharedLinkInvalidException;
import com.modiejun.cloudfiles.FileSharing.Model.SharedLinkNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service("sharingService")
public class SharingServiceImplementation  implements SharingService{

    private final SharedLinkRepository linkRepository;

    @Autowired
    public SharingServiceImplementation(@Qualifier("sharedLinkRepository") SharedLinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Override
    public SharedLink fetchFilePathForSharing(String code) {
        SharedLink link= linkRepository.findByShareCode(code).orElseThrow(()->
                new SharedLinkNotFoundException("SharedLink Code doesnt exist: "+ code)
        );

        if (link.isCurrentlyValid()) {
            return link;
        }else{
            throw new SharedLinkInvalidException("SharedLink has been invalidated");
        }
    }

    @Override
    public Iterable<SharedLink> fetchAllLinks() {
        return  linkRepository.findAll();
    }

    @Override
    public Optional<List<SharedLink>> fetchAllValidLinks() {
        return linkRepository.findAllByCurrentlyValid(true);
    }

    @Override
    public void inValidateLink(String fileName) {
        linkRepository.findByFileToBeAccessed(fileName).ifPresent(sharedLink -> {
            sharedLink.setCurrentlyValid(false);
            linkRepository.save(sharedLink);
        });
    }

    @Override
    public String createSharableLink(String username,String fileName, String filePath) {
        String code = this.encode(fileName+username);
        if (linkRepository.findByShareCode(code).isPresent()){
            throw new SharedLinkExistException("Share link Code already exists [Code]: " + code);
        }

        SharedLink link= new SharedLink(code,username,fileName,filePath);
        linkRepository.save(link);
        return code;
    }

    @Override
    public List<SharedLink> getListOfSharedLinks(String username) {
        return null;
    }

    @Override
    public String encode(String value) {
        return Hashing.sha256().hashString(value, StandardCharsets.UTF_8)
                .toString();
    }
}
