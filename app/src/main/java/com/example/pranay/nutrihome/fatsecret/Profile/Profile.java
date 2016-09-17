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

package com.example.pranay.nutrihome.fatsecret.Profile;

import com.example.pranay.nutrihome.AppLogger;
import com.example.pranay.nutrihome.OAuthCommon.OAuthConstants;
import com.example.pranay.nutrihome.R;
import com.example.pranay.nutrihome.fatsecret.FatSecretCommons;
import com.example.pranay.nutrihome.fatsecret.OAuthRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pranay on 17/9/16.
 */
public class Profile {

    private String  oAuthToken,
                    oAuthSecret;

    private String userId;



    private static Profile getOrCreate(String method,
                                String userId,
                                String consumerKey, String sharedKey,
                                String nonce, String url,
                                OAuthConstants.OAuthProto proto
                                )
    {
        OAuthRequest request = new OAuthRequest(consumerKey,sharedKey, nonce, url, proto);
        request.addParameter(FatSecretCommons.FORMAT, FatSecretCommons.FORMAT_JSON);
        request.addParameter(ProfileConstants.USER_ID, userId);
        request.addParameter(FatSecretCommons.METHOD, method);
        JSONObject result = null;
        try {
            result = new JSONObject(request.sendRequest(true, true));

            String auth_token = result.getJSONObject(ProfileConstants.JSON_OBJECT_NAME).
                    getString(ProfileConstants.AUTH_TOKEN);

            String auth_secret = result.getJSONObject(ProfileConstants.JSON_OBJECT_NAME).
                    getString(ProfileConstants.AUTH_SECRET);

            return new Profile(auth_token, auth_secret, userId);
        } catch (JSONException e) {
            try {
                JSONObject error = result.getJSONObject("error");
                AppLogger.getInstance().error(error.getString("code") + ": " +
                                error.getString("message"));
            } catch (JSONException e1) {
                AppLogger.getInstance().error(e1.getMessage());
            }
        }
        return null;
    }

    public static Profile getProfileFromServer(String userId,
                                               String consumerKey, String sharedKey,
                                               String nonce, String url,
                                               OAuthConstants.OAuthProto proto
                                            )
    {
        return getOrCreate(ProfileConstants.METHOD_PROFILE_GET_AUTH, userId,
                                consumerKey, sharedKey, nonce, url,proto);
    }
    public static Profile createProfile(String userId,
                                        String consumerKey, String sharedKey,
                                        String nonce, String url,
                                        OAuthConstants.OAuthProto proto
                                        )
    {
        return getOrCreate(ProfileConstants.METHOD_PROFILE_CREATE, userId,
                consumerKey, sharedKey, nonce, url,proto);
    }

    public Profile(String token, String secret, String userId)
    {
        oAuthSecret = secret;
        oAuthToken = token;
        this.userId = userId;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getoAuthToken()
    {
        return oAuthToken;
    }

    public String getoAuthSecret()
    {
        return oAuthSecret;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(ProfileConstants.AUTH_SECRET +": " + oAuthSecret +"\n");
        sb.append(ProfileConstants.AUTH_TOKEN + ": " + oAuthToken + "\n");
        sb.append(ProfileConstants.USER_ID + ": " + userId + "\n");

        return sb.toString();
    }
}
