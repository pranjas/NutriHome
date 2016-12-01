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

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pranay on 12/11/16.
 */
public class FoodServing implements Parcelable{

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(nutrients);
    }

    private FoodServing(Parcel in)
    {
        in.readStringArray(nutrients);
    }

    public static final Parcelable.Creator<FoodServing> CREATOR
            = new Parcelable.Creator<FoodServing>() {
        public FoodServing createFromParcel(Parcel in) {
            return new FoodServing(in);
        }

        public FoodServing[] newArray(int size) {
            return new FoodServing[size];
        }
    };

    public enum FoodServingNutrient {
        SATURATED_FAT,
        POLYUNSATURATED_FAT,
        MONOUNSATURATED_FAT,
        TRANS_FAT,
        CHOLESTEROL, /*This is the way it is spelled in fa-secret json response*/
        SODIUM,
        POTASSIUM, /*This is the way it is spelled in fa-secret json response*/
        SUGAR,
        VITAMIN_A,
        VITAMIN_C,
        CALCIUM,
        IRON
    }

    private String food_id;

    public enum FoodServingField {
        serving_id,
        serving_description,
        serving_url,
        metric_serving_amount,
        metric_serving_unit,
        number_of_units,
        measurement_description
    }

    private String [] mFoodServingParameter = null;

    public FoodServing(String food_id)
    {
        this.food_id= food_id;
        nutrients = new String[FoodServingNutrient.values().length];
        mFoodServingParameter = new String[FoodServingField.values().length];
    }



    private String nutrients[];

    public void setNutrient(FoodServingNutrient which, String value)
    {
        nutrients[which.ordinal()] = value;
    }

    public void setFoodServingParameter(FoodServingField which, String value)
    {
        mFoodServingParameter[which.ordinal()] = value;
    }

    public String getFoodServingParameter(FoodServingField which)
    {
        return mFoodServingParameter[which.ordinal()];
    }

    public String getNutrient(FoodServingNutrient which)
    {
        return nutrients[which.ordinal()];
    }

    public static FoodServingField mapStringToField(String name)
    {
        for (FoodServingField field:FoodServingField.values()
             ) {
            if (name.equals(field.name()))
                return field;
        }
        return null;
    }

    public static FoodServingNutrient mapStringToNutrient(String name)
    {
        for (FoodServingNutrient nutrient:FoodServingNutrient.values()
             ) {
            if(name.equals(nutrient.name()))
                return nutrient;
        }
        return null;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (FoodServingField field: FoodServingField.values()
             ) {
            sb.append(field.name() +" = " + getFoodServingParameter(field) +"\n");
        }

        for (FoodServingNutrient nutrient: FoodServingNutrient.values()
             ) {
            sb.append(nutrient.name() +" = " + getNutrient(nutrient) +"\n");
        }

        return sb.toString();
    }
}
