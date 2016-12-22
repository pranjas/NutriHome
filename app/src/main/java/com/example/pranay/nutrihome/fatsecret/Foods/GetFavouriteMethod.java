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
import com.example.pranay.nutrihome.fatsecret.CommonMethod;
import com.example.pranay.nutrihome.fatsecret.FatSecretCommons;
import com.example.pranay.nutrihome.fatsecret.MethodParam;
import com.example.pranay.nutrihome.fatsecret.Profile.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pranay on 8/12/16.
 */
public class GetFavouriteMethod extends CommonMethod<FoodInfo> {

    private ArrayList<FoodInfo> result;
    private Profile profile;
    @Override
    public ArrayList<FoodInfo> parse(String jsonInput)
    {
        try {
            JSONObject rootObject = new JSONObject(jsonInput);
            Object allFoods = rootObject.getJSONObject(FoodConstants.JSON_OBJECT_NAME)
                    .get(FoodConstants.JSON_ARRAY_NAME);
            JSONObject singleFood = null;
            JSONArray multiFood = null;

            if (allFoods instanceof JSONObject)
                singleFood = (JSONObject)allFoods;
            else
                multiFood = (JSONArray) allFoods;

            if (singleFood != null) {
                FoodInfo fInfo = new FoodInfo(
                        singleFood.getString("food_id"),
                        singleFood.getString("food_name"),
                        singleFood.getString("food_type"),
                        singleFood.optString("brand_name"),
                        singleFood.getString("food_description"),
                        singleFood.getString("food_url"));
                fInfo.initliaizeServings(OAuthConstants.OAuthProto.O_AUTH_PROTO_VER1,
                        new MethodParam(
                                OAuthConstants.OAUTH_AUTH_TOKEN,
                                profile.getoAuthToken()
                        ));
                result.add(fInfo);
            }else {
                for (int i = 0; i < multiFood.length(); i++) {
                    JSONObject jsonItem = multiFood.getJSONObject(i);
                    FoodInfo fInfo = new FoodInfo(
                            jsonItem.getString("food_id"),
                            jsonItem.getString("food_name"),
                            jsonItem.getString("food_type"),
                            jsonItem.optString("brand_name"),
                            jsonItem.getString("food_description"),
                            jsonItem.getString("food_url"));

                    fInfo.initliaizeServings(OAuthConstants.OAuthProto.O_AUTH_PROTO_VER1,
                            new MethodParam(
                                    OAuthConstants.OAUTH_AUTH_TOKEN,
                                    profile.getoAuthToken()
                            ));

                    result.add(fInfo);
                }
            }
        }catch (JSONException ex) {
            AppLogger.getInstance().debug(ex.getMessage());
        }
        return result;
    }

    private GetFavouriteMethod()
    {
        super();
        result = new ArrayList<>();
    }

    public GetFavouriteMethod(Profile profile)
    {
        this();
        this.profile = profile;
    }

    public GetFavouriteMethod(OAuthConstants.OAuthProto proto,
                              Profile profile)
    {
        super(proto);
        result = new ArrayList<>();
        this.profile = profile;
    }

    public ArrayList<FoodInfo> getFavourites()
    {
        addParameter(FatSecretCommons.METHOD, FoodConstants.METHOD_GET_FAVOURITES);

        addParameter(OAuthConstants.OAUTH_AUTH_TOKEN, profile.getoAuthToken());
        addParameter(OAuthConstants.OAUTH_ACCESS_KEY, profile.getoAuthSecret());
        addParameter(FatSecretCommons.FORMAT, FatSecretCommons.FORMAT_JSON);
        return parse(sendRequest());
    }
}
