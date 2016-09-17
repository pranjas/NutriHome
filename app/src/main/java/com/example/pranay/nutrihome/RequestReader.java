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

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.example.pranay.nutrihome.OAuthCommon.OAuthConstants;
import com.example.pranay.nutrihome.fatsecret.FatSecretCommons;
import com.example.pranay.nutrihome.fatsecret.Food;
import com.example.pranay.nutrihome.fatsecret.FoodConstants;
import com.example.pranay.nutrihome.fatsecret.OAuthRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;

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
    protected Long doInBackground(String... params) {
        OAuthRequest request = null;
        try {
            Food f = new Food(
                    appCompatActivity.getResources().getString(R.string.consumerKey)
                    ,appCompatActivity.getResources().getString(R.string.sharedKey),
                    "wtf", appCompatActivity.getResources().getString(R.string.api_url),
                    OAuthConstants.OAuthProto.O_AUTH_PROTO_VER1);
            Food.FoodInfo[] foods = f.search("high protein", 0);
            for (int i = 0; i < foods.length; i++) {
                AppLogger.getInstance().debug(foods[i].toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }
}
