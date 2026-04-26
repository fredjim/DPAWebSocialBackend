package com.infsis.socialpagebackend.social_networks.dtos;

public class FacebookConfigResponseDTO {

    private String uuid;
    private String institution_id;
    private String page_id;
    private boolean configured;
    private boolean enabled;
    private String token_hint;

    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }

    public String getInstitution_id() { return institution_id; }
    public void setInstitution_id(String institution_id) { this.institution_id = institution_id; }

    public String getPage_id() { return page_id; }
    public void setPage_id(String page_id) { this.page_id = page_id; }

    public boolean isConfigured() { return configured; }
    public void setConfigured(boolean configured) { this.configured = configured; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public String getToken_hint() { return token_hint; }
    public void setToken_hint(String token_hint) { this.token_hint = token_hint; }
}
