package com.eziosoft.patchyauthapplet;

import com.eziosoft.patchyauthapplet.objects.PatchyAuthJson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.SystemUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Utilities {

    /**
     * routine for easily POSTing json data to a webpage
     * @param url url to post too
     * @param json json data to psost
     * @return returned web content as a string
     * @throws IOException if the web server didnt like your request
     */
    public static String doJsonHttpPost(String url, String json) throws IOException {
        // first, make the StringEntity
        StringEntity cont = new StringEntity(
                json,
                ContentType.APPLICATION_JSON
        );
        // setup the post
        HttpPost psot = new HttpPost(url);
        psot.setEntity(cont);
        // do the request
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(psot);
        // check the response code
        if (response.getCode() != HttpStatus.SC_OK){
            int code = response.getCode();
            response.close();
            client.close();
            throw new IOException("Web Server replied with status code " + code);
        }
        // otherwise, jam it it into a string
        String beans;
        try {
            beans = EntityUtils.toString(response.getEntity());
        } catch (ParseException e) {
            response.close();
            client.close();
            throw new IOException("Something went wrong during string conversion!", e);
        }
        response.close();
        client.close();
        return beans;
    }

    /**
     * cheap n dirty hack to open a url in the yser's default browser
     * only works on Windows, Linux and Mac OS
     * @param url url to open
     * @throws IOException oh no, your system is fucked (probably)
     */
    public static void OpenInDefaultBrowser(String url) throws IOException{
        // basically, just do the different things you need to do for xyz platform.
        if (SystemUtils.IS_OS_LINUX){
            Runtime.getRuntime().exec("xdg-open " + url);
        } else if (SystemUtils.IS_OS_WINDOWS){
            Runtime.getRuntime().exec("start " + url);
        } else if (SystemUtils.IS_OS_MAC){
            Runtime.getRuntime().exec("open " + url);
        } else {
            System.out.println("Hmm, we don't know how to open your default browser!");
            System.out.println("Please manually open this link in your browser of choice:");
            System.out.println(url);
        }
    }

    /**
     * Produces a file importable by PatchyLauncher for authentication
     * @param auth the authentication information to dump to a file
     */
    public static String createAuthenticationFile(PatchyAuthJson auth, String password){
        // get a gson with pretty printing enabled
        Gson g = new GsonBuilder().setPrettyPrinting().create();
        // get the object as json text
        String json = g.toJson(auth);
        // encrypt the text with the password
        String enc;
        try {
            enc = EncryptTextWithPassword(json, password);
        } catch (Exception e){
            System.err.println("Error while trying to encrypt text!");
            e.printStackTrace();
            System.err.println("Continuing by storing the file in plaintext!");
            enc = json;
        }
        // open a new file
        File authfile = new File("authenticate.json");
        // write to it
        try {
            FileWriter filew = new FileWriter(authfile);
            BufferedWriter writer = new BufferedWriter(filew);
            writer.write(enc);
            // clean up
            writer.close();
            filew.close();
        } catch (IOException e){
            System.err.println("Error while trying to save file!");
            e.printStackTrace();
            System.err.println("The raw data to save yourself is:");
            System.out.println(enc);
            return "error";
        }
        // get the path of the file
        // exit
        return authfile.getAbsolutePath();
    }

    /**
     * encrypts a string with a user provided password
     * @param text the text to encrypt
     * @param password password to encrypt the  text with
     * @return encrypted text
     * @throws Exception oh, no, something broke
     */
    private static String EncryptTextWithPassword(String text, String password) throws Exception{
        // setup the encryption
        SecretKey key = new SecretKeySpec(password.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        // encrypt it
        byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
        // return it
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * function to easily make authorized git requests
     * @param token authorization token to go into the header
     * @param url url to make get request too
     * @return string of returned page
     * @throws IOException if the wen server didnt like your request for some reason
     */
    public static String doAuthorizedGet(String token, String url) throws IOException {
        // setup the authorization headers
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        // setup to do the request
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        for (Map.Entry<String, String> hheader : headers.entrySet()){
            get.addHeader(hheader.getKey(), hheader.getValue());
        }
        // do the request
        CloseableHttpResponse response = client.execute(get);
        // check the status code
        if (response.getCode() != HttpStatus.SC_OK){
            int code = response.getCode();
            response.close();
            client.close();
            throw new IOException("Web server replied with status code " + code);
        }
        // convert to string
        String beans;
        try {
            beans = EntityUtils.toString(response.getEntity());
        } catch (ParseException e) {
            response.close();
            client.close();
            throw new IOException("Something went wrong during string conversion!", e);
        }
        // clean up and return
        response.close();
        client.close();
        return beans;
    }
}
