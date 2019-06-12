package com.example.shepi13.testlogin.Utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Loads the actual image using the image url that was received from the server in bacckground.
 */

@SuppressLint("StaticFieldLeak")
public class ProfileImageTask extends AsyncTask<String, Void, Bitmap> {

  private final ImageView mImageView;
  private final ProgressBar mProgressBar;

  public ProfileImageTask(ImageView imageView, ProgressBar progressBar) {
    mImageView = imageView;
    mProgressBar = progressBar;
  }

  // Loads the bitmap image using the image url
  @Override
  protected Bitmap doInBackground(String... urls) {
    Bitmap image = null;
    try {
      InputStream inputStream = new URL(urls[0]).openStream();
      image = BitmapFactory.decodeStream(inputStream);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return image;
  }

  // Once successfully loaded, make the progressbar invisible and make the image view visible.
  @Override
  protected void onPostExecute(Bitmap image) {
    mImageView.setImageBitmap(image);
    if (mProgressBar != null) {
      mProgressBar.setVisibility(View.INVISIBLE);
      mImageView.setVisibility(View.VISIBLE);
    }
  }
}