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
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.pranay.nutrihome.OAuthCommon.OAuthConstants;
import com.example.pranay.nutrihome.fatsecret.Foods.Food;
import com.example.pranay.nutrihome.fatsecret.Foods.FoodConstants;
import com.example.pranay.nutrihome.fatsecret.Foods.FoodInfo;
import com.example.pranay.nutrihome.fatsecret.Foods.MethodParam;
import com.example.pranay.nutrihome.fatsecret.Profile.Profile;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class FoodSearchResult extends AppCompatActivity {

    protected ProgressDialog progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent fromSearchActivity  = getIntent();
        String searchText = "";
        if (fromSearchActivity != null)
            searchText = fromSearchActivity.getExtras().getString(IntentURI.SEARCH_FOOD);
        setContentView(R.layout.activity_food_search_result);
        progressBar = ProgressDialog.show(this,
                            "Loading...", "Searching for " + searchText,
                            true);
        new RequestReader().execute(searchText);
    }


    private class RequestReader extends AsyncTask<String, Integer, ArrayList<FoodInfo>> {

        private FoodSearchResult appCompatActivity = FoodSearchResult.this;

        @Override
        protected void onPostExecute(ArrayList<FoodInfo> result)
        {
            progressBar.dismiss();
            if(result == null) {
                /*
                 *Go back to parent activity.
                 */
                Intent parentIntent = getParentActivityIntent();
                parentIntent.putExtra(IntentURI.SEARCH_NO_RESULT, "No results for search: ");
                NavUtils.navigateUpTo(FoodSearchResult.this, parentIntent);
                return;
            }
            ListView listView = (ListView)findViewById(R.id.listSearchFood);

            if (listView.getAdapter().getCount() == 0)
                listView.setAdapter(new FoodSearchAdapter(FoodSearchResult.this,
                        R.layout.search_list, result));
            else {
                ArrayAdapter <FoodInfo> listAdapter = (ArrayAdapter)listView.getAdapter();
                listAdapter.addAll(result);
            }
        }

        @Override
        protected ArrayList<FoodInfo> doInBackground(String... params) {
            try {
                Profile p = Profile.createProfile( "kumar.srivastava.pranay@gmail.com",
                        appCompatActivity.getResources().getString(R.string.consumerKey)
                        ,appCompatActivity.getResources().getString(R.string.sharedKey),
                        "wtf", appCompatActivity.getResources().getString(R.string.api_url),
                        OAuthConstants.OAuthProto.O_AUTH_PROTO_VER1
                );

                if (p == null)
                    p = Profile.getProfileFromServer("kumar.srivastava.pranay@gmail.com",
                            appCompatActivity.getResources().getString(R.string.consumerKey)
                            ,appCompatActivity.getResources().getString(R.string.sharedKey),
                            "wtf", appCompatActivity.getResources().getString(R.string.api_url),
                            OAuthConstants.OAuthProto.O_AUTH_PROTO_VER1);

            /*
             * NEED TO ADD SEARCH_EXPRESSION and any other
             * parameters. This would be done in PRE-EXECUTE?
             */
                ArrayList<MethodParam> foodParams = new ArrayList<MethodParam>();
                foodParams.add(new MethodParam(OAuthConstants.OAUTH_ACCESS_KEY, p.getoAuthSecret()));
                foodParams.add(new MethodParam(OAuthConstants.OAUTH_AUTH_TOKEN,  p.getoAuthToken()));
                foodParams.add(new MethodParam(FoodConstants.SEARCH_EXPRESSION, params[0]));
                foodParams.addAll(getFixedResourceParams());

                ArrayList<FoodInfo> foods = Food.search(OAuthConstants.OAuthProto.O_AUTH_PROTO_VER1,
                        foodParams.toArray(new MethodParam[0]));

                for (int i = 0; foods != null && i < foods.size(); i++) {
                    AppLogger.getInstance().debug(foods.toString());
                }

                if(p != null) {
                    if (!p.getUserInformation())
                        AppLogger.getInstance().debug("User information not found");
                    AppLogger.getInstance().debug(p.toString());
                }
                return foods;

            } catch (Exception e) {
                AppLogger.getInstance().error(e.getMessage());
            }
            return null;
        }

        private ArrayList<MethodParam> getFixedResourceParams()
        {
            ArrayList<MethodParam> result = new ArrayList<MethodParam>();

            result.add(new MethodParam(OAuthConstants.OAUTH_CONSUMER_KEY,
                    appCompatActivity.getResources().getString(R.string.consumerKey)));

            result.add(new MethodParam(OAuthConstants.OAUTH_SHARED_KEY,
                    appCompatActivity.getResources().getString(R.string.sharedKey)));

            result.add(new MethodParam(OAuthConstants.OAUTH_NONCE, "wtf"));

            result.add(new MethodParam(OAuthConstants.OAUTH_URL,
                    appCompatActivity.getResources().getString(R.string.api_url)));

            return result;
        }
    }

    private class FoodSearchAdapter extends ArrayAdapter<FoodInfo> {
        public FoodSearchAdapter(Context context, int resource)
        {
            super(context, resource);
        }

        public FoodSearchAdapter(Context context, int resource, int textViewResourceId)
        {
            super(context, resource, textViewResourceId);
        }

        public FoodSearchAdapter(Context context, int resource, FoodInfo[] objects)
        {
            super(context, resource, objects);
        }

        public FoodSearchAdapter(Context context, int resource, int textViewResourceId,
                                 FoodInfo[] objects)
        {
            super(context, resource, textViewResourceId, objects);
        }

        public  FoodSearchAdapter(Context context, int resource, List<FoodInfo> objects)
        {
            super(context, resource, objects);
        }

        public FoodSearchAdapter(Context context, int resource, int textViewResourceId,
                                 List<FoodInfo> objects)
        {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parentGroup)
        {
            View searchView = convertView;
            if (searchView == null)
            {
                LayoutInflater searchLayout = getLayoutInflater();
                searchView = searchLayout.inflate(R.layout.search_list, null);
            }

            TextView brandName = (TextView)searchView.findViewById(R.id.topSearchBrand);
            TextView name = (TextView)searchView.findViewById(R.id.topSearchName);
            TextView topValue1 = (TextView)searchView.findViewById(R.id.topSearchVal);
            TextView topValue2 = (TextView)searchView.findViewById(R.id.topSearchVal2);

            FoodInfo item = getItem(position);

            name.setText("Name: " + item.food_name);


            if (item.isGeneric())
                brandName.setText("Generic: No Brand");
            if (item.isBrand())
                brandName.setText("Brand: " + item.brand_name);

            Pair<String, String> nutrient1 = item.getSortedNutrientAt(item.getNutrientCount()- 1);
            Pair<String, String> nutrient2 = item.getSortedNutrientAt(item.getNutrientCount() - 2);

            if (nutrient1 != null)
                topValue1.setText(nutrient1.first+ ": " + nutrient1.second);
            else
                topValue1.setVisibility(View.GONE);

            if (nutrient2 != null)
                topValue2.setText(nutrient2.first +": " + nutrient2.second);
            else
                topValue2.setVisibility(View.GONE);

            return searchView;
        }
    }
}
