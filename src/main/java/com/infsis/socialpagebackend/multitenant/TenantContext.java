package com.infsis.socialpagebackend.multitenant;

/**
 * Almacena el tenant (institution uuid) del request actual en un ThreadLocal.
 * Debe limpiarse siempre en el finally del filtro que lo establece.
 */
public class TenantContext {

    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    public static void setCurrentTenant(String institutionId) {
        CURRENT_TENANT.set(institutionId);
    }

    public static String getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    public static boolean isRoot() {
        return CURRENT_TENANT.get() == null;
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }
}
