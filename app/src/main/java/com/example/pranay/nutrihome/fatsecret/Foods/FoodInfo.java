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

import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * Created by pranay on 18/9/16.
 */
public class FoodInfo {

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
        FAT,
        SATURATED_FAT,
        POLYUNSATURATED_FAT,
        MONOUNSATURATED_FAT,
        TRANS_FAT,
        CHOLESTROL,
        SODIUM,
        POTTASIUM,
        SUGAR,
        VITAMIN_A,
        VITAMIN_C,
        CALCIUM,
        IRON
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

        return sb.toString();
    }
}
