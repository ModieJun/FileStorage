package com.modiejun.cloudfiles.Security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.modiejun.cloudfiles.Security.ApplicationUserPermission.*;

public enum ApplicationUserRoles {
    ADMIN(Sets.newHashSet(FILE_READ,FILE_WRITE,FILE_DOWNLOAD,FILE_DELETE)),
    NORMAL_USER(Sets.newHashSet(FILE_READ,FILE_WRITE,FILE_DOWNLOAD,FILE_DELETE)),
    VISITOR(Sets.newHashSet(FILE_READ,FILE_DOWNLOAD));

    private final Set<ApplicationUserPermission> permissionSet;

    ApplicationUserRoles(Set<ApplicationUserPermission> permissionSet) {
        this.permissionSet = permissionSet;
    }

    public Set<ApplicationUserPermission> getPermissionSet() {
        return permissionSet;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthoritySet(){
        Set<SimpleGrantedAuthority> permissions= getPermissionSet().stream().map(appicationUserPermission ->
                new SimpleGrantedAuthority(appicationUserPermission.getPermission())
        ).collect(Collectors.toSet());

        permissions.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return  permissions;
    }
}
