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

/**
 * Created by pranay on 17/9/16.
 */
public interface FoodConstants {
    String  METHOD_SEARCH = "foods.search",
            METHOD_GET = "food.get",
            METHOD_ADD_FAVOURITE = "food.add_favorite",
            METHOD_DELETE_FAVOURITE ="food.delete_favorite",
            METHOD_GET_FAVOURITES = "foods.get_favorites",
            METHOD_GET_MOST_EATEN = "foods.get_most_eaten",
            METHOD_GET_RECENTLY_EATEN = "foods.get_recently_eaten";

    String  SEARCH_EXPRESSION = "search_expression";

    /*
     * One Foods JSON object has a JSONArray of food.
     * To get each food item, first we need to get
     * the foods and then each food within it.
     */
    String JSON_OBJECT_NAME = "foods";
    String JSON_ARRAY_NAME = "food";
    String JSON_OBJECT_SUCCESS = "success";
}
