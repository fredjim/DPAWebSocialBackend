package com.infsis.socialpagebackend.social_networks.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class FacebookConfigRequestDTO {

    @NotBlank(message = "page_id es obligatorio")
    @Size(max = 50)
    private String page_id;

    @NotBlank(message = "access_token es obligatorio")
    private String access_token;

    private boolean enabled = true;

    public String getPage_id() { return page_id; }
    public void setPage_id(String page_id) { this.page_id = page_id; }

    public String getAccess_token() { return access_token; }
    public void setAccess_token(String access_token) { this.access_token = access_token; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
