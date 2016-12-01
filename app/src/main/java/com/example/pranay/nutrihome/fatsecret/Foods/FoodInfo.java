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
import android.util.Pair;

import com.example.pranay.nutrihome.OAuthCommon.OAuthConstants;
import com.example.pranay.nutrihome.fatsecret.FatSecretCommons;
import com.example.pranay.nutrihome.fatsecret.MethodParam;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by pranay on 18/9/16.
 */
public class FoodInfo implements Parcelable {

    public  String
            food_id,
            food_name,
            food_type,
            food_url,
            food_description,
            brand_name,
            measurement;


    protected FoodInfoNutrient[] sortedNutrients;
    protected int nutrientCount;
    protected boolean hasServings;

    protected ArrayList<FoodServing> servings;

    public FoodServing[] getServings()
    {
        if( servings != null)
            return servings.toArray(new FoodServing[0]);
        return null;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        out.writeString(food_id);
        out.writeString(food_name);
        out.writeString(food_type);
        out.writeString(food_url);
        out.writeString(food_description);
        out.writeString(brand_name);
        out.writeString(measurement);
        out.writeValue(hasServings);

        if (hasServings)
            out.writeTypedList(servings);
    }

    public static final Parcelable.Creator<FoodInfo> CREATOR
            = new Parcelable.Creator<FoodInfo>() {
        public FoodInfo createFromParcel(Parcel in) {
            return new FoodInfo(in);
        }

        public FoodInfo[] newArray(int size) {
            return new FoodInfo[size];
        }
    };

    private FoodInfo(Parcel in) {
        food_id = in.readString();
        food_name = in.readString();
        food_type = in.readString();
        food_url = in.readString();
        food_description = in.readString();
        brand_name = in.readString();
        measurement = in.readString();
        hasServings = (boolean) in.readValue(null);
        if (hasServings)
            servings = in.readArrayList(FoodServing.class.getClassLoader());
        setDosageandNutritionalInformation();
        sortNutrients();
    }



    public FoodInfo(String food_id, String food_name, String food_type,
                    String brand_name, String food_description, String food_url)
    {
        this.brand_name = brand_name;
        this.food_description = food_description;
        this.food_url = food_url;
        this.food_name = food_name;
        this.food_type = food_type;
        this.food_id = food_id;
        setDosageandNutritionalInformation();
        sortNutrients();
    }

    private void sortNutrients()
    {
        String [] nutrientsCopy;
        FoodInfoNutrient[] enumCopy = FoodInfoNutrient.values();
        ArrayList<FoodInfoNutrient> nonNullNutrients = new ArrayList<>();

        /*
         * First find out how many nutrients
         * we've as non-null returned.
         */
        for (int i = 0; i < enumCopy.length ; i++) {
            if(nutrients[enumCopy[i].ordinal()] != null)
                nonNullNutrients.add(enumCopy[i]);
        }
        nutrientsCopy = new String[nonNullNutrients.size()];
        /*
         * Only copy the non-null Nutrients in nutrients
         * copy array.
         */
        for (int i = 0; i <nutrientsCopy.length ; i++) {
            nutrientsCopy[i] = nutrients[nonNullNutrients.get(i).ordinal()];
        }

        sortedNutrients = nonNullNutrients.toArray(new FoodInfoNutrient[0]);
        enumCopy = sortedNutrients;
        /*
         * Bubble sort the mappings first.
         */
        for(int i = 0;i < nutrientsCopy.length; i++){
            int lower_index = i;
            for (int j = i + 1 ; j < nutrientsCopy.length ; j++) {
                if (nutrientsCopy[lower_index].compareTo(nutrientsCopy[j]) > 0)
                    lower_index = j;
            }
            String tmp = nutrientsCopy[i];
            FoodInfoNutrient tmpEnum = enumCopy[i];
            enumCopy[i] = enumCopy[lower_index];
            nutrientsCopy[i] = nutrientsCopy[lower_index];
            nutrientsCopy[lower_index] = tmp;
            enumCopy[lower_index] = tmpEnum;
        }
        nutrientCount = sortedNutrients.length;
    }

    public int getNutrientCount()
    {
        return  nutrientCount;
    }

