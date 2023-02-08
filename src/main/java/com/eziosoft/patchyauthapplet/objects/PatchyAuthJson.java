package com.eziosoft.patchyauthapplet.objects;

public class PatchyAuthJson {
    private String accesstoken;
    private String uuid;
    private String playername;

    public PatchyAuthJson(String accesstoken, String uuid, String playername){
        this.accesstoken = accesstoken;
        this.playername = playername;
        this.uuid = uuid;
    }
}
