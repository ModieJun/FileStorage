package com.modiejun.cloudfiles.FileUpload;

import org.springframework.context.annotation.Configuration;

@Configuration("storage")
public class StorageProperties {
    private String location = "upliad-dir";

    public String getLocation() {
        return  location;
    }

    public void setLocation(String location) {
        this.location=location;
    }
}
