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

package com.example.pranay.nutrihome.OAuthCommon;

/**
 * Created by pranay on 3/9/16.
 */
public interface OAuthConstants {
    String  OAUTH_CONSUMER_KEY = "oauth_consumer_key",
            OAUTH_SIGNATURE_METHOD = "oauth_signature_method",
            OAUTH_TIMESTAMP = "oauth_timestamp",
            OAUTH_NONCE = "oauth_nonce",
            OAUTH_VERSION = "oauth_version",
            OAUTH_SIGNATURE = "oauth_signature",
            OAUTH_HMAC_SIGNATURE_METHOD = "HMAC-SHA1";

    enum OAuthProto {
        O_AUTH_PROTO_VER1,
        O_AUTH_PROTO_VER2
    };
}
