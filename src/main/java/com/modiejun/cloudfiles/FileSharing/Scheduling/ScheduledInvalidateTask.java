package com.modiejun.cloudfiles.FileSharing.Scheduling;

import com.modiejun.cloudfiles.FileSharing.Model.SharedLink;
import com.modiejun.cloudfiles.FileSharing.SharingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScheduledInvalidateTask {
    private static final Logger log = LoggerFactory.getLogger(ScheduledInvalidateTask.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private final SharingService sharingService;

    @Autowired
    public ScheduledInvalidateTask(SharingService sharingService) {
        this.sharingService = sharingService;
    }

    @Scheduled(fixedRate = 5000)
    public void invalidateExpiredLinks() {
        sharingService.fetchAllValidLinks().ifPresent(this::checkValidity);
    }

    private void checkValidity(List<SharedLink> links) {
        Calendar current =Calendar.getInstance();
        Calendar expireTime = Calendar.getInstance();
        current.setTime(new Date());
        links.forEach(sharedLink -> {
            expireTime.setTime(sharedLink.getCreated());
            expireTime.add(Calendar.SECOND,sharedLink.getValidDuration());
            System.out.println("Current time: " + current.getTime());
            System.out.println("Expire time: " + expireTime.getTime());
            if (current.getTime().after(expireTime.getTime())) { // if the current time is passed the expire
                sharingService.inValidateLink(sharedLink.getFileToBeAccessed());
            }
        });
    }
}
