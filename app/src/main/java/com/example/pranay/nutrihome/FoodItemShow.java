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

package com.example.pranay.nutrihome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.Space;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.pranay.nutrihome.OAuthCommon.OAuthConstants;
import com.example.pranay.nutrihome.fatsecret.Foods.FoodInfo;
import com.example.pranay.nutrihome.fatsecret.Foods.FoodServing;

import java.util.ArrayList;
import java.util.List;

public class FoodItemShow extends AppCompatActivity {

    private FoodInfo foodItem;
    private String parentSearchExpression;
    private int parentSearchPage;

    private ProgressDialog progressDialog;

    private ArrayAdapter<String>
                    servingArrayAdapter;
    private FoodServing[] allServings;

    private int selectedServing;



    /*
     * This is the container Layout for the,
     * rest of nutrients layout within it.
     */
    private LinearLayout.LayoutParams nutritionalInfoParam
            = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);

    /*
     * This is applied to each of the nutrient,
     * text field.
     */
    private LinearLayout.LayoutParams nutrientLayoutParam
            = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);

    private void showSelectedServing()
    {
        LinearLayout nutritionInfo = (LinearLayout)
                findViewById(R.id.nutritionLayout);
        nutritionInfo.removeAllViews();

        /*
         * From the available nutrition values,
         * only set the ones which are available.
         */
        for(FoodServing.FoodServingNutrient nutrient :
                FoodServing.FoodServingNutrient.values())
        {
            String val = allServings[selectedServing].getNutrient(nutrient);
            if (val.length() > 0) {
                /*
                 * First create a Linear Layout.
                 */
                LinearLayout linearLayout =
                        (LinearLayout)findViewById(R.id.nutritionLayout);

                LayoutInflater inflater = getLayoutInflater();
                LinearLayout tblLayout = (LinearLayout)
                        inflater.inflate(R.layout.serving_layout, null);

                TextView nutritionLabel =
                        (TextView)tblLayout.findViewById(R.id.txtNutritionLabel);

                TextView nutritionValue =
                        (TextView)tblLayout.findViewById(R.id.txtNutritionValue);

                nutritionValue.setText(val);

                /*
                if (Build.VERSION.SDK_INT >= 23) {
                    nutritionLabel.setTextColor(
                            getResources().getColor(R.color.textColorWhite
                                    , this.getTheme()));

                    nutritionValue.setTextColor(getResources().getColor(R.color.textColorWhite
                            , this.getTheme()));
                } else {
                    nutritionLabel.setTextColor(getResources().getColor(R.color.textColorWhite));
                    nutritionValue.setTextColor(getResources().getColor(R.color.textColorWhite));
                }
                */
                nutritionLabel.setText(nutrient.name());
                tblLayout.setLayoutParams(nutritionalInfoParam);
                /*
                nutritionLabel.setLayoutParams(nutrientLayoutParam);
                nutritionValue.setLayoutParams(nutrientLayoutParam);
                */
                linearLayout.addView(tblLayout);
            }
        }
    }
    private class FoodItemInitializer extends AsyncTask<Integer, String, String>
    {

        @Override
        protected void onPreExecute()
        {
            if (progressDialog == null)
                progressDialog = ProgressDialog.show(FoodItemShow.this,
                        "Getting Food Info...", foodItem.food_name, true);
            else
                progressDialog.show();
        }

        /*
         * Must be called in post-execute
         */
        private void populateServingsInSpinner()
        {
            Spinner spinner = (Spinner)findViewById(R.id.spinnerServings);
            allServings = foodItem.getServings();

            /*
             * The below condition should never
             * happen though. However to be on
             * safer side, better to check rather
             * than have application crash.
             * 
             */
            if (allServings == null) {
                if (servingArrayAdapter != null) {
                    servingArrayAdapter.clear();
                    servingArrayAdapter.notifyDataSetChanged();
                }
                return;
            }

            ArrayList<String> spinnerList
                    = new ArrayList<>();

            /*
             * Create a simple spinnerList,
             * instead of using Serving Objects
             * directly.
             */
            for(FoodServing serving: allServings) {

                spinnerList.add(serving.getFoodServingParameter(
                        FoodServing.FoodServingField.serving_description
                ) + ":" +serving.getFoodServingParameter(FoodServing.FoodServingField.serving_id));
            }

            if (servingArrayAdapter != null) {
                servingArrayAdapter.clear();
                servingArrayAdapter.addAll(spinnerList);
            }
            else {
                servingArrayAdapter = new
                        ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_spinner_item,
                        spinnerList);

                servingArrayAdapter.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item
                );

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedServing = position;
                        showSelectedServing();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
            spinner.setAdapter(servingArrayAdapter);
            servingArrayAdapter.notifyDataSetChanged();
        }
        @Override
        protected  void onPostExecute(String result)
        {
            progressDialog.dismiss();
            populateServingsInSpinner();
            showFoodItem();
            AppLogger.getInstance().debug(foodItem.toString());
        }

        /*
         * Return any n
         */
        @Override
        protected String doInBackground(Integer... params) {
            foodItem.initliaizeServings(OAuthConstants.OAuthProto.O_AUTH_PROTO_VER1);
            return "";
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item_show);

        Intent parentIntent = getIntent();
        if (parentIntent != null) {
            foodItem = parentIntent.getParcelableExtra(IntentURI.FOOD_ITEM_SHOW);
            parentSearchExpression = parentIntent.getStringExtra(IntentURI.SEARCH_FOOD);
            parentSearchPage = parentIntent.getIntExtra(IntentURI.SEARCH_FOOD_PAGE, 0);

            /*
             * Initialize the food item in a seperate
             * thread to avoid main thread exceptions.
             */
            new FoodItemInitializer().execute();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem)
    {

        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.putExtra(IntentURI.SEARCH_FOOD, parentSearchExpression);
                upIntent.putExtra(IntentURI.SEARCH_FOOD_PAGE, parentSearchPage);

                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void showFoodItem()
    {
        TextView txtName = (TextView)findViewById(R.id.txtName);
        TextView txtNutritionInfo = (TextView)findViewById(R.id.txtNutritionInfo);
        txtName.setText(foodItem.food_name);
        txtNutritionInfo.setText(foodItem.food_description);
    }

}
