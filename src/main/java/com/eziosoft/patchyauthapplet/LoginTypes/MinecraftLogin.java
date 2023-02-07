package com.eziosoft.patchyauthapplet.LoginTypes;

import com.eziosoft.patchyauthapplet.Utilities;
import com.eziosoft.patchyauthapplet.objects.minecraft.MinecraftAuthResponse;
import com.eziosoft.patchyauthapplet.objects.minecraft.MinecraftProfile;
import com.eziosoft.patchyauthapplet.objects.xbox.XboxToken;
import com.google.gson.Gson;

import java.io.IOException;

public class MinecraftLogin {

    private static Gson g = new Gson();

    // sub classes for smaller things that basically dont matter lmao
    static class MinecraftXboxAuthRequest {
        private String identityToken;

        public MinecraftXboxAuthRequest(String token){
            this.identityToken = token;
        }
    }

    // default shit
    public static String default_minecraft_xbox = "https://api.minecraftservices.com/authentication/login_with_xbox";
    public static String default_minecraft_profile = "https://api.minecraftservices.com/minecraft/profile";

    public static String getMinecraftAccessToken(XboxToken token) throws IOException {
        // first make a authrequest object
        MinecraftXboxAuthRequest xboxauth = new MinecraftXboxAuthRequest("XBL3.0 x=" + token.getCombinedToken());
        // then do the request via json
        String json = Utilities.doJsonHttpPost(default_minecraft_xbox, g.toJson(xboxauth));
        // convert to minecraft response, and return just the access token
        return g.fromJson(json, MinecraftAuthResponse.class).getAccess_token();
    }

    public static MinecraftProfile getMinecraftProfile(String token) throws IOException {
        // its simple really
        // get the returned json
        String raw = Utilities.doAuthorizedGet(token, default_minecraft_profile);
        //  convert to json and return
        return g.fromJson(raw, MinecraftProfile.class);
    }

}
