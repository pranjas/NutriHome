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

package com.example.pranay.nutrihome.fatsecret;

import com.example.pranay.nutrihome.AppLogger;
import com.example.pranay.nutrihome.OAuthCommon.OAuthConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.ArrayList;

/**
 * Created by pranay on 17/9/16.
 */
public class Food {
    private OAuthRequest request;

    public Food(String consumerKey, String sharedKey,
                String nonce, String url,
                OAuthConstants.OAuthProto proto)
    {
        request = new OAuthRequest(consumerKey,sharedKey, nonce, url, proto);

    }

    public FoodInfo[] search(String pattern, int page)
    {
        request.clear();
        request.addParameter(FatSecretCommons.METHOD, FoodConstants.METHOD_SEARCH);
        request.addParameter(FoodConstants.SEARCH_EXPRESSION, pattern);
        request.addParameter(FatSecretCommons.FORMAT, FatSecretCommons.FORMAT_JSON);
        request.addParameter(FatSecretCommons.PAGE_NUMBER, String.valueOf(page));

        if (request.getoAuthManager().getoAuthToken().length() > 0)
            request.addParameter(FatSecretCommons.OAUTH_TOKEN,
                    request.getoAuthManager().getoAuthToken());
        String jsonOutput = request.sendRequest(true, true);
        try {
            JSONObject foods = new JSONObject(jsonOutput);
            JSONArray allFoods = foods.getJSONObject(FoodConstants.JSON_OBJECT_NAME)
                                    .getJSONArray(FoodConstants.JSON_ARRAY_NAME);

            ArrayList<FoodInfo> items = new ArrayList<FoodInfo>();

            for (int i = 0; i < allFoods.length(); i++) {
                JSONObject jsonItem = allFoods.getJSONObject(i);
                FoodInfo fInfo = new FoodInfo();
                        fInfo.food_id = jsonItem.getString("food_id");
                        fInfo.food_name = jsonItem.getString("food_name");
                        fInfo.food_type = jsonItem.getString("food_type");
                        fInfo.brand_name = jsonItem.optString("brand_name");
                        fInfo.food_description = jsonItem.getString("food_description");
                        fInfo.food_url = jsonItem.getString("food_url");
                items.add(fInfo);
            }

            return items.toArray(new FoodInfo[0]);

        } catch (JSONException e) {
            AppLogger.getInstance().error(e.getMessage());
        }
        return null;
    }

    public class FoodInfo {
        public  String
                food_id,
                food_name,
                food_type,
                food_url,
                food_description,
                brand_name;

        public boolean isGeneric()
        {
            return food_type.toLowerCase().equals("generic");
        }

        public boolean isBrand()
        {
            return food_type.toLowerCase().equals("brand");
        }

        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.append("===\n ID: " + food_id +"\n");
            sb.append("Name: " + food_name + "\n");
            sb.append("Type: " + food_type +"\n");
            sb.append("URL: " + food_url + "\n");
            sb.append("Description: " + food_description + "\n");
            sb.append("Brand: " + brand_name + "\n===\n");

            return sb.toString();
        }
    }

}
