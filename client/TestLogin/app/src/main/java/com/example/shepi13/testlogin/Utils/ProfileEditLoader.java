package com.example.shepi13.testlogin.Utils;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import static com.example.shepi13.testlogin.Utils.LoginRegisterLoader.LOC_KEY;
import static com.example.shepi13.testlogin.Utils.LoginRegisterLoader.NAME_KEY;

/**
 * AsyncTaskLoader specific for ProfileEditActivity.
 * Retrieves the user input data and makes patch request through ApiConnector class.
 * Runs a saparete thread to make a post request for the user profile image through ApiConnector class.
 */
@SuppressWarnings("ConstantConditions")
public class ProfileEditLoader extends AsyncTaskLoader<String> {

  private static final String profile_image_url = "http://52.165.223.103:8000/user/profile_pic";
  private String mResult = null;
  private final Bundle args;

  public ProfileEditLoader(Context context, Bundle args) {
    super(context);
    this.args = args;
  }

  @Override
  public void onStartLoading() {
    if (args == null) {
      return;
    }
    if (mResult != null) {
      deliverResult(mResult);
    } else {
      forceLoad();
    }
  }

  @Override
  public void deliverResult(String result) {
    mResult = result;
    super.deliverResult(result);
  }

  @Override
  public String loadInBackground() {
    // Gets the user profile image from the bundle and makes a new thread to make a post request
    final byte[] imageData = args.getByteArray("profile_pic");
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        try {
          ApiConnector.MakePostRequest(new URL(profile_image_url), imageData, "image/png");
        } catch (IOException e) {
          e.printStackTrace();
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

    // Gets the user input and if inputs are not null, make a patch request with the created JSONObject.
    String name = args.getString(NAME_KEY);
    String location = args.getString(LOC_KEY);
    JSONObject jsonObject;
    try {
      if (name == null && location == null) {
        jsonObject = new JSONObject();
      } else if (!name.equals("") && location.equals("")) {
        jsonObject = new JSONObject()
                .put("name", name);
      } else if (name.equals("") && !location.equals("")) {
        jsonObject = new JSONObject()
                .put("location", location);
      } else {
        jsonObject = new JSONObject()
                .put("name", name)
                .put("location", location);
      }
      String json = jsonObject.toString();
      return ApiConnector.MakePatchRequest(json.getBytes(), "application/json");
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return null;
  }
}