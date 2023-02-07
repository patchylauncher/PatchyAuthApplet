package com.eziosoft.patchyauthapplet.LoginTypes;

import com.eziosoft.patchyauthapplet.Utilities;
import com.eziosoft.patchyauthapplet.objects.microsoft.MSCodeRedeemResponse;
import com.eziosoft.patchyauthapplet.objects.xbox.*;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

public class XboxLogin {

    private static Gson g = new Gson();

    public static String default_relay = "http://auth.xboxlive.com";
    public static String default_sitename = "user.auth.xboxlive.com";
    public static String default_xbox_userauth = "https://user.auth.xboxlive.com/user/authenticate";
    public static String default_xsts_auth = "https://xsts.auth.xboxlive.com/xsts/authorize";
    public static String xsts_default_part = "rp://api.minecraftservices.com/";

    public static XboxToken GetXboxTonken(MSCodeRedeemResponse codez) throws IOException {
        // setup hashmap
        HashMap<String, String> props = new HashMap<>();
        props.put("AuthMethod", "RPS");
        props.put("SiteName", default_sitename);
        props.put("RpsTicket", "d=" + codez.getAccess_token());
        // make a new xbox auth request
        XboxAuthRequest xbox = new XboxAuthRequest(
                props,
                default_relay,
                "JWT"
        );
        // do the request
        String json = Utilities.doJsonHttpPost(default_xbox_userauth, g.toJson(xbox));
        // deserialize and return
        XboxAuthResponse auth = g.fromJson(json, XboxAuthResponse.class);
        return new XboxToken(auth.getToken(), auth.getDisplayClaims().getXui()[0].getUhs());
    }

    public static XboxToken GetXSTSToken(String xboxtoken) throws IOException {
        // first, setup the request enitiy, involves an xbox token like before
        XSTSAuthProperties props = new XSTSAuthProperties(
                "RETAIL",
                new String[]{ xboxtoken }
        );
        // now setup the request json
        XSTSAuthRequest auth = new XSTSAuthRequest("JWT", xsts_default_part, props);
        // do the request
        String json = Utilities.doJsonHttpPost(default_xsts_auth, g.toJson(auth));
        // return it
        XboxAuthResponse authrep = g.fromJson(json, XboxAuthResponse.class);
        return new XboxToken(authrep.getToken(), authrep.getDisplayClaims().getXui()[0].getUhs());
    }
}
