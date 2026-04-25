package com.infsis.socialpagebackend.security;

import com.infsis.socialpagebackend.multitenant.TenantContext;
import org.springframework.stereotype.Component;

@Component
public class AuthContext {

    public String getInstitutionId() {
        return TenantContext.getCurrentTenant();
    }

    public boolean isRoot() {
        return TenantContext.getCurrentTenant() == null;
    }
}
