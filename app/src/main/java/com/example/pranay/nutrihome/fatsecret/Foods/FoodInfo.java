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
            brand_name;

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
    }

    private void setDosageandNutritionalInformation()
    {
        StringTokenizer st = new StringTokenizer(food_description, "|");
        while (st.hasMoreElements()) {
            String next = st.nextElement().toString();

            if(next.toLowerCase().contains("fat"))
                fat = next.split(":")[1].trim();

            else if (next.toLowerCase().contains("carbs"))
                carbohydrates = next.split(":")[1].trim();

            else if (next.toLowerCase().contains("protein"))
                protein = next.split(":")[1].trim();

            else if (next.toLowerCase().contains("saturated_fat"))
                saturated_fat = next.split(":")[1].trim();

            else if (next.toLowerCase().contains("polyunsaturated_fat"))
                polyunsaturated_fat = next.split(":")[1].trim();

            else if (next.toLowerCase().contains("monounsaturated_fat"))
                monounsaturated_fat = next.split(":")[1].trim();

            else if (next.toLowerCase().contains("trans_fat"))
                trans_fat = next.split(":")[1].trim();

            else if (next.toLowerCase().contains("cholestrol"))
                cholestrol = next.split(":")[1].trim();

            else if (next.toLowerCase().contains("sodium"))
                sodium = next.split(":")[1].trim();

            else if (next.toLowerCase().contains("potassium"))
                potassium = next.split(":")[1].trim();

            else if (next.toLowerCase().contains("sugar"))
                sugar = next.split(":")[1].trim();

            else if (next.toLowerCase().contains("vitamin_a"))
                vitamin_a = next.split(":")[1].trim();

            else if (next.toLowerCase().contains("vitamin_c"))
                vitamin_c = next.split(":")[1].trim();

            else if (next.toLowerCase().contains("calcium"))
                calcium = next.split(":")[1].trim();

            else if (next.toLowerCase().contains("iron"))
                iron = next.split(":")[1].trim();

            else if (next.toLowerCase().contains("calories")) {

                StringTokenizer forMeasurement = new StringTokenizer(next,"-");
                while (forMeasurement.hasMoreElements()) {
                    String val = forMeasurement.nextElement().toString();
                    if (val.toLowerCase().contains("calories"))
                        calories = val.split(":")[1].trim();
                    else
                        measurement = val.trim();
                }
            }
        }
    }
    protected String
            protein,
            carbohydrates,
            calories,
            fat,
            saturated_fat,
            polyunsaturated_fat,
            monounsaturated_fat,
            trans_fat,
            cholestrol,
            sodium,
            potassium,
            sugar,
            vitamin_a,
            vitamin_c,
            calcium,
            iron;

    protected String measurement;

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
        sb.append("Carbs: "+ carbohydrates + "\n");
        sb.append("Protein: " + protein + "\n");
        sb.append("Fat: " + fat + "\n");
        sb.append("Measurement: " + measurement + "\n");
        sb.append("Brand: " + brand_name + "\n===\n");

        return sb.toString();
    }

}
