package com.example.shepi13.testlogin;

import static com.example.shepi13.testlogin.Utils.LoginRegisterLoader.LOC_KEY;
import static com.example.shepi13.testlogin.Utils.LoginRegisterLoader.NAME_KEY;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.shepi13.testlogin.Utils.ProfileEditLoader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * The user can edit his/her profile.
 * For the user's profile image, the user can take a photo or can select a photo from the gallery.
 * The user can change his/her name and location.
 */

public class ProfileEditActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

  private static final int REQUEST_IMAGE_CAPTURE = 1;
  private static final int REQUEST_IMAGE_RETRIEVAL = 2;
  private static final String IMAGE_DATA_KEY = "image";

  private ImageView mPicEditImageView;
  private EditText mNameEditEditText, mLocEditEditText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit);

    mPicEditImageView = findViewById(R.id.iv_editpic);
    mNameEditEditText = findViewById(R.id.et_edit_name);
    mLocEditEditText = findViewById(R.id.et_edit_location);

    LoaderManager loaderManager = getLoaderManager();
    Loader<String> profileEditLoader = loaderManager.getLoader(5);

    if (profileEditLoader == null) {
      loaderManager.initLoader(5, null, this);
    }
    if (savedInstanceState != null) {
      byte[] imageData = savedInstanceState.getByteArray(IMAGE_DATA_KEY);

      if (imageData != null) {
        Log.d("Location", "Restoring savedInstanceState");
        Bitmap bmp = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        mPicEditImageView.setImageBitmap(bmp);
      } else {
        Log.d("LoadingError", "Error loading imageData");
      }
    }
  }

  // Displays the image previously loaded from the user camera or the user gallery.
  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);

    byte[] imageData = extractImageData();
    outState.putByteArray(IMAGE_DATA_KEY, imageData);
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
        mPicEditImageView.setImageBitmap(image);
      }
    }
  }
  // If the image is selected through taking a picture or browing gallery, it sets the imageview with the image.
  private void handleImageRetrieval(Uri uri) {
    InputStream imageStream = null;
    try {
      imageStream = getContentResolver().openInputStream(uri);
      Bitmap image = BitmapFactory.decodeStream(imageStream);

      if(image != null) {
        mPicEditImageView.setImageBitmap(image);
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

  // Helper function that extracts the drawable image to byte array and compresses it.
  private byte[] extractImageData() {
    BitmapDrawable drawable = (BitmapDrawable) mPicEditImageView.getDrawable();
    Bitmap compressedImage = drawable.getBitmap();
    ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
    compressedImage.compress(CompressFormat.JPEG, 50, imageStream);
    return imageStream.toByteArray();
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

  // When submit button is clicked, calls peofileEditLoader to handle the request.
  public void EditSubmit(@SuppressWarnings("unused") View view) {
    // puts the user inputs inside the bundle to pass them to profile edit Loader
    Bundle profileEditBundle = new Bundle();
    profileEditBundle.putString(NAME_KEY, mNameEditEditText.getText().toString());
    profileEditBundle.putString(LOC_KEY, mLocEditEditText.getText().toString());

    byte[] imageData = extractImageData();
    profileEditBundle.putByteArray("profile_pic", imageData);

    LoaderManager loaderManager = getLoaderManager();
    Loader<String> profileEditLoader = loaderManager.getLoader(5);

    if(profileEditLoader == null) {
      loaderManager.initLoader(5, profileEditBundle, this);
    } else {
      loaderManager.restartLoader(5, profileEditBundle, this);
    }
  }

  @Override
  public Loader<String> onCreateLoader(int i, Bundle bundle) {
    return new ProfileEditLoader(this, bundle);
  }

  @Override
  public void onLoadFinished(Loader<String> loader, String s) {
    if(s != null) {
      Toast.makeText(this, R.string.toast_edit_success, Toast.LENGTH_LONG).show();
    }
    else {
      Toast.makeText(this, R.string.toast_edit_fail, Toast.LENGTH_LONG).show();
    }
  }

  @Override
  public void onLoaderReset(Loader<String> loader) {
  }
}