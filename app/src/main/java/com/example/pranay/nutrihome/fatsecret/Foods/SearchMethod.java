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

package com.example.pranay.nutrihome.fatsecret.Foods;

import com.example.pranay.nutrihome.AppLogger;
import com.example.pranay.nutrihome.OAuthCommon.OAuthConstants;
import com.example.pranay.nutrihome.fatsecret.FatSecretCommons;
import com.example.pranay.nutrihome.fatsecret.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pranay on 18/9/16.
 */
public class SearchMethod extends CommonMethod<FoodInfo> {

    public SearchMethod(OAuthConstants.OAuthProto proto)
    {
        super(proto);
    }


    @Override
    public String sendRequest(MethodParam...params) {
        /*
         * TODO:
         * Handle OAUTH_TOKEN and OAUTH_ACCESS key here.
         */
        for(MethodParam p : params) {
            request.addParameter(p.name, p.value);
        }
        return request.sendRequest(true, true);
    }

    @Override
    public FoodInfo[] parse(String jsonInput) {
        try {
            JSONObject foods = new JSONObject(jsonInput);
            JSONArray allFoods = foods.getJSONObject(FoodConstants.JSON_OBJECT_NAME)
                    .getJSONArray(FoodConstants.JSON_ARRAY_NAME);

            ArrayList<FoodInfo> items = new ArrayList<FoodInfo>();

            for (int i = 0; i < allFoods.length(); i++) {
                JSONObject jsonItem = allFoods.getJSONObject(i);
                FoodInfo fInfo = new FoodInfo(
                        jsonItem.getString("food_id"),
                        jsonItem.getString("food_name"),
                        jsonItem.getString("food_type"),
                        jsonItem.optString("brand_name"),
                        jsonItem.getString("food_description"),
                        jsonItem.getString("food_url"));
                items.add(fInfo);
            }
            return items.toArray(new FoodInfo[0]);

        } catch (JSONException e) {
            AppLogger.getInstance().error(e.getMessage());
        }
        return null;
    }
}
