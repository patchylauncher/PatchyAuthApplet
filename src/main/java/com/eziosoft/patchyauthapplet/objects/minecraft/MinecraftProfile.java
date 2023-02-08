package com.eziosoft.patchyauthapplet.objects.minecraft;

public class MinecraftProfile {
    private String id;
    private String name;
    private MinecraftSkinItem[] skins;
    // TODO: capes i guess


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MinecraftSkinItem[] getSkins() {
        return skins;
    }

    public String getFixedUUID(){
        // the format is 8-4-4-4-12
        // get first 8 chars
        String seg1 = this.id.substring(0, 8);
        // get next 4 chars
        String seg2 = this.id.substring(5, 9);
        // next 4
        String seg3 = this.id.substring(10, 14);
        // next 4
        String seg4 = this.id.substring(15, 19);
        // last segment
        String seg5 = this.id.substring(20);
        // add them all together
        return seg1 + "-" + seg2 + "-" + seg3 + "-" + seg4 + "-" + seg5;
    }
}
