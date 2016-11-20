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
import com.example.pranay.nutrihome.fatsecret.Profile.Profile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pranay on 18/9/16.
 */
public class AddDeleteFavoriteMethod extends CommonMethod<FoodInfo> {

    public static String FOOD_ID = "food_id";
    public AddDeleteFavoriteMethod()
    {
        super();
    }
    public AddDeleteFavoriteMethod(OAuthConstants.OAuthProto proto){
        super(proto);
    }

    @Override
    public ArrayList<FoodInfo> parse(String jsonInput) {
        try {
            JSONObject successObject = new JSONObject(jsonInput);
            String isSuccess = successObject.getString("success");
            if (isSuccess.equals("1"))
                AppLogger.getInstance().debug("Successfully added food");
            else
                AppLogger.getInstance().debug("OOPS....couldn't add food");

        } catch (JSONException e) {
            AppLogger.getInstance().error(e.getMessage());
        }
        /*
         Always return null for this.
         */
        return null;
    }

    private void foodAddorDelete(Profile profile, String foodID, boolean delete)
    {
        if (delete)
            addParameter(FatSecretCommons.METHOD, FoodConstants.METHOD_DELETE_FAVOURITE);
        else
            addParameter(FatSecretCommons.METHOD, FoodConstants.METHOD_ADD_FAVOURITE);

        addParameter(FatSecretCommons.OAUTH_TOKEN, profile.getoAuthToken());
        addParameter(FOOD_ID, foodID);
        addParameter(FatSecretCommons.FORMAT, FatSecretCommons.FORMAT_JSON);
        String reply = sendRequest();
        if (reply != null)
            parse(reply);
    }

    public void addFood(Profile profile,String foodID)
    {
        foodAddorDelete(profile, foodID, false);
    }

    public void deleteFood(Profile profile, String foodID)
    {
        foodAddorDelete(profile, foodID, true);
    }
}