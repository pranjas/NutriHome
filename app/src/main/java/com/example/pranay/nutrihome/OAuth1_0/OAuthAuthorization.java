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

package com.example.pranay.nutrihome.OAuth1_0;

import android.util.Base64OutputStream;

import com.example.pranay.nutrihome.AppLogger;
import com.example.pranay.nutrihome.OAuthCommon.OAuthManager;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by pranay on 4/9/16.
 */
public class OAuthAuthorization extends OAuthManager {

    @Override
    public String generateToken(String content) {
        return null;
    }

    @Override
    public String generateSignature(String content) {
        String key = oAuthSharedKey + "&" + oAuthToken;
        String signatureMethod = getoAuthSignatureMethod();
        SecretKeySpec spec = new SecretKeySpec(key.getBytes(), signatureMethod);
        String signature = "";
        try {

            Mac mac = Mac.getInstance(signatureMethod);
            mac.init(spec);
            byte []macResult =mac.doFinal(content.getBytes());
            signature = percentEncode(base64Encode(macResult));
        } catch (NoSuchAlgorithmException e) {
            AppLogger.getInstance().error(e.getMessage());
        } catch (InvalidKeyException e) {
            AppLogger.getInstance().error(e.getMessage());
        }
        return signature;
    }

    @Override
    protected String generateNonce(String nonce) {
        Random rand = new Random();

        return String.valueOf(rand.nextLong()) + nonce;
    }
}
