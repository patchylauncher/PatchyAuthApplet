package com.eziosoft.patchyauthapplet.objects.microsoft;

public class MSCodeRedeemResponse {
    private String token_type;
    private int expires_in;
    private String scope;
    private String access_token;
    private String refresh_token;
    private String user_id;
    private String foci;


    public int getExpires_in() {
        return expires_in;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getFoci() {
        return foci;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public String getScope() {
        return scope;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getUser_id() {
        return user_id;
    }
}
