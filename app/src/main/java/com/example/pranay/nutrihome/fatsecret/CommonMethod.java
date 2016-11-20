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

import android.content.Context;

import com.example.pranay.nutrihome.AppGlobalState;
import com.example.pranay.nutrihome.OAuthCommon.OAuthConstants;
import com.example.pranay.nutrihome.R;

import java.util.ArrayList;

/**
 * Created by pranay on 20/9/16.
 */
public abstract class CommonMethod<T> implements Method<T> {

    protected OAuthRequest request;

    public CommonMethod()
    {
        request = new OAuthRequest();
    }

    public CommonMethod(OAuthConstants.OAuthProto proto) {
        request = new OAuthRequest(proto);
    }

    public String sendRequest(MethodParam... params) {

        for(MethodParam p : params) {
            request.addParameter(p.name, p.value);
        }

        for (MethodParam p:getFixedResourceParams()
             ) {
            request.addParameter(p.name, p.value);
        }
        return request.sendRequest(true, true);
    }

    private ArrayList<MethodParam> getFixedResourceParams()
    {
        Context context = AppGlobalState.getGlobalContext();

        ArrayList<MethodParam> result = new ArrayList<MethodParam>();

        result.add(new MethodParam(OAuthConstants.OAUTH_CONSUMER_KEY,
                context.getResources().getString(R.string.consumerKey)));

        result.add(new MethodParam(OAuthConstants.OAUTH_SHARED_KEY,
                context.getResources().getString(R.string.sharedKey)));

        result.add(new MethodParam(OAuthConstants.OAUTH_NONCE, "wtf"));

        result.add(new MethodParam(OAuthConstants.OAUTH_URL,
                context.getResources().getString(R.string.api_url)));

        result.add(new MethodParam(FatSecretCommons.FORMAT, FatSecretCommons.FORMAT_JSON));

        return result;
    }


    public void addParameter(String param, String value)
    {
        request.addParameter(param, value);
    }
}
