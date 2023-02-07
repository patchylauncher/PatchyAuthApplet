package com.eziosoft.patchyauthapplet.objects.minecraft;

public class MinecraftAuthResponse {

    private String username;
    private String[] roles;
    private String access_token;
    private String token_type;
    private int expires_in;

    public String getToken_type() {
        return token_type;
    }

    public String getAccess_token() {
        return access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public String getUsername() {
        return username;
    }

    public String[] getRoles() {
        return roles;
    }
}
