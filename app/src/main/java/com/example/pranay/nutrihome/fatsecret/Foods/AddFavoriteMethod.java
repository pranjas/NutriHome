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
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pranay on 18/9/16.
 */
public class AddFavoriteMethod extends CommonMethod<FoodInfo> {

    public AddFavoriteMethod()
    {
        super();
    }
    public AddFavoriteMethod(OAuthConstants.OAuthProto proto){
        super(proto);
    }

    @Override
    public FoodInfo[] parse(String jsonInput) {
        try {
            JSONObject successObject = new JSONObject(jsonInput);

        } catch (JSONException e) {
            AppLogger.getInstance().error(e.getMessage());
        }
        return null;
    }

    @Override
    public String sendRequest(MethodParam... params) {
        return null;
    }
}