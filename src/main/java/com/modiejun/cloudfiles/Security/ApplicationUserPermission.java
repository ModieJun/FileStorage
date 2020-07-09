package com.modiejun.cloudfiles.Security;

public enum ApplicationUserPermission {
    FILE_READ("file:read"),
    FILE_WRITE("file:write"),
    FILE_DOWNLOAD("file:download"),
    FILE_DELETE("file:delete");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
