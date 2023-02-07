package com.eziosoft.patchyauthapplet.objects.microsoft;

import jdk.nashorn.internal.runtime.ECMAException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MSAuthUrl {
    // constant, default values
    public static String mclaunchid = "00000000402B5328";
    private static String defaultAuthorizeURl = "https://login.live.com/oauth20_authorize.srf";
    private static String default_scopes = "XboxLive.signin XboxLive.offline_access";
    public static String default_redirect = "https://login.microsoftonline.com/common/oauth2/nativeclient";


    private String clientid;
    private String scopes;
    private String redirect_url;
    private String responsetype = "code";
    private String url;

    public MSAuthUrl(String client, String scopes, String redirect_url){
        this.clientid = client;
        this.scopes = scopes;
        this.redirect_url = redirect_url;
    }

    public MSAuthUrl(String redirect_url){
        this.clientid = mclaunchid;
        this.url = defaultAuthorizeURl;
        this.redirect_url = redirect_url;
        this.scopes = default_scopes;
    }

    public MSAuthUrl(){
        this.clientid = mclaunchid;
        this.url = defaultAuthorizeURl;
        this.redirect_url = default_redirect;
        this.scopes = default_scopes;
    }

    public String buldURL() {
        try {
            StringBuilder build = new StringBuilder();
            // first, apparen the url to the created url
            build.append(this.url);
            // then, append client id information
            build.append("?client_id=").append(this.clientid);
            //  next, scope information
            build.append("&scope=").append(URLEncoder.encode(this.scopes, "UTF-8"));
            //  next, redirect information
            build.append("&redirect_uri=").append(this.redirect_url);
            // response types
            build.append("&response_type=").append(this.responsetype);
            // return this
            return build.toString();
        } catch (UnsupportedEncodingException e){
            // this should litterally never happen lmao
            e.printStackTrace();
        }
        return null;
    }

    public String getClientid(){
        return this.clientid;
    }
}
