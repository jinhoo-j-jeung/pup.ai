package com.example.shepi13.testlogin;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build.VERSION_CODES;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shepi13.testlogin.Utils.ApiConnector;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * The user can either take a photo or send a photo to get it classified.
 * The user can also direclty search a breed through a search bar.
 */

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>,
        SharedPreferences.OnSharedPreferenceChangeListener {
  private static final int REQUEST_IMAGE_CAPTURE = 1;
  private static final int REQUEST_IMAGE_RETRIEVAL = 2;
  private static final String IMAGE_DATA_KEY = "image";
  private static final int SEND_PHOTO_LOADER = 1;

  //storage permission code
  private static final int STORAGE_PERMISSION_CODE = 746;

  //views and buttons
  private ImageView mImagePreview;
  private Button mSubmitButton;
  private ProgressBar spinner;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mImagePreview = findViewById(R.id.iv_main);
    mSubmitButton = findViewById(R.id.bt_submit_main);
    spinner = findViewById(R.id.main_progressBar);

    // Asks for a permission if permission does not exist
    requestStoragePermission();

    getLoaderManager().initLoader(SEND_PHOTO_LOADER, null, this);

    if (savedInstanceState != null) {
      byte[] imageData = savedInstanceState.getByteArray(IMAGE_DATA_KEY);

      if (imageData != null) {
          Bitmap bmp = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
          mImagePreview.setImageBitmap(bmp);
      }
    }

    //Shared preferences
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    ApiConnector.mApiToken = sharedPreferences.getString(getString(R.string.api_token), "");
    sharedPreferences.registerOnSharedPreferenceChangeListener(this);
  }

  // When MainActivity is reloaded, makes the spinner invisible.
  @Override
  public void onRestart() {
    super.onRestart();
    spinner.setVisibility(View.INVISIBLE);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    invalidateOptionsMenu();
  }

  // Displays the image previously loaded from the user camera or the user gallery.
  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    byte[] imageData = extractImageData();
    outState.putByteArray(IMAGE_DATA_KEY, imageData);
  }

  // Creates an option that navigates to other activities.
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    final MenuInflater inflater = getMenuInflater();

    // Search bar
    inflater.inflate(R.menu.menu_search, menu);

    // If not logged in, display menu with logout only
    if(ApiConnector.mApiToken == null || ApiConnector.mApiToken.equals("")) {
        inflater.inflate(R.menu.menu_main_anon, menu);
    } else {
        inflater.inflate(R.menu.menu_main, menu);
    }

    SearchView mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
    mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        Intent new_activity = new Intent(MainActivity.this, BreedInfoActivity.class);
        new_activity.putExtra("result_data", query);
        // Multiple instances bug fixed: https://stackoverflow.com/questions/9055052/back-button-reopens-the-activity
        new_activity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(new_activity);
        return true;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
            return false;
        }
    });
    return true;
  }

  // Methods for option items
  @Override
  public boolean onOptionsItemSelected(MenuItem menuItem) {
    if(menuItem.getItemId() == R.id.action_login) {
      Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
      startActivity(loginIntent);
      return true;
    } else if(menuItem.getItemId() == R.id.action_logout) {
      ApiConnector.mApiToken = "";
      SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
      editor.putString(getString(R.string.api_token), ApiConnector.mApiToken);
      editor.apply();
      return true;
    } else if(menuItem.getItemId() == R.id.action_profile) {
      Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
      startActivity(profileIntent);
      return true;
    } else if(menuItem.getItemId() == R.id.action_fav) {
      Intent favIntent = new Intent(MainActivity.this, FavoriteActivity.class);
      startActivity(favIntent);
    } else if(menuItem.getItemId() == R.id.action_friend) {
      Intent favIntent = new Intent(MainActivity.this, FriendActivity.class);
      startActivity(favIntent);
    }
    return super.onOptionsItemSelected(menuItem);
  }

  // When take picture button is clicked, it opens the camera intent.
  public void takePicture(@SuppressWarnings("unused") View view) {
    Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if(captureIntent.resolveActivity(getPackageManager()) != null)
      startActivityForResult(captureIntent, REQUEST_IMAGE_CAPTURE);
  }

  // When browse gallery button is clicked, it opens the gallery intent
  public void browseGallery(@SuppressWarnings("unused") View view) {
    Intent browseIntent = new Intent(Intent.ACTION_GET_CONTENT);
    browseIntent.setType("image/*");
    if(browseIntent.resolveActivity(getPackageManager()) != null)
      startActivityForResult(browseIntent, REQUEST_IMAGE_RETRIEVAL);
  }

  // When submit button is clicked,
  public void submit(@SuppressWarnings("unused") View view) {
    spinner.setVisibility(View.VISIBLE);
    byte[] imageData = extractImageData();

    Bundle photoBundle = new Bundle();
    photoBundle.putByteArray(IMAGE_DATA_KEY, imageData);

    LoaderManager loaderManager = getLoaderManager();
    Loader<String> sendPhotoLoader = loaderManager.getLoader(SEND_PHOTO_LOADER);

    if(sendPhotoLoader == null) {
      loaderManager.initLoader(SEND_PHOTO_LOADER, photoBundle, this);
    } else {
      loaderManager.restartLoader(SEND_PHOTO_LOADER, photoBundle, this);
    }
  }

  // Helper function that extracts the drawable image to byte array and compresses it.
  private byte[] extractImageData() {
    BitmapDrawable drawable = (BitmapDrawable) mImagePreview.getDrawable();
    Bitmap compressedImage = drawable.getBitmap();
    ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
    compressedImage.compress(CompressFormat.JPEG, 50, imageStream);
    return imageStream.toByteArray();
  }

  @Override
  public void onActivityResult(int request, int result, Intent data) {
    if(request == REQUEST_IMAGE_CAPTURE && result == RESULT_OK && data != null) {
        handleImageCapture(data.getExtras());
    } else if (request == REQUEST_IMAGE_RETRIEVAL && result == RESULT_OK && data != null) {
        handleImageRetrieval(data.getData());
    }
  }

  // If the image is selected through taking a picture or browing gallery, it sets the imageview with the image.
  private void handleImageCapture(Bundle data) {
    if(data != null) {
      Bitmap image = (Bitmap) data.get("data");

      if(image != null) {
        mImagePreview.setImageBitmap(image);
        mSubmitButton.setClickable(true);
        mSubmitButton.setVisibility(View.VISIBLE);
      }
    }
  }

  private void handleImageRetrieval(Uri uri) {
    InputStream imageStream = null;

    try {
      imageStream = getContentResolver().openInputStream(uri);
      Bitmap image = BitmapFactory.decodeStream(imageStream);

      if(image != null) {
        mImagePreview.setImageBitmap(image);
        mSubmitButton.setClickable(true);
        mSubmitButton.setVisibility(View.VISIBLE);
      }
  } catch (FileNotFoundException e) {
      e.printStackTrace();
  } finally {
      try {
        if (imageStream != null)
            imageStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public Loader<String> onCreateLoader(int id, Bundle args) {
      return new MainLoader(this, args);
  }

  @Override
  public void onLoadFinished(Loader<String> loader, String data) {
    if(data != null) {
      Intent new_activity = new Intent(MainActivity.this, ResultActivity.class);
      new_activity.putExtra("result_data", data);
      startActivity(new_activity);
    } else {
      Toast.makeText(this, R.string.toast_main_error, Toast.LENGTH_LONG).show();
    }
  }

  @Override
  public void onLoaderReset(Loader<String> loader) {}

  /**
   * AsyncTaskLoader specific for MainActivity.
   * Loads sendphoto method in ApiConnector class and delivers the result to MainActivity.
   */
  static class MainLoader extends AsyncTaskLoader<String> {
    private String mSubmitResults;
    private final Bundle args;

    MainLoader(Context context, Bundle args) {
      super(context);
      this.args = args;
    }
    @Override
    public void onStartLoading() {
      if(args == null) {
        return;
      }
      if(mSubmitResults != null) {
        deliverResult(mSubmitResults);
      } else {
        forceLoad();
      }
    }

    @Override
    public String loadInBackground() {
      byte[] imageData = args.getByteArray(IMAGE_DATA_KEY);
      return ApiConnector.sendPhoto(imageData);
    }

    @Override
    public void deliverResult(String submitResults) {
      mSubmitResults = submitResults;
      super.deliverResult(submitResults);
    }
  }

  /*
  referred to https://www.simplifiedcoding.net/android-marshmallow-permissions-example/
   */
  //Permission Request
  @TargetApi(VERSION_CODES.JELLY_BEAN)
  private void requestStoragePermission() {
    // If permission already granted, do nothing.
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
      return;

    // If permission was previously denied
    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
        Toast.makeText(this, R.string.toast_main_perm3, Toast.LENGTH_LONG).show();
    }
    // Ohterwise ask for permission.
    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
  }

  //Result after request permission
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[	] grantResults) {
    // Request code is 746
    if (requestCode == STORAGE_PERMISSION_CODE) {
      //If granted
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        Toast.makeText(this, R.string.toast_main_perm1, Toast.LENGTH_LONG).show();
      } else {
        Toast.makeText(this, R.string.toast_main_perm2, Toast.LENGTH_LONG).show();
      }
    }
  }
}