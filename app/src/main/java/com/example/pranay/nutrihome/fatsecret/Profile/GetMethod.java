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

import java.util.ArrayList;

/**
 * Created by pranay on 20/11/16.
 */
public class GetMethod extends CommonMethod<Profile> {

    private Profile m_profile;
    public GetMethod(OAuthConstants.OAuthProto proto, Profile profile) {
        super(proto);
        m_profile = profile;
        addParameter(FatSecretCommons.METHOD, ProfileConstants.METHOD_PROFILE_GET);
        addParameter(OAuthConstants.OAUTH_AUTH_TOKEN, m_profile.getoAuthToken());
        addParameter(OAuthConstants.OAUTH_ACCESS_KEY, m_profile.getoAuthSecret());
    }

    @Override
    public ArrayList<Profile> parse(String jsonInput) {

        /*
         * If everything is OK then this function returns
         * with a non-null arraylist object. However
         * that arrayList is meaning less since the Profile
         * item has already been set with the correct values.
         *
         * Therefore only check for non-null from this method
         * DO NOT try to get any items from the returned
         * ArrayList AT ALL.
         */
        ArrayList<Profile> result = new ArrayList<>();
        JSONObject fromServer = null;

        try {
            fromServer = new JSONObject(jsonInput);
            JSONObject profile = fromServer.getJSONObject(ProfileConstants.JSON_OBJECT_NAME);


            m_profile.weightMeasure = profile.optString(ProfileConstants.JSON_KEY_WEIGHT_MEASURE).
                    toLowerCase();

            m_profile.heightMeasure = profile.optString(ProfileConstants.JSON_KEY_HEIGHT_MEASURE).
                    toLowerCase();

            m_profile.lastWeightKG = profile.optString(ProfileConstants.JSON_KEY_LAST_WEIGHT_KG).
                    toLowerCase();

            m_profile.lastWeightDate = profile.optString(ProfileConstants.JSON_KEY_LAST_WEIGHT_DATE_INT).
                    toLowerCase();

            m_profile.lastWeightComment = profile.optString(ProfileConstants.JSON_KEY_LAST_WEIGHT_COMMENT).
                    toLowerCase();

            m_profile.goalWeightKG = profile.optString(ProfileConstants.JSON_KEY_GOAL_WEIGHT_KG).
                    toLowerCase();

            m_profile.heightInCM = profile.optString(ProfileConstants.JSON_KEY_HEIGHT_CM).toLowerCase();

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
