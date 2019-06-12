package com.example.shepi13.testlogin.Utils;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;

import java.io.IOException;

/**
 * AsyncTaskLoader specific for BreedInfoActivity.
 * Loads the MakeGetRequest function in ApiConnector class and delivers the result to BreedInfoActivity.
 */

public class BreedLoader extends AsyncTaskLoader<String> {
  private static final String BREEDNAME_KEY = "result_data";
  private static final String BREED_URL = "http://52.165.223.103:8000/breeds";
  public static final int BREED_REQUEST = 3;
  private String mResult = null;
  private final Bundle args;

  public BreedLoader(Context context, Bundle args) {
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
    String breedname = args.getString(BREEDNAME_KEY);
    String breed_url = BREED_URL + "/" + breedname;
    try {
      return ApiConnector.MakeGetRequest(breed_url);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}