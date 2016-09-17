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

package com.example.pranay.nutrihome.OAuthCommon;

import android.net.Uri;
import android.provider.Settings;
import android.util.Base64;

import com.example.pranay.nutrihome.AppLogger;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

public abstract class OAuthManager {

    protected String oAuthConsumerKey;
    protected String oAuthNonce;
    protected String oAuthTimestamp;
    protected String oAuthSignatureMethod;
    protected String oAuthVersion;
    protected String oAuthSignature;

    protected String oAuthSharedKey;
    protected String oAuthAccessKey;
    protected String oAuthToken;


    public OAuthManager() {
        oAuthToken="";
        oAuthAccessKey="";
        oAuthConsumerKey="";
        oAuthNonce="";
        oAuthSharedKey="";
        oAuthSignature="";
        oAuthSignatureMethod="";
        oAuthTimestamp="";
        oAuthVersion="";
    }

    public void setoAuthVersion(String version)
    {
        oAuthVersion = version;
    }
    public String getoAuthVersion()
    {
        return oAuthVersion;
    }
    public String getoAuthTimestamp()
    {
        oAuthTimestamp = Long.toString(System.currentTimeMillis());
        return oAuthTimestamp;
    }

    public void setoAuthSignatureMethod(String signatureMethod)
    {
        oAuthSignatureMethod = signatureMethod;
    }
    public String getoAuthSignatureMethod()
    {
        return oAuthSignatureMethod;
    }


    public void setoAuthSignature(String signature)
    {
        oAuthSignature = signature;
    }
    public String getoAuthSignature()
    {
        return oAuthSignature;
    }

    public String getoAuthSharedKey()
    {
        return oAuthSharedKey;
    }
    public void setoAuthSharedKey(String sharedKey)
    {
        oAuthSharedKey = sharedKey;
    }
    public String getOAuthAccessKey()
    {
        return oAuthAccessKey;
    }
    public void setoAuthAccessKey(String accessKey)
    {
        oAuthAccessKey = accessKey;
    }

    public String getoAuthToken()
    {
        return oAuthToken;
    }
    public void setoAuthToken(String token)
    {
        oAuthToken = token;
    }

    public String getoAuthConsumerKey()
    {
        return oAuthConsumerKey;
    }
    public void setoauthConsumerKey(String consumerKey)
    {
        oAuthConsumerKey = consumerKey;
    }

    public String getoAuthNonce(String nonce)
    {
        oAuthNonce = generateNonce(nonce);
        return oAuthNonce;
    }



    /**
     * Returns the percent encoded String of content
     * that is utf-8 encoding.
     *
     * @param  content to be encoded
     * @return utf-8 encoded version of @param or null
     *
     * @author {Pranay Kr. Srivastava}
     */
    public String percentEncode(String content)
    {
        return Uri.encode(content);
    }

    /**
     * Returns the base64 encoded String of content.
     *
     * @param  content to be encoded
     * @return utf-8 encoded version of @param or null
     *
     * @author {Pranay Kr. Srivastava}
     */

    public String base64Encode(byte[] content)
    {
        return Base64.encodeToString(content, Base64.DEFAULT).trim();
    }

    public String generateTimeStamp()
    {
        return Long.toString(System.currentTimeMillis());
    }

    public abstract String generateToken(String content);
    public abstract String generateSignature(String content);
    protected abstract String generateNonce(String nonce);
}
