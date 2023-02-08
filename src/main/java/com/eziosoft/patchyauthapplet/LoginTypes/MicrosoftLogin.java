package com.eziosoft.patchyauthapplet.LoginTypes;

import com.eziosoft.patchyauthapplet.objects.microsoft.MSCodeRedeemResponse;
import com.eziosoft.patchyauthapplet.objects.microsoft.MSCodeRequest;
import com.google.gson.Gson;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MicrosoftLogin {

    // url to ask for tokens
    private static final String tokenurl = "https://login.live.com/oauth20_token.srf";


    /**
     * gets the required OAuth token for your microsoft account
     * @param request the data required to make the request
     * @return the response in parsed json form
     * @throws IOException oh no, the web server didnt like your request
     */
    public static MSCodeRedeemResponse GetOAuthToken(MSCodeRequest request) throws IOException {
        // setup the apache http client
        HttpPost httpPost = new HttpPost(tokenurl);
        // create form content
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // fill the array with bullshit
        params.add(new BasicNameValuePair("client_id", request.getClientid()));
        params.add(new BasicNameValuePair("code", request.getAuthcode()));
        params.add(new BasicNameValuePair("grant_type", request.getGranttype()));
        params.add(new BasicNameValuePair("redirect_uri", request.getRedirect()));
        // encode it and set as the entity
        httpPost.setEntity(new UrlEncodedFormEntity(params));
        // do the request
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(httpPost);
        if (response.getCode() != HttpStatus.SC_OK){
            int code = response.getCode();
            response.close();
            client.close();
            throw new IOException("Web Server responded with error code " + code);
        }
        String json;
        try {
            json = EntityUtils.toString(response.getEntity());
        } catch (ParseException e){
            // TODO: write more descriptive error message
            // for now, though, just rethrow it
            throw new IOException(e);
        }
        // close the response and the client
        response.close();
        client.close();
        // parse and return
        Gson g = new Gson();
        return g.fromJson(json, MSCodeRedeemResponse.class);
    }
}