    public Pair<String, String> getSortedNutrientAt(int pos){
        if (pos > 0 && pos < sortedNutrients.length)
            return new Pair<>(sortedNutrients[pos].name(),
                                nutrients[sortedNutrients[pos].ordinal()]);

        return  null;
    }

    private void setDosageandNutritionalInformation()
    {
        StringTokenizer st = new StringTokenizer(food_description, "|");
        while (st.hasMoreElements()) {
            String next = st.nextElement().toString();

            for(FoodInfoNutrient nutrient: FoodInfoNutrient.values()) {
                if (next.toLowerCase().contains(FoodInfoNutrient.CALORIES.name().toLowerCase())) {

                    StringTokenizer forMeasurement = new StringTokenizer(next, "-");
                    while (forMeasurement.hasMoreElements()) {
                        String val = forMeasurement.nextElement().toString();
                        if (val.toLowerCase().contains(FoodInfoNutrient.CALORIES.name().toLowerCase()))
                            nutrients[FoodInfoNutrient.CALORIES.ordinal()] = val.split(":")[1].trim();
                        else
                            measurement = val.trim();
                    }
                    break;
                }
                else {

                    if(next.toLowerCase().contains(nutrient.name().toLowerCase())) {
                        nutrients[nutrient.ordinal()] = next.split(":")[1].trim();
                        break;
                    }
                }
            }
        }
    }


    public enum FoodInfoNutrient {
        PROTEIN,
        CARBS,
        CALORIES,
        FAT
    }


    public static final int MAX_NUTRIENT_COUNT = FoodInfoNutrient.values().length;

    protected String[] nutrients = new String[MAX_NUTRIENT_COUNT];

    public String getNutrient(FoodInfoNutrient nutrient)
    {
        return nutrients[nutrient.ordinal()];
    }


    public boolean isGeneric()
    {
        return food_type.toLowerCase().equals("generic");
    }

    public boolean isBrand()
    {
        return food_type.toLowerCase().equals("brand");
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        FoodInfoNutrient[] enumCopy = FoodInfoNutrient.values();
        sb.append("===\n ID: " + food_id +"\n");
        sb.append("Name: " + food_name + "\n");
        sb.append("Type: " + food_type +"\n");
        sb.append("URL: " + food_url + "\n");
        sb.append("Description: " + food_description + "\n");
        sb.append("Carbs: "+ getNutrient(FoodInfoNutrient.CARBS) + "\n");
        sb.append("Protein: " + getNutrient(FoodInfoNutrient.PROTEIN) + "\n");
        sb.append("Fat: " + getNutrient(FoodInfoNutrient.FAT) + "\n");
        sb.append("Measurement: " + measurement + "\n");
        sb.append("Brand: " + brand_name + "\n===\n");

        for (int i = 0; i < nutrients.length; i++) {
            if (nutrients[i] != null) {
                sb.append(enumCopy[i].name() +": " + nutrients[i] + "\n");
            }
        }

        if (servings != null)
        {
            for (FoodServing serving:servings
                 ) {
                sb.append(serving);
            }
        }

        return sb.toString();
    }

    public static final int PERCENT_CARBS = 0,
                            PERCENT_PROTEIN = 1,
                            PERCENT_FAT = 2;
    private static final int CALORIE_PER_GRAM[] = {
            4, /*By CARBOHYDRATE calorie per gram*/
            4, /*By PROTEIN calorie per gram*/
            9, /*By FAT calorie per gram*/
    };

    public float getPercentCalorie(int how, float amount, float totalCalories)
    {
        if (how > CALORIE_PER_GRAM.length)
            return 0.0f;
        return (CALORIE_PER_GRAM[how] * amount) / totalCalories;
    }

    public void initliaizeServings(OAuthConstants.OAuthProto proto,
                                   MethodParam...params)
    {
        /*
         * Don't do unnecessary calls to method.
         */

        if (servings != null)
            return;

        GetMethod getMethod = new GetMethod(proto, food_id);
        getMethod.addParameter(FatSecretCommons.METHOD, FoodConstants.METHOD_GET);
        getMethod.addParameter(FatSecretCommons.FORMAT, FatSecretCommons.FORMAT_JSON);
        String jsonOutput = getMethod.sendRequest(params);
        servings = getMethod.parse(jsonOutput);

    }
}
