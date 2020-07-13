package com.modiejun.cloudfiles.FileSharing;

import com.modiejun.cloudfiles.FileSharing.Model.SharedLink;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("sharedLinkRepository")
public interface SharedLinkRepository extends CrudRepository<SharedLink,String> {
    Optional<List<SharedLink>> findAllByCurrentlyValid(boolean currentlyValid);
    Optional<SharedLink> findByShareCode(String code);
    Optional<SharedLink> findByFileToBeAccessed(String fileName);
    Optional<List<SharedLink>> findAllBySharedBy(String shareBy);
}
