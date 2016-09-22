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
import com.example.pranay.nutrihome.fatsecret.Method;
import com.example.pranay.nutrihome.fatsecret.OAuthRequest;

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

    public void addParameter(String param, String value)
    {
        request.addParameter(param, value);
    }
}
