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

import com.example.pranay.nutrihome.OAuthCommon.OAuthConstants;
import com.example.pranay.nutrihome.fatsecret.FatSecretCommons;
import com.example.pranay.nutrihome.fatsecret.MethodParam;

import java.util.ArrayList;

/**
 * Created by pranay on 17/9/16.
 */
public class Food {

    public static ArrayList<FoodInfo> search(
            OAuthConstants.OAuthProto proto,
            MethodParam...params) {

        SearchMethod searchMethod = new SearchMethod(proto);
        searchMethod.addParameter(FatSecretCommons.METHOD, FoodConstants.METHOD_SEARCH);
        String jsonOutput = searchMethod.sendRequest(params);
        return searchMethod.parse(jsonOutput);
    }
}