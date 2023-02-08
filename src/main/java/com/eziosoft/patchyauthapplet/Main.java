package com.eziosoft.patchyauthapplet;

import com.eziosoft.patchyauthapplet.LoginTypes.MicrosoftLogin;
import com.eziosoft.patchyauthapplet.LoginTypes.MinecraftLogin;
import com.eziosoft.patchyauthapplet.LoginTypes.XboxLogin;
import com.eziosoft.patchyauthapplet.objects.PatchyAuthJson;
import com.eziosoft.patchyauthapplet.objects.microsoft.MSAuthUrl;
import com.eziosoft.patchyauthapplet.objects.microsoft.MSCodeRedeemResponse;
import com.eziosoft.patchyauthapplet.objects.microsoft.MSCodeRequest;
import com.eziosoft.patchyauthapplet.objects.minecraft.MinecraftProfile;
import com.eziosoft.patchyauthapplet.objects.xbox.XboxToken;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("PatchyAuthApplet v0.1");
        if (args.length > 0){
            // run the new TUI
            runUI(args);
            return;
        }
        // TODO: all of this shit
        System.out.println("wow! this is unfinished");
        System.out.println("so have console mode");
        runUI(args);
    }

    private static void runUI(String[] args) throws IOException{
        System.out.println("Console mode started");
        // first, build the main MSA url
        MSAuthUrl msurl = new MSAuthUrl();
        String url = msurl.buldURL();
        // open the url
        System.out.println("The Microsoft account sign-in screen will be automatically opened.");
        System.out.println("Please sign in to the account that owns a copy of Minecraft: Java Edition.");
        System.out.println("Once you sign in, you will be brought to a blank page. Copy everything after the \"?code=\" and then paste it into the console");
        try {
            // opens in default browser for a handful of supported operating systems
            Utilities.OpenInDefaultBrowser(url);
        } catch (IOException e){
            System.err.println("Error while attempting to open in default browser!");
            e.printStackTrace();
            System.out.println("You will have to manually open the following url:");
            System.out.println(url);
        }
        // wait for the code to be entered
        Scanner in = new Scanner(System.in);
        String frack = in.nextLine();
        while (frack.trim().isEmpty()){
            System.out.println("Please paste the code into the console and press enter");
            frack = in.nextLine();
        }
        // we need to also get a password to encrypt the output file with
        System.out.println("This program outputs your Minecraft access token (along with other data) into a text file.");
        System.out.println("You can import this file into PatchyLauncher to play online");
        System.out.println("However, to provide some basic security, please provide a password to encrypt the resulting file with");
        System.out.println("The key has to be 16 characters in length, and cannot contain spaces");
        String encpass = in.nextLine();
        while (encpass.trim().length() < 16){
            System.out.println("Please enter a password that is at least 16 characters long to encrypt the authentication file with");
            encpass = in.nextLine();
        }
        // once we have the code, get rid of the scanner
        in.close();
        // get rid of spaces too
        encpass = encpass.trim();
        // continue the authentication chain
        System.out.println("Now authenticating with Microsoft...");
        MSCodeRequest msreq = new MSCodeRequest(frack, MSAuthUrl.default_redirect, MSAuthUrl.mclaunchid);
        msreq.SetGrantAuth();
        MSCodeRedeemResponse coderedeem = MicrosoftLogin.GetOAuthToken(msreq);
        System.out.println("Now authenticating with Xbox Live...");
        XboxToken xboxlive = XboxLogin.GetXboxTonken(coderedeem);
        System.out.println("Now authenticating with XSTS...");
        XboxToken xststoken = XboxLogin.GetXSTSToken(xboxlive);
        System.out.println("Now authenticating with Minecraft...");
        String mcaccess = MinecraftLogin.getMinecraftAccessToken(xststoken);
        // TODO: check if they own the game lmao
        MinecraftProfile profile = MinecraftLogin.getMinecraftProfile(mcaccess);
        System.out.println("Logged into minecraft!");
        System.out.println("Profile name: " + profile.getName());
        // make the a json object to hold it all
        PatchyAuthJson object = new PatchyAuthJson(mcaccess, profile.getFixedUUID(), profile.getName());
        // write the authentication file
        String output = Utilities.createAuthenticationFile(object, encpass);
        if (output.equals("error")){
            System.err.println("There was an error saving the file");
            System.err.println("Please consult the log for more information");
        } else {
            System.out.println("The file was written to " + output);
        }
    }
}