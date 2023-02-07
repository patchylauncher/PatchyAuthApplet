package com.eziosoft.patchyauthapplet.objects.xbox;

public class XSTSAuthProperties {

    private String SandboxId;
    private String[] UserTokens;

    public XSTSAuthProperties(String sandbox, String[] beans){
        this.SandboxId = sandbox;
        this.UserTokens = beans;
    }

    public String getSandboxId() {
        return SandboxId;
    }

    public String[] getUserTokens() {
        return UserTokens;
    }
}
