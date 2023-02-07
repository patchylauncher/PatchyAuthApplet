package com.eziosoft.patchyauthapplet.objects.microsoft;

public class MSCodeRequest {
    private String clientid;
    private String authcode;
    private String granttype;
    private String redirect;


    public MSCodeRequest(String code, String redirect, String clientid){
        this.authcode = code;
        this.redirect = redirect;
        this.granttype = "oof";
        this.clientid = clientid;
    }

    public void SetGrantAuth(){
        this.granttype = "authorization_code";
    }

    public String getAuthcode() {
        return authcode;
    }

    public String getClientid() {
        return clientid;
    }

    public String getGranttype() {
        return granttype;
    }

    public String getRedirect() {
        return redirect;
    }
}
