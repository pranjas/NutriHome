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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Search extends AppCompatActivity{

    private  String lastSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        AppLogger.getInstance().setEnabled(true);

        Button searchButton = (Button)findViewById(R.id.btnSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtSearch = (EditText)findViewById(R.id.txtSearch);
                if (txtSearch.getText().length() != 0) {
                    Intent searchIntent = new Intent(getApplicationContext(),
                            FoodSearchResult.class);
                    searchIntent.putExtra(IntentURI.SEARCH_FOOD, txtSearch.getText().toString());
                    startActivity(searchIntent);
                    lastSearch = txtSearch.getText().toString();
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstance)
    {
        super.onSaveInstanceState(savedInstance);
        EditText txtSearch = (EditText)findViewById(R.id.txtSearch);
        savedInstance.putString(IntentURI.SEARCH_FOOD, txtSearch.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstance)
    {
        super.onRestoreInstanceState(savedInstance);
        if (savedInstance == null)
            return;
        String searchText = savedInstance.getString(IntentURI.SEARCH_FOOD, "");
        EditText txtSearch = (EditText)findViewById(R.id.txtSearch);
        txtSearch.setText(searchText);
    }



    public void onResume()
    {
        super.onResume();
        Bundle fromSearchResult = getIntent().getExtras();
        if (fromSearchResult != null) {
            String forToast ="";
            forToast = fromSearchResult.getString(IntentURI.SEARCH_NO_RESULT, "");
            if (forToast.length() != 0)
                Toast.makeText(getApplicationContext(), forToast.toString() + " " +
                lastSearch, Toast.LENGTH_LONG).show();
        }
    }

    private void getSearchResults()
    {

    }
}
