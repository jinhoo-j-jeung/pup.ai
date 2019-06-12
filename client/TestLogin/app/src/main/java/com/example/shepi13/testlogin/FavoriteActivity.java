package com.example.shepi13.testlogin;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.example.shepi13.testlogin.Utils.ApiConnector;
import com.example.shepi13.testlogin.Utils.FavList;
import com.example.shepi13.testlogin.Utils.ProfileLoader;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The user can view the favorite breeds that he/she has saved before.
 * By clicking the item of the list, the user is sent to BreedInfoActivity with the associated breed.
 * By long-clicking the item of the list, the user can delete the breed from his/her favorite list.
 */

public class FavoriteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

  private ListView mFavListView;
  private TextView mTitleTextView, mExpTextView, mEmptyTextView;
  private FavList arrAdapter;
  private ArrayList<String> fav_arr;

  private byte[] delete_data;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_favorite);

    mTitleTextView = findViewById(R.id.tv_fav_title);
    mExpTextView = findViewById(R.id.tv_fav_exp);
    mEmptyTextView = findViewById(R.id.tv_fav_empty);
    mFavListView = findViewById(R.id.lv_favs);

    Bundle favBundle = new Bundle();
    LoaderManager loaderManager = getLoaderManager();
    Loader<String> favLoader = loaderManager.getLoader(4);

    if (favLoader == null) {
      loaderManager.initLoader(4, favBundle, this);
    } else {
      loaderManager.restartLoader(4, favBundle, this);
    }
  }

  @Override
  public Loader<String> onCreateLoader(int i, Bundle bundle) {
    return new ProfileLoader(this, bundle);
  }

  @Override
  public void onLoadFinished(Loader<String> loader, final String data) {
    if (data != null) {
      try {
        final JSONObject json = new JSONObject(data);
        // If an empty array is returned from the server, displays the message.
        if(json.getString("favorite_breeds").equals("[]")) {
          mTitleTextView.setVisibility(View.INVISIBLE);
          mExpTextView.setVisibility(View.INVISIBLE);
          mFavListView.setVisibility(View.INVISIBLE);
          mEmptyTextView.setVisibility(View.VISIBLE);
          mEmptyTextView.setText(R.string.favact_empty);
        }
        else {
          // Creates the list of the favorite breeds using the data received from the server.
          final String[] fav = (json.getString("favorite_breeds")).split(",");
          fav_arr = new ArrayList<>();
          for (String aFav : fav) {
            String s = aFav.replaceAll("\"", "");
            int s_idx = s.indexOf(':');
            int f_idx = s.indexOf('}');
            s = s.substring(s_idx + 1, f_idx);
            fav_arr.add(s);
          }
          arrAdapter = new FavList(this, fav_arr);
          mFavListView.setAdapter(arrAdapter);

          // If the item in the list is clicked, sends the user to BreeedInfoActivity with the associated breed
          mFavListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String selected_breed = arrAdapter.getItem(i);
            Intent new_activity = new Intent(FavoriteActivity.this, BreedInfoActivity.class);
            new_activity.putExtra("result_data", selected_breed);
            startActivity(new_activity);
            }
          });

          // If the item is long-clilcked, make a delete request to the server and updates the arrayadapter.
          mFavListView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i,
                long l) {
           Runnable runnable = new Runnable() {
            @Override
            public void run() {
              if (!arrAdapter.isEmpty()) {
                final String selected_breed = fav_arr.get(i);
                fav_arr.remove(i);
                try {
                  JSONObject jsonObject = new JSONObject().put("name", selected_breed);
                  String json = jsonObject.toString();
                  delete_data = json.getBytes();
                } catch (JSONException e) {
                  e.printStackTrace();
                }

                String fav_url = "http://52.165.223.103:8000/user/fav_breed";
                try {
                  ApiConnector.MakeDeleteRequest(fav_url, delete_data, "application/json");
                } catch (IOException e) {
                  e.printStackTrace();
                }
              }
            }
           };

          Thread t = new Thread(runnable);
          t.start();
          try {
            t.join();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          arrAdapter.notifyDataSetChanged();

          // If all the items are removed, displays the no favorite breed message.
          if(fav_arr.size() == 0){
            mTitleTextView.setVisibility(View.INVISIBLE);
            mExpTextView.setVisibility(View.INVISIBLE);
            mFavListView.setVisibility(View.INVISIBLE);
            mEmptyTextView.setVisibility(View.VISIBLE);
            mEmptyTextView.setText(R.string.favact_empty);
          }

          return true;
            }
          });
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void onLoaderReset(Loader<String> loader) {}
}