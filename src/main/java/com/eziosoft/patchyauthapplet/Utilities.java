package com.eziosoft.patchyauthapplet;

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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Utilities {

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
