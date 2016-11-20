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
import com.example.pranay.nutrihome.fatsecret.CommonMethod;
import com.example.pranay.nutrihome.fatsecret.FatSecretCommons;
import com.example.pranay.nutrihome.fatsecret.MethodParam;
import com.example.pranay.nutrihome.fatsecret.OAuthRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pranay on 17/9/16.
 */
public class Profile {

    private String  oAuthToken,
                    oAuthSecret;

    private String userId;

    public String  weightMeasure,
                    heightMeasure,
                    lastWeightKG,
                    lastWeightDate,
                    lastWeightComment,
                    goalWeightKG,
                    heightInCM;


    public boolean getUserInformation(MethodParam...params)
    {
        GetMethod method = new GetMethod(OAuthConstants.OAuthProto.O_AUTH_PROTO_VER1, this);
        ArrayList<Profile> result = method.parse(method.sendRequest(params));
        if (result != null)
            return true;

        return false;
    }

    private static Profile getProfile(String userId, MethodParam...params)
    {
        GetAuthMethod method = new GetAuthMethod(OAuthConstants.OAuthProto.O_AUTH_PROTO_VER1, userId);
        ArrayList<Profile> result = method.parse(method.sendRequest(params));
        if (result !=null)
            return result.size() == 1? result.get(0) : null;

        return  null;
    }

    private static Profile _createProfile(String userId, MethodParam...params)
    {
        CreateMethod method = new CreateMethod(OAuthConstants.OAuthProto.O_AUTH_PROTO_VER1, userId);
        ArrayList<Profile> result = method.parse(method.sendRequest(params));
        if (result !=null)
            return result.size() == 1? result.get(0) : null;

        return  null;
    }



    private static Profile getOrCreate(String method,
                                String userId, MethodParam...params
                                )
    {
        if (method.equals(ProfileConstants.METHOD_PROFILE_GET_AUTH))
            return getProfile(userId, params);

        else if (method.equals(ProfileConstants.METHOD_PROFILE_CREATE))
            return _createProfile(userId, params);

        return  null;
    }

    public static Profile getProfileFromServer(String userId,
                                               MethodParam...params
                                            )
    {
        return getOrCreate(ProfileConstants.METHOD_PROFILE_GET_AUTH, userId,params);
    }

    public static Profile createProfile(String userId,
                                        MethodParam...params
                                        )
    {
        return getOrCreate(ProfileConstants.METHOD_PROFILE_CREATE, userId, params);
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

        sb.append("Weight Measure: " + weightMeasure +"\n");
        sb.append("Height Measure: " + heightMeasure +"\n");
        sb.append("Last Weight in Kg: " + lastWeightKG +"\n");
        sb.append("Last Weight Date: " + lastWeightDate +"\n");
        sb.append("Last Weight Comment: " + lastWeightComment + "\n");
        sb.append("Goal Weight: " + goalWeightKG + "\n");
        sb.append("Height in cm: " + heightInCM +"\n");

        return sb.toString();
    }
}