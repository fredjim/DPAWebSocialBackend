package com.infsis.socialpagebackend.multitenant;

import org.springframework.stereotype.Component;

@Component
public class TenantResolver {

    public String resolveOrThrow(String tenantSlug) {
        if (tenantSlug == null || tenantSlug.isBlank()) {
            throw new IllegalArgumentException("The X-Tenant-Slug' header is required");
        }
        String tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null) {
            throw new IllegalArgumentException("Institution not found for slug: " + tenantSlug);
        }
        return tenantId;
    }
}
