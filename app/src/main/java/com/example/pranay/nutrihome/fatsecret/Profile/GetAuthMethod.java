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
import com.example.pranay.nutrihome.fatsecret.CommonMethod;
import com.example.pranay.nutrihome.fatsecret.FatSecretCommons;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by pranay on 20/11/16.
 */
public class GetAuthMethod extends CommonMethod<Profile>{


    private String userID;
    public GetAuthMethod(OAuthConstants.OAuthProto proto, String userID)
    {
        super(proto);
        this.userID = userID;
        addParameter(FatSecretCommons.METHOD, ProfileConstants.METHOD_PROFILE_GET_AUTH);
        addParameter(ProfileConstants.USER_ID,userID);
    }

    @Override
    public ArrayList<Profile> parse(String jsonInput) {

        ArrayList<Profile> result = new ArrayList<>();
        JSONObject fromServer = null;
        try {
            fromServer = new JSONObject(jsonInput);

            String auth_token = fromServer.getJSONObject(ProfileConstants.JSON_OBJECT_NAME).
                    getString(ProfileConstants.AUTH_TOKEN);

            String auth_secret = fromServer.getJSONObject(ProfileConstants.JSON_OBJECT_NAME).
                    getString(ProfileConstants.AUTH_SECRET);

            result.add(new Profile(auth_token, auth_secret, userID));

        } catch (JSONException e) {
            result.clear();
            result = null;
            try {
                JSONObject error = fromServer.getJSONObject("error");
                AppLogger.getInstance().error(error.getString("code") + ": " +
                        error.getString("message"));
            } catch (JSONException e1) {
                AppLogger.getInstance().error(e1.getMessage());
            }
        }
        return result;
    }
}
