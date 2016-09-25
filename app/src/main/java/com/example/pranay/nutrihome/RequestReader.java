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

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.example.pranay.nutrihome.OAuthCommon.OAuthConstants;
import com.example.pranay.nutrihome.fatsecret.FatSecretCommons;
import com.example.pranay.nutrihome.fatsecret.Foods.Food;
import com.example.pranay.nutrihome.fatsecret.Foods.FoodConstants;
import com.example.pranay.nutrihome.fatsecret.Foods.FoodInfo;
import com.example.pranay.nutrihome.fatsecret.Foods.MethodParam;
import com.example.pranay.nutrihome.fatsecret.Method;
import com.example.pranay.nutrihome.fatsecret.OAuthRequest;
import com.example.pranay.nutrihome.fatsecret.Profile.Profile;
import com.example.pranay.nutrihome.fatsecret.Profile.ProfileConstants;

import java.util.ArrayList;

/**
 * Created by pranay on 3/9/16.
 */
public class RequestReader extends AsyncTask
        <String, Integer, Long> {

    private AppCompatActivity appCompatActivity;
    public RequestReader(AppCompatActivity appCompatActivity)
    {
        this.appCompatActivity = appCompatActivity;
    }

    @Override
    protected void onPostExecute(Long result)
    {
    }
    @Override
    protected Long doInBackground(String... params) {
        try {
            Profile p = Profile.createProfile( "kumar.srivastava.pranay@gmail.com",
                    appCompatActivity.getResources().getString(R.string.consumerKey)
                    ,appCompatActivity.getResources().getString(R.string.sharedKey),
                    "wtf", appCompatActivity.getResources().getString(R.string.api_url),
                    OAuthConstants.OAuthProto.O_AUTH_PROTO_VER1
            );

            if (p == null)
                p = Profile.getProfileFromServer("kumar.srivastava.pranay@gmail.com",
                        appCompatActivity.getResources().getString(R.string.consumerKey)
                        ,appCompatActivity.getResources().getString(R.string.sharedKey),
                        "wtf", appCompatActivity.getResources().getString(R.string.api_url),
                        OAuthConstants.OAuthProto.O_AUTH_PROTO_VER1);

            /*
             * NEED TO ADD SEARCH_EXPRESSION and any other
             * parameters. This would be done in PRE-EXECUTE?
             */
            ArrayList<MethodParam> foodParams = new ArrayList<MethodParam>();
            foodParams.add(new MethodParam(OAuthConstants.OAUTH_ACCESS_KEY, p.getoAuthSecret()));
            foodParams.add(new MethodParam(OAuthConstants.OAUTH_AUTH_TOKEN,  p.getoAuthToken()));
            foodParams.add(new MethodParam(FoodConstants.SEARCH_EXPRESSION, params[0]));
            foodParams.addAll(getFixedResourceParams());

            FoodInfo[] foods = Food.search(OAuthConstants.OAuthProto.O_AUTH_PROTO_VER1,
                    foodParams.toArray(new MethodParam[0]));

            for (int i = 0; foods != null && i < foods.length; i++) {
                AppLogger.getInstance().debug(foods[i].toString());
            }

            if(p != null) {
                if (!p.getUserInformation())
                    AppLogger.getInstance().debug("User information not found");
                AppLogger.getInstance().debug(p.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    private ArrayList<MethodParam> getFixedResourceParams()
    {
        ArrayList<MethodParam> result = new ArrayList<MethodParam>();

        result.add(new MethodParam(OAuthConstants.OAUTH_CONSUMER_KEY,
                appCompatActivity.getResources().getString(R.string.consumerKey)));

        result.add(new MethodParam(OAuthConstants.OAUTH_SHARED_KEY,
                appCompatActivity.getResources().getString(R.string.sharedKey)));

        result.add(new MethodParam(OAuthConstants.OAUTH_NONCE, "wtf"));

        result.add(new MethodParam(OAuthConstants.OAUTH_URL,
                appCompatActivity.getResources().getString(R.string.api_url)));

        return result;
    }
}
