package com.eziosoft.patchyauthapplet.objects.xbox;

public class XboxToken {
    private String Token;
    private String UserHash;

    public XboxToken(String t, String uh){
        this.Token = t;
        this.UserHash = uh;
    }

    public String getToken() {
        return Token;
    }

    public String getUserHash() {
        return UserHash;
    }

    public String getCombinedToken(){
        return this.UserHash + ";" + this.Token;
    }
}
