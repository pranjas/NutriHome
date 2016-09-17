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

package com.example.pranay.nutrihome.fatsecret.Profile;

/**
 * Created by pranay on 17/9/16.
 */
public interface ProfileConstants {
    String  AUTH_TOKEN = "auth_token",
            AUTH_SECRET = "auth_secret";

    String  JSON_OBJECT_NAME = "profile";

    String  METHOD_PROFILE_CREATE = "profile.create",
            METHOD_PROFILE_GET = "profile.get",
            METHOD_PROFILE_GET_AUTH = "profile.get_auth";

    String USER_ID = "user_id";
}
