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

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.pranay.nutrihome.fatsecret.Foods.FoodInfo;

public class FoodItemShow extends AppCompatActivity {

    private FoodInfo foodItem;
    private String parentSearchExpression;
    private int parentSearchPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item_show);
        Intent parentIntent = getIntent();
        if (parentIntent != null) {
            foodItem = parentIntent.getParcelableExtra(IntentURI.FOOD_ITEM_SHOW);
            parentSearchExpression = parentIntent.getStringExtra(IntentURI.SEARCH_FOOD);
            parentSearchPage = parentIntent.getIntExtra(IntentURI.SEARCH_FOOD_PAGE, 0);
            showFoodItem();
        }
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
