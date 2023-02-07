package com.eziosoft.patchyauthapplet.objects.xbox;

public class XSTSAuthRequest {
    private XSTSAuthProperties Properties;
    private String RelyingParty;
    private String TokenType;

    public XSTSAuthRequest(String tokentype, String party, XSTSAuthProperties prop){
        this.Properties = prop;
        this.RelyingParty = party;
        this.TokenType = tokentype;
    }
}
