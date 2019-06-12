package com.example.shepi13.testlogin;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.shepi13.testlogin.Utils.ApiConnector;
import com.example.shepi13.testlogin.Utils.ProfileImageTask;
import com.example.shepi13.testlogin.Utils.ProfileLoader;
import org.json.JSONObject;

/**
 * The user can view his/her profile and can go to the profile edit page to edit contents of the profile..
 */

public class ProfileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

  private ImageView mProfileImageView;
  private ProgressBar mProgressBar;
  private TextView mNameTextView, mEmailTextView, mLocationTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);

    mNameTextView = findViewById(R.id.tv_profile_name);
    mProfileImageView = findViewById(R.id.iv_profile);
    mEmailTextView = findViewById(R.id.tv_profile_email);
    mLocationTextView = findViewById(R.id.tv_profile_location);
    mProgressBar = findViewById(R.id.pb_profile);

    Bundle profileBundle = new Bundle();
    LoaderManager loaderManager = getLoaderManager();
    Loader<String> profileLoader = loaderManager.getLoader(ProfileLoader.PROFILE_REQUEST);

    if (profileLoader == null) {
      loaderManager.initLoader(ProfileLoader.PROFILE_REQUEST, profileBundle, this);
    } else {
      loaderManager.restartLoader(ProfileLoader.PROFILE_REQUEST, profileBundle, this);
    }
  }

  // When Edit button is clicked, simply opens ProfileEditActivity.
  public void EditProfile(@SuppressWarnings("unused") View view) {
    Intent editprofileactivity = new Intent(ProfileActivity.this, ProfileEditActivity.class);
    startActivity(editprofileactivity);

  }

  // When the user presses back button at ProfileEditActivity, refreshes ProfileActivity
  @Override
  public void onRestart() {
    super.onRestart();
    finish();
    startActivity(getIntent());
  }

  // Displays the option menu
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    final MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_profile, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem menuItem) {
    if (menuItem.getItemId() == R.id.action_profile_logout) {
      ApiConnector.mApiToken = "";
      SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
      editor.putString(getString(R.string.api_token), ApiConnector.mApiToken);
      editor.apply();
      finish();
      onBackPressed();
      return true;
    } else if (menuItem.getItemId() == R.id.action_profile_fav) {
      Intent favIntent = new Intent(ProfileActivity.this, FavoriteActivity.class);
      startActivity(favIntent);
    } else if (menuItem.getItemId() == R.id.action_profile_friend) {
      Intent favIntent = new Intent(ProfileActivity.this, FriendActivity.class);
      startActivity(favIntent);
    }
    return super.onOptionsItemSelected(menuItem);
  }

  @Override
  public Loader<String> onCreateLoader(int i, Bundle bundle) {
    return new ProfileLoader(this, bundle);
  }

  // When the acticity is created, it makes a get request to the server to retrieve the user data.
  // After successfully receiving the data, it sets texts for textviews accrodingly.
  @Override
  public void onLoadFinished(Loader<String> loader, final String data) {
    try {
      final JSONObject json = new JSONObject(data);

      // Extracts the image url and displays the image
      String imageURL = json.getString("image");
      new ProfileImageTask(mProfileImageView, mProgressBar).execute(imageURL);

      // The user data received from the server does not contain email. Gets the user email from the shared preference.
      SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
      final String email = sharedPreferences.getString("email", "");

      mNameTextView.setText(json.getString("name"));
      mEmailTextView.setText(email);
      mLocationTextView.setText(json.getString("location"));
    } catch (Exception e) {
      mProgressBar.setVisibility(View.INVISIBLE);
      mProfileImageView.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void onLoaderReset(Loader<String> loader) {
  }
}