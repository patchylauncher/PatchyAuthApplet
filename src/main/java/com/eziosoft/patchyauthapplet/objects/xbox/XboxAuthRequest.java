package com.eziosoft.patchyauthapplet.objects.xbox;

import java.util.HashMap;

public class XboxAuthRequest {
    private HashMap<String, String> Properties;
    private String RelyingParty;
    private String TokenType;

    public XboxAuthRequest(HashMap<String, String> map, String party, String tokenType){
        this.Properties = map;
        this.RelyingParty = party;
        this.TokenType = tokenType;
    }
}
