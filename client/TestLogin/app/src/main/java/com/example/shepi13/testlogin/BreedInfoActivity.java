package com.example.shepi13.testlogin;

import android.app.LoaderManager;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.shepi13.testlogin.Utils.ApiConnector;
import com.example.shepi13.testlogin.Utils.BreedInfoList;
import com.example.shepi13.testlogin.Utils.BreedLoader;
import com.example.shepi13.testlogin.Utils.ProfileImageTask;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Receives parsed information about the breed user searched or clicked in ResultActivity.
 * Contains the image of the associated breed as well.
 * By clicking the star icon, the logged in user can add the breed in his/her favorite breeds.
 */

public class BreedInfoActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{
  private static final String FAV_URL = "http://52.165.223.103:8000/user/fav_breed";
  private String breedname;

  private ListView mBreedListView;
  private ImageView mPicImageView, mFavButtonImageView;
  private TextView mBreedTextView, mBreedNameTextView, mBreedGroupTextView, mBreedHeightTextView, mBreedWeightTextView, mBreedLifeTextView, mBreedHeight2TextView;
  private TextView mBreedWeight2TextView, mBreedLife2TextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_breedinfo);

    mFavButtonImageView = findViewById(R.id.bt_add_fav);
    mBreedListView = findViewById(R.id.lv_breed_info);
    mPicImageView = findViewById(R.id.iv_breed_pic);
    mBreedTextView = findViewById(R.id.tv_breed_error);

    mBreedNameTextView = findViewById(R.id.tv_breed_name);
    mBreedGroupTextView = findViewById(R.id.tv_breed_group);
    mBreedHeightTextView = findViewById(R.id.tv_breed_height);
    mBreedHeight2TextView = findViewById(R.id.tv_breed_height2);
    mBreedWeightTextView = findViewById(R.id.tv_breed_weight);
    mBreedWeight2TextView = findViewById(R.id.tv_breed_weight2);
    mBreedLifeTextView = findViewById(R.id.tv_breed_lifespan);
    mBreedLife2TextView = findViewById(R.id.tv_breed_lifespan2);

    Bundle b = getIntent().getExtras();
    assert b != null;
    breedname = b.getString("result_data");
    LoaderManager loaderManager = getLoaderManager();
    Loader<String> loader = loaderManager.getLoader(BreedLoader.BREED_REQUEST);
    if(loader == null) {
      loaderManager.initLoader(BreedLoader.BREED_REQUEST, b, this);
    }
    else {
      loaderManager.restartLoader(BreedLoader.BREED_REQUEST, b, this);
    }
  }

  // When the star icon is clicked, check whether the user is logged in.
  // If logged in, sends a post request to the server to add the breed into the user's favorite breeds
  public void addFavorite(@SuppressWarnings("unused") View view) {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    final String token = sharedPreferences.getString("token", "");
    // If not logged in, displays the message.
    if(token.equals("")) {
      Toast.makeText(this, R.string.toast_info_favfailed, Toast.LENGTH_LONG).show();
    }
    // If logged in, sends a post request
    else {
      Thread t = new Thread() {
        @Override
        public void run() {
          try {
            JSONObject jsonObject = new JSONObject();
            try {
              jsonObject.put("Authorization", "Token " + ApiConnector.mApiToken);
              jsonObject.put("name", breedname);
            } catch (JSONException e) {
              e.printStackTrace();
            }
            byte[] data = jsonObject.toString().getBytes();
            ApiConnector.MakePostRequest(new URL(FAV_URL), data, "application/json");
          } catch (IOException e)
          {
            e.printStackTrace();
          }
        }
      };
      t.start();
      try {
        t.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      Toast.makeText(this, R.string.toast_info_favsuccess, Toast.LENGTH_LONG).show();
    }
  }

  @Override
  public Loader<String> onCreateLoader(int i, Bundle bundle) {
    return new BreedLoader(this, bundle);
  }

  @Override
  public void onLoadFinished(Loader<String> loader, String s) {
    if (s == null || s.length() <= 2) {
      String errmsg = getString(R.string.tv_info_nodata, breedname);
      mBreedTextView.setText(errmsg);
      mBreedTextView.setVisibility(View.VISIBLE);
      mFavButtonImageView.setVisibility(View.INVISIBLE);
    }
    // The server returns a jsonobject instead of an jsonarray. Make this jsonobject into a string array
    else {
      s = s.substring(2, s.length() - 2);
      s = s.replaceAll("\"", "");
      String[] breed_info = s.split(",");

      final ArrayList<String> arr_info = new ArrayList<>();

      // Sets the textviews and the image view. Put the other information for the listview in the ArrayList
      for (String aBreed_info : breed_info) {
        if (aBreed_info.contains("url")) {
          int url_idx = aBreed_info.indexOf(":");
          String img_url = aBreed_info.substring(url_idx + 1);
          new ProfileImageTask(mPicImageView, null).execute(img_url);
        } else if (aBreed_info.contains("name")) {
          int idx = aBreed_info.indexOf(":");
          breedname = aBreed_info.substring(idx + 1);
          mBreedNameTextView.setText(breedname);
        } else if (aBreed_info.contains("breed_group")) {
          int idx = aBreed_info.indexOf(":");
          mBreedGroupTextView.setText(aBreed_info.substring(idx + 1));
        } else if (aBreed_info.contains("height")) {
          int idx = aBreed_info.indexOf(":");
          if (aBreed_info.contains("to")) {
            int idx2 = aBreed_info.indexOf("to");
            mBreedHeightTextView.setText(R.string.tv_info_category1);
            mBreedHeight2TextView.setText(aBreed_info.substring(idx + 1, idx2 - 1));
          } else {
            mBreedHeightTextView.setText(R.string.tv_info_category1);
            mBreedHeight2TextView.setText(aBreed_info.substring(idx + 1));
          }
        } else if (aBreed_info.contains("weight")) {
          int idx = aBreed_info.indexOf(":");
          mBreedWeightTextView.setText(R.string.tv_info_category2);
          String weight_in_unit = getString(R.string.tv_info_weight_unit, aBreed_info.substring(idx + 1));
          mBreedWeight2TextView.setText(weight_in_unit);
          //mBreedWeight2TextView.setText(aBreed_info.substring(idx + 1) + R.string.tv_info_weight_unit);
        } else if (aBreed_info.contains("life")) {
          int idx = aBreed_info.indexOf(":");
          mBreedLifeTextView.setText(R.string.tv_info_category3);
          mBreedLife2TextView.setText(aBreed_info.substring(idx + 1));
        } else if (!aBreed_info.contains("inch") && !aBreed_info.contains("inches") && !aBreed_info.contains("id")) {
          arr_info.add(aBreed_info);
        }
      }

      final Integer[] imageId = new Integer[arr_info.size()];
      final String[] category = new String[arr_info.size()];

      // Sets categories and sets the particular icon images to display levels of these categories
      for(int i = 0; i < arr_info.size(); i++) {
        String info = arr_info.get(i);
        int idx = info.indexOf(":");
        String c = info.substring(0, idx);
        c = c.replaceAll("_", " ");

        // "stranger friendliness is too long. shorten it.
        if(c.contains("stranger friendliness")){
          c = "stranger friendly";
        }
        category[i] = c+":";

        int v = Integer.parseInt(info.substring(idx+1, info.length()).replace("}", ""));
        if(v == 1) {
          imageId[i] = R.drawable.breed1;
        }
        else if(v == 2) {
          imageId[i] = R.drawable.breed2;
        }
        else if(v == 3) {
          imageId[i] = R.drawable.breed3;
        }
        else if(v == 4) {
          imageId[i] = R.drawable.breed4;
        }
        else if(v == 5) {
          imageId[i] = R.drawable.breed5;
        }
        else {
          imageId[i] = R.drawable.pup_icon3;
          Log.d("Unexpected", String.valueOf(i));
        }
      }
      final BreedInfoList arrayAdapter = new BreedInfoList(BreedInfoActivity.this, category, imageId);
      mBreedListView.setAdapter(arrayAdapter);
    }
  }

  @Override
  public void onLoaderReset(Loader<String> loader) {
  }
}