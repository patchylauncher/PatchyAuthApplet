package com.eziosoft.patchyauthapplet;

import com.eziosoft.patchyauthapplet.LoginTypes.MicrosoftLogin;
import com.eziosoft.patchyauthapplet.LoginTypes.MinecraftLogin;
import com.eziosoft.patchyauthapplet.LoginTypes.XboxLogin;
import com.eziosoft.patchyauthapplet.objects.microsoft.MSAuthUrl;
import com.eziosoft.patchyauthapplet.objects.microsoft.MSCodeRedeemResponse;
import com.eziosoft.patchyauthapplet.objects.microsoft.MSCodeRequest;
import com.eziosoft.patchyauthapplet.objects.minecraft.MinecraftProfile;
import com.eziosoft.patchyauthapplet.objects.xbox.XboxToken;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        System.out.println("PatchyAuthApplet v0.1");
        // make a new msurl
        MSAuthUrl msurl = new MSAuthUrl();
        // get the url
        String url = msurl.buldURL();
        // make the user visit the url
        System.out.println("Go to this url in your browser and then paste in the code:");
        System.out.println(url);
        // read the console for the code
        // setup a scanner to do so
        Scanner in = new Scanner(System.in);
        String code = in.nextLine();
        if (code.isEmpty()){
            System.err.println("Please enter a code next time!");
            System.exit(-1);
        }
        // next, do the ms request
        MSCodeRequest req = new MSCodeRequest(code, MSAuthUrl.default_redirect, MSAuthUrl.mclaunchid);
        req.SetGrantAuth();
        MSCodeRedeemResponse dank = MicrosoftLogin.GetOAuthToken(req);
        // see if we got anything
        if (dank == null){
            throw new IOException("Returned response is null!");
        }
        System.out.println("MSA Access token: " + dank.getAccess_token());
        System.out.println("MSA Refresh Token " + dank.getRefresh_token());
        // then, get xbox token
        XboxToken dank2 = XboxLogin.GetXboxTonken(dank);
        System.out.println("Xbox Live Token: " + dank2.getToken());
        // get xsts token
        XboxToken xsts = XboxLogin.GetXSTSToken(dank2.getToken());
        System.out.println("XSTS Token: " + xsts.getToken());
        System.out.println("XBL3.0 x=");
        System.out.println(xsts.getCombinedToken());
        // get minecraft token
        String mcaccess = MinecraftLogin.getMinecraftAccessToken(xsts);
        System.out.println("Minecraft Access Token: " + mcaccess);
        // get minecraft profile
        MinecraftProfile mcp = MinecraftLogin.getMinecraftProfile(mcaccess);
        System.out.println("MC UUID: " +mcp.getId());
        System.out.println("MC Name: " + mcp.getName());
    }
}