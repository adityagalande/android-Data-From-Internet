package com.example.weatherforecast.datafrominternet;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherforecast.datafrominternet.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText mET_Search_Box;
    private TextView mTV_Url_Display;
    private TextView mTV_Github_Search_Results_Json;
    private TextView mTV_Error_Message_Display;
    private ProgressBar mPB_Loading_Indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mET_Search_Box = (EditText) findViewById(R.id.et_search_box);
        mTV_Url_Display = (TextView) findViewById(R.id.tv_url_display);
        mTV_Github_Search_Results_Json = (TextView) findViewById(R.id.tv_github_search_results_json);
        mTV_Error_Message_Display = (TextView) findViewById(R.id.tv_error_message_display);
        mPB_Loading_Indicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
    }


    private void makeGithubSearchQuery() {
        String githubQuery = mET_Search_Box.getText().toString();
        URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery);
        mTV_Url_Display.setText(githubSearchUrl.toString());
        String githubSearchResults = null;
        new GithubQueryTask().execute(githubSearchUrl);
    }


    private void showJsonDataView(){
        mTV_Error_Message_Display.setVisibility(View.INVISIBLE);
        mTV_Github_Search_Results_Json.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        mTV_Github_Search_Results_Json.setVisibility(View.INVISIBLE);
        mTV_Error_Message_Display.setVisibility(View.VISIBLE);
    }


    public class GithubQueryTask extends AsyncTask<URL, Void, String> {

        // COMPLETED (2) Override the doInBackground method to perform the query. Return the results. (Hint: You've already written the code to perform the query)
        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String githubSearchResults = null;
            try {
                githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return githubSearchResults;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mPB_Loading_Indicator.setVisibility(View.VISIBLE);
        }

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        @Override
        protected void onPostExecute(String githubSearchResults) {
            if (githubSearchResults != null && !githubSearchResults.equals("")) {
                showJsonDataView();
                mTV_Github_Search_Results_Json.setText(githubSearchResults);
                mPB_Loading_Indicator.setVisibility(View.INVISIBLE);
            }else{
                showErrorMessage();
                mPB_Loading_Indicator.setVisibility(View.INVISIBLE);
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemThatWasSelected = item.getItemId();
        switch (itemThatWasSelected){
            case R.id.action_search:
//                Toast.makeText(MainActivity.this, "Search selected!", Toast.LENGTH_SHORT).show();
                makeGithubSearchQuery();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}