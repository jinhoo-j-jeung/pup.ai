package com.example.shepi13.testlogin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shepi13.testlogin.Utils.CustomList;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Receives the classification data from the server and simply displays it.
 * When an item in the result list is clicked, goes to BreedInfoActivity to display information about the associated breed.
 * If the classfication result is 'not a dog', displays 'Not a dog' message.
 */

public class ResultActivity extends AppCompatActivity{

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_result);

    ListView mResultListView = findViewById(R.id.lv_result);
    TextView mResultExpTextView = findViewById(R.id.tv_result_exp);
    ImageView mNotdogImageView = findViewById(R.id.iv_result_notdog);
    TextView mNotdogTextView = findViewById(R.id.tv_result_notdog);

    Bundle b = getIntent().getExtras();
    assert b != null;
    String result = b.getString("result_data");

    try {
      JSONObject json = new JSONObject(result);
      Boolean isDog = json.getBoolean("isdog");

      // Checks whether the pic is dog or not
      if(isDog) {
        // Since there are two JSONarrays without names, use string manipulation to extract data
        String result_data = json.getString("data");
        int name_s = result_data.indexOf('[');
        int name_f = result_data.indexOf(']');
        int prob_s = result_data.indexOf('[', name_s+1);
        int prob_f = result_data.indexOf(']', name_f+1);

        String names = result_data.substring(name_s+1, name_f);
        String probs = result_data.substring(prob_s+1, prob_f);

        names = names.replaceAll("\'", "");
        probs = probs.replaceAll("\'", "");

        final String[] arr_dogs = names.split(",");
        String[] arr_probs = probs.split(",");

        for(int i = 0; i < arr_dogs.length; i++){
          arr_dogs[i] = arr_dogs[i].trim();
        }

        for(int i = 0; i < arr_probs.length; i++){
          arr_probs[i] = Float.toString(Math.round(Float.parseFloat(arr_probs[i])*100))+"%";
        }

        final Integer[] imageId = {
                R.drawable.pup_icon4,
                R.drawable.pup_icon4,
                R.drawable.pup_icon4
        };

        final CustomList arrayAdapter = new CustomList(ResultActivity.this, arr_dogs, arr_probs, imageId);

        // Sets the header for the result list
        TextView textView = new TextView(this);
        textView.setTextSize(25);
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundColor(Color.DKGRAY);
        textView.setText(R.string.resultact_header);
        mResultListView.addHeaderView(textView);

        mResultListView.setAdapter(arrayAdapter);

        // If an item in the list is clicked, goes to BreedInfoActivity with the associated breed name.
        mResultListView.setOnItemClickListener(new OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String selected_breed = arr_dogs[i-1];
            Intent new_activity = new Intent(ResultActivity.this, BreedInfoActivity.class);
            new_activity.putExtra("result_data", selected_breed);
            startActivity(new_activity);
          }
        });
      }
      // If not a dog, displays the not a dog message only.
      else {
        mResultExpTextView.setVisibility(View.INVISIBLE);
        mResultListView.setVisibility(View.INVISIBLE);
        mNotdogImageView.setVisibility(View.VISIBLE);
        mNotdogTextView.setVisibility(View.VISIBLE);
        mNotdogTextView.setText(R.string.resultact_notdog);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
}