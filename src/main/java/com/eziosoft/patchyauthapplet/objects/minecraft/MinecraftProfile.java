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
}
