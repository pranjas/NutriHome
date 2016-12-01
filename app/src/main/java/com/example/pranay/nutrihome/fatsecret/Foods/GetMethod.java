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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pranay on 7/11/16.
 */
public class GetMethod extends CommonMethod<FoodServing> {

    private String food_id;

    static {
        FoodServing.FoodServingField[] fields =
                FoodServing.FoodServingField.values();

        FoodServing.FoodServingNutrient[] nutrients =
                FoodServing.FoodServingNutrient.values();
    }
    public GetMethod(OAuthConstants.OAuthProto proto, String food_id)
    {
        super(proto);
        this.food_id = food_id;
        addParameter(FoodConstants.FOOD_ID, food_id);
    }

    private FoodServing __GetServing(JSONObject serving)
    {
        FoodServing fserv = new FoodServing(food_id);

        /*
         * Instead of doing this by hand,
         * better to use the names of fields instead.
         * Given the fact that the number of fields are not
         * huge, but typing them all is a real pain in
         * the ass....
         */

        /*
         * Map all available field names first.
         */
        for (FoodServing.FoodServingField field: FoodServing.FoodServingField.values()
                ) {
            fserv.setFoodServingParameter(field, serving.optString(field.name().toLowerCase()));
        }

        /*
         * Map all available nurients next.
         */

        for (FoodServing.FoodServingNutrient nutrient: FoodServing.FoodServingNutrient.values()
                ) {
            fserv.setNutrient(nutrient, serving.optString(nutrient.name().toLowerCase()));
        }

        return fserv;
    }

    @Override
    public ArrayList<FoodServing> parse(String jsonInput) {
        ArrayList<FoodServing> result = new ArrayList<>();
        try {
            JSONObject food = new JSONObject(jsonInput);

            JSONArray allServings = null;
            JSONObject singleServing = null;

            Object servingObject = food.getJSONObject(FoodConstants.JSON_ARRAY_NAME).
                    getJSONObject(FoodConstants.JSON_SERVINGS_NAME)
                    .get(FoodConstants.JSON_SERVING_NAME);

            if (servingObject instanceof JSONArray)
                allServings = (JSONArray)servingObject;
            else
                singleServing = (JSONObject)servingObject;

            /*
             * We really don't need any of these,
             * apart from the fserv, which we add to the
             * result anyway, which is why the blocked code.
             */

            if (allServings !=null)
            {
                for (int i = 0; i < allServings.length(); i++) {
                    JSONObject serving = allServings.getJSONObject(i);
                    result.add(__GetServing(serving));
                }
            }else if (singleServing != null)
            {
                result.add(__GetServing(singleServing));
            }
            else {
                AppLogger.getInstance().error("BUG!!");
                throw new RuntimeException("Invalid JSON Object state");
            }
        } catch (JSONException e) {
            AppLogger.getInstance().error(e.getMessage());
            result.clear();
            result = null;
        }
        return result;
    }
}