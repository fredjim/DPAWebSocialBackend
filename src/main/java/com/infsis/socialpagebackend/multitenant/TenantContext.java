package com.infsis.socialpagebackend.multitenant;

/**
 * Almacena el tenant (institution uuid) del request actual en un ThreadLocal.
 * Debe limpiarse siempre en el finally del filtro que lo establece.
 */
public class TenantContext {

    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();
    // Refleja el claim "isRoot" del JWT del request actual (false si no hay JWT / no autenticado).
    // No se infiere de CURRENT_TENANT == null: un usuario ROOT puede tener un tenant resuelto
    // en el contexto (ej. header X-Tenant-Slug enviado por el frontend por el subdominio actual)
    // sin dejar de ser ROOT.
    private static final ThreadLocal<Boolean> CURRENT_IS_ROOT = new ThreadLocal<>();

    public static void setCurrentTenant(String institutionId) {
        CURRENT_TENANT.set(institutionId);
    }

    public static String getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    public static void setRootFlag(boolean isRoot) {
        CURRENT_IS_ROOT.set(isRoot);
    }

    public static boolean isRoot() {
        return Boolean.TRUE.equals(CURRENT_IS_ROOT.get());
    }

    public static void clear() {
        CURRENT_TENANT.remove();
        CURRENT_IS_ROOT.remove();
    }
}
