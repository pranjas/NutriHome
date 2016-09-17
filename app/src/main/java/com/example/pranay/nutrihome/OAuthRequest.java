/*
 * Copyright (c) 2016.
 * Original Author[Case insensitive]: Pranay Kumar Srivastava <pranjas (at) gmail.com>
 *
 *  You are free to re-distribute this code, modify it and make derivatives of this work
 *  however under all such cases the Original Author and Copyright holder must be
 *  accredited. The original author reserves the right to modify this software at anytime however the above clauses would still be applicable.
 *
 *  This software may not be used commercially without prior written agreement with the Original  Author and the Original Author above CANNOT be changed under any circumstances.
 *
 *  There's absolutely NO WARRANTY WHATSOEVER for using this software.
 *
 */

package com.example.pranay.nutrihome;

import com.example.pranay.nutrihome.OAuth1_0.OAuthAuthorization;
import com.example.pranay.nutrihome.OAuthCommon.OAuthConstants;
import com.example.pranay.nutrihome.OAuthCommon.OAuthManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pranay on 4/9/16.
 */
public class OAuthRequest {

    private OAuthManager oAuthManager;
    private String nonce;
    private String requestType = "POST";
    private String url;
    private Map<String, String> parameters;

    private OAuthRequest(OAuthConstants.OAuthProto proto){
        switch (proto)
        {
            case O_AUTH_PROTO_VER1:
                oAuthManager = new OAuthAuthorization();
                break;

            default:
                oAuthManager = null;
        }
    }

    public OAuthRequest(String consumerKey, String sharedKey,
                        String nonce, String url,
                        OAuthConstants.OAuthProto proto)
    {
        this(proto);
        if(oAuthManager != null) {
            oAuthManager.setoauthConsumerKey(consumerKey);
            oAuthManager.setoAuthSharedKey(sharedKey);
            parameters = new HashMap<String, String>();
            this.nonce = nonce;
            this.url = url;
        }
    }

    public void setRequestType(String type)
    {
        if(type.toLowerCase().equals("get") ||
                type.toLowerCase().equals("post"))
        requestType = type.toUpperCase();
    }

    public OAuthManager getoAuthManager()
    {
        return oAuthManager;
    }

    public void addParameter(String param, String value)
    {
        parameters.put(param, value);
    }

    public void addBaseParameters()
    {
        parameters.put(OAuthConstants.OAUTH_CONSUMER_KEY,
                oAuthManager.getoAuthConsumerKey());

        parameters.put(OAuthConstants.OAUTH_NONCE,
                oAuthManager.getoAuthNonce(nonce));

        parameters.put(OAuthConstants.OAUTH_SIGNATURE_METHOD,
                OAuthConstants.OAUTH_HMAC_SIGNATURE_METHOD);

        oAuthManager.setoAuthSignatureMethod(OAuthConstants.OAUTH_HMAC_SIGNATURE_METHOD);

        parameters.put(OAuthConstants.OAUTH_TIMESTAMP,
                oAuthManager.getoAuthTimestamp());

        parameters.put(OAuthConstants.OAUTH_VERSION, "1.0");
    }


    public BufferedReader sendRequest(boolean addBaseParams, boolean clearParams)
    {
        try {
            HttpURLConnection connection = (HttpURLConnection)
                    (new URL(url).openConnection());
            connection.setRequestMethod(requestType);
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            connection.getOutputStream().write(generateFormDataToSend(addBaseParams).getBytes());

            if(clearParams)
                parameters.clear();

            return new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            AppLogger.getInstance().error(e.getMessage());
        }
        return null;
    }

    private String getNormalizedParameters()
    {
        int i = 0;

        String[] keys = parameters.keySet().toArray(new String[0]);

        /*
         * Don't know if this be done or not.
         */

        for(String s: keys) {
            keys[i++] = oAuthManager.percentEncode(s);
        }

        Arrays.sort(keys);

        StringBuilder forSigbase = new StringBuilder();

        for(String s : keys) {
            if(forSigbase.length() != 0)
                forSigbase.append("&");
            forSigbase.append(s
                    + "=" + oAuthManager.percentEncode(parameters.get(s)));
        }
        return forSigbase.toString();
    }

    private String generateSignatureBase()
    {
        StringBuilder sigBase = new StringBuilder();

        sigBase.append(requestType + "&");

        sigBase.append(oAuthManager.percentEncode(url));
        sigBase.append("&");
            /*
             * Sort parameters first then percent encode.
             */
        sigBase.append(oAuthManager.percentEncode(getNormalizedParameters()));
        return sigBase.toString();
    }

    private String generateFormDataToSend(boolean addBaseParams)
    {
        StringBuilder toSend = new StringBuilder();
        String oauthSignature;
        int i = 0;
        if(addBaseParams)
            addBaseParameters();

        oauthSignature = oAuthManager.generateSignature(generateSignatureBase());
        /*
         *addParameter(OAuthConstants.OAUTH_SIGNATURE, oauthSignature);
        */
        String []keys = parameters.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        for(String s : keys) {
            if(toSend.length() != 0)
                toSend.append("&");
            toSend.append(oAuthManager.percentEncode(s));
            toSend.append("=");
            toSend.append(oAuthManager.percentEncode(parameters.get(s)));
        }
        toSend.append("&" + oAuthManager.percentEncode(OAuthConstants.OAUTH_SIGNATURE));
        toSend.append("=" + oauthSignature);

        return toSend.toString();
    }
}
