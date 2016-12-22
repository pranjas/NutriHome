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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pranay.nutrihome.OAuthCommon.OAuthConstants;
import com.example.pranay.nutrihome.fatsecret.FatSecretCommons;
import com.example.pranay.nutrihome.fatsecret.Foods.Food;
import com.example.pranay.nutrihome.fatsecret.Foods.FoodConstants;
import com.example.pranay.nutrihome.fatsecret.Foods.FoodInfo;
import com.example.pranay.nutrihome.fatsecret.MethodParam;
import com.example.pranay.nutrihome.fatsecret.Profile.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FoodSearchResult extends AppCompatActivity {

    protected ProgressDialog progressBar;
    protected int currentSearchPage = -1;
    protected String currentSearchExpression = "";

    protected ArrayList<FoodInfo> currentResultSet;

    public ArrayList<FoodInfo> getCurrentResultSet()
    {
        return currentResultSet;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int searchPage = 0;
        setContentView(R.layout.activity_food_search_result);

        /*
         * No saved instance, get it again from network.
         */
        if(savedInstanceState == null) {
            AppLogger.getInstance().debug("SavedInstance is null");
            Intent fromSearchActivity = getIntent();
            if (fromSearchActivity != null) {
                currentSearchExpression = fromSearchActivity.getStringExtra(IntentURI.SEARCH_FOOD);
                if (currentSearchExpression == null)
                    currentSearchExpression = "";
                currentSearchPage = fromSearchActivity.getIntExtra(IntentURI.SEARCH_FOOD_PAGE, -1);
            }
        }
        else {
            AppLogger.getInstance().debug("Bundle is not null in onCreate");
            currentResultSet = savedInstanceState.getParcelableArrayList(IntentURI.SEARCH_FOOD);
            currentSearchExpression = savedInstanceState.getString(FoodConstants.SEARCH_EXPRESSION);
            currentSearchPage = savedInstanceState.getInt(FatSecretCommons.PAGE_NUMBER);
            searchPage = currentSearchPage;
        }
        new RequestReader().execute(currentSearchExpression,
                String.valueOf(searchPage));
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        AppLogger.getInstance().debug("Calling onSaveInstance");
        savedInstanceState.putParcelableArrayList(IntentURI.SEARCH_FOOD,
                currentResultSet);
        savedInstanceState.putString(FoodConstants.SEARCH_EXPRESSION, currentSearchExpression);
        savedInstanceState.putInt(FatSecretCommons.PAGE_NUMBER, currentSearchPage);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstance)
    {
        super.onRestoreInstanceState(savedInstance);
        AppLogger.getInstance().debug("OnRestore called....");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        AppLogger.getInstance().debug("Destroying parent activity");
    }


    private class RequestReader extends AsyncTask<String, Integer, ArrayList<FoodInfo>> {

        private FoodSearchResult appCompatActivity = FoodSearchResult.this;
        private ArrayList<MethodParam> mFoodParams = new ArrayList<>();
        Profile mProfile;
        @Override
        protected void onPreExecute()
        {
            mFoodParams.clear();
            /*
             * NEED TO ADD SEARCH_EXPRESSION and any other
             * parameters. This would be done in PRE-EXECUTE?
             */
            progressBar = ProgressDialog.show(appCompatActivity,
                    "Loading...", "Searching for " + appCompatActivity.currentSearchExpression,
                    true);
        }

        private void addProfileParams()
        {
            if (mProfile != null) {
                mFoodParams.add(new MethodParam(OAuthConstants.OAUTH_ACCESS_KEY,
                                                        mProfile.getoAuthSecret()));
                mFoodParams.add(new MethodParam(OAuthConstants.OAUTH_AUTH_TOKEN,
                                                        mProfile.getoAuthToken()));
            }
        }

        private Profile getProfile()
        {
            if (mProfile == null) {
                mProfile = Profile.createProfile("kumar.srivastava.pranay@gmail.com");
            }

            if (mProfile == null)
                mProfile = Profile.getProfileFromServer("kumar.srivastava.pranay@gmail.com");

            return mProfile;
        }

        protected LinearLayout inflateAndHookSearchListFooter()
        {
            LinearLayout searchListFooterLayout= (LinearLayout)getLayoutInflater().
                                                    inflate(R.layout.search_list_footer, null);
            AbsListView.LayoutParams footerLP = new AbsListView.LayoutParams(
                    AbsListView.LayoutParams.MATCH_PARENT,
                    AbsListView.LayoutParams.MATCH_PARENT
            );
            /*
             * TODO:
             * Need to check if we need to inflate every time?
             */
            searchListFooterLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            searchListFooterLayout.setLayoutParams(footerLP);

            Button btnNext = (Button)searchListFooterLayout.findViewById(R.id.btnNext);
            Button btnPrev = (Button)searchListFooterLayout.findViewById(R.id.btnPrev);
            btnNext.setOnClickListener(new ListViewBtnHandler());
            btnPrev.setOnClickListener(new ListViewBtnHandler());

            return searchListFooterLayout;
        }


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
            currentResultSet = result;
            final ListView listView = (ListView)findViewById(R.id.listSearchFood);


            if (listView.getAdapter() == null || listView.getAdapter().getCount() == 0)
                listView.setAdapter(new FoodSearchAdapter(FoodSearchResult.this,
                        R.layout.search_list, result));
            else {

                ArrayAdapter <FoodInfo> listAdapter =
                        (ArrayAdapter)
                                ((HeaderViewListAdapter)listView.getAdapter()).getWrappedAdapter();
                /*
                 * TODO:
                 * This might add everything?
                 */
                listAdapter.clear();
                listAdapter.addAll(result);
            }
            if (listView.getFooterViewsCount() == 0)
                listView.addFooterView(inflateAndHookSearchListFooter());

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent forFoodItemShow = new Intent(getApplicationContext(),
                            FoodItemShow.class);
                    forFoodItemShow.putExtra(IntentURI.FOOD_ITEM_SHOW, currentResultSet.get(position));
                    forFoodItemShow.putExtra(IntentURI.SEARCH_FOOD, currentSearchExpression);
                    forFoodItemShow.putExtra(IntentURI.SEARCH_FOOD_PAGE, currentSearchPage);
                    startActivity(forFoodItemShow);
                }
            });
        }

        /*
         * The arguments are ordered as, in this order,
         * @search_expression
         * @page_number
         */
        @Override
        protected ArrayList<FoodInfo> doInBackground(String... params) {
            try {

                /*
                 * Only add search expression if it's there.
                 */
                if (params.length > 0)
                    mFoodParams.add(new MethodParam(FoodConstants.SEARCH_EXPRESSION, params[0]));

                /*
                 * Only add page number if it's there. But check if are coming
                 * on the same page as the last one showed. This is useful for when
                 * activity is suspended or we return to it.
                 */
                if (params.length > 1) {
                    if (Integer.valueOf(params[1]).intValue() == currentSearchPage)
                        return currentResultSet;
                    mFoodParams.add(new MethodParam(FatSecretCommons.PAGE_NUMBER, params[1]));
                    currentSearchPage = Integer.valueOf(params[1]).intValue();
                }

                getProfile(); /* Doesn't necessarily goes to network.*/
                addProfileParams(); /*Does always sets profile params, if above call was success*/


                ArrayList<FoodInfo> foods = Food.search(OAuthConstants.OAuthProto.O_AUTH_PROTO_VER1,
                        mFoodParams.toArray(new MethodParam[0]));

                for (int i = 0; foods != null && i < foods.size(); i++) {
                    AppLogger.getInstance().debug(foods.toString());
                }

                if(mProfile != null) {
                    if (!mProfile.getUserInformation())
                        AppLogger.getInstance().debug("User information not found");
                    AppLogger.getInstance().debug(mProfile.toString());
                    AppGlobalState.setUserProfile(mProfile);
                }
                return foods;

            } catch (Exception e) {
                AppLogger.getInstance().error(e.getMessage());
            }
            return null;
        }

    }

    private class ListViewBtnHandler implements View.OnClickListener {

        private FoodSearchResult appCompatActivity = FoodSearchResult.this;
        @Override
        public void onClick(View v)
        {
            int prevPage = currentSearchPage;
            int nextPage = -1;
            if (v.getId() == R.id.btnNext)
                nextPage = prevPage + 1;

            if (v.getId() == R.id.btnPrev)
                nextPage = prevPage - 1;

            if(nextPage < 0)
                nextPage = 0;

            new RequestReader().execute(appCompatActivity.currentSearchExpression,
                        String.valueOf(nextPage));
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

        @NonNull
        private String addNewLineAtModPos(String str, int modPos)
        {
            StringBuilder sb = new StringBuilder();
            String [] words = str.split(" ");
            int spaceLeft = modPos - 1;
            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                if (word.length() < spaceLeft) {
                    sb.append(word +" ");
                    spaceLeft -= word.length()  + 1;
                }
                else {
                    sb.append("\n");
                    spaceLeft = modPos - 1;
                    i--;
                }
            }
            return sb.toString();
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

            LinearLayout graphLayout = (LinearLayout)searchView.findViewById(R.id.graphLayout);


            TextView brandName = (TextView)searchView.findViewById(R.id.topSearchBrand);
            TextView name = (TextView)searchView.findViewById(R.id.topSearchName);
            TextView topValue1 = (TextView)searchView.findViewById(R.id.topSearchVal);
            TextView topValue2 = (TextView)searchView.findViewById(R.id.topSearchVal2);


            FoodInfo item = getItem(position);
            String foodName = addNewLineAtModPos(item.food_name, 30);
            name.setText(foodName);



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

            if (graphLayout != null)
            {
                if (graphLayout.getChildCount() == 0) {
                    Random r = new Random();
                    Pair<Integer, Integer> forPie[] = new Pair[3];
                    for (int i = 0; i < forPie.length; i++) {
                        int red, green, blue;
                        red = r.nextInt() & 0xff;
                        green = r.nextInt() & 0xff;
                        blue = r.nextInt() & 0xff;
                        forPie[i] = new Pair<Integer, Integer>(
                                r.nextInt(), Color.rgb(red, green, blue));
                    }

                    PieGraph pieGraph = new PieGraph(graphLayout.getContext(), forPie,
                            graphLayout.getWidth(), graphLayout.getHeight());
                    pieGraph.index = position;
                    LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);

                    llp.setMargins(5, 5, 5, 5);

                    llp.gravity = Gravity.RIGHT;
                    pieGraph.setLayoutParams(llp);
                    graphLayout.addView(pieGraph);
                }
            }
            return searchView;
        }
    }

    private class PieGraph extends View
    {
        /*
         * Value,Color pair.
         */
        private Pair<Integer, Integer> [] colorValuePairs;
        int m_width, m_height;
        int index;
        Paint mPaint;
        public static final int MIN_WIDTH = 150, MIN_HEIGHT = 150;

        public PieGraph(Context context, Pair<Integer, Integer> [] pairs, int width, int height)
        {
            super(context);
            this.colorValuePairs = pairs;
            this.setWillNotDraw(false);
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }


        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

            // Try for a width based on our minimum

            int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth()
                    + MeasureSpec.getSize(widthMeasureSpec)/9;

            minw = Math.max(minw, PieGraph.MIN_WIDTH);
            
            int minh = MeasureSpec.getSize(heightMeasureSpec)/16
                    + getPaddingBottom() + getPaddingTop();//resolveSizeAndState(minh , heightMeasureSpec, 0);

            minh = Math.max(minh, PieGraph.MIN_HEIGHT);

            super.onMeasure(MeasureSpec.makeMeasureSpec(minw, MeasureSpec.AT_MOST)
                    , MeasureSpec.makeMeasureSpec(minh, MeasureSpec.AT_MOST));
        }

        public void onDraw(Canvas canvas)
        {
            int width = getWidth() - getPaddingLeft() - getPaddingRight();
            int height = getHeight() - getPaddingLeft() - getPaddingBottom();
            int left = getPaddingLeft();
            int top = getPaddingTop();
            int maxSum = 0;
            float startAngle = 0.0f;
            AppLogger.getInstance().debug("Position = " + index +
                    " onDraw left = "+left + " width = " + width + ", height = " + height);

            RectF canvasSize = new RectF(left, top, width, height);

            for(Pair<Integer, Integer> p : colorValuePairs) {
                maxSum +=p.first.intValue();
            }

            for(Pair<Integer, Integer> p: colorValuePairs) {
                mPaint.setColor(p.second);
                float sweep = (p.first * 360.0f) / maxSum;
                canvas.drawArc(canvasSize, startAngle, sweep,true, mPaint);
                startAngle += sweep;
            }
        }
    }
}
