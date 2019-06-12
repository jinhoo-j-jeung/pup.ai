package com.example.shepi13.testlogin.Utils;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;

/**
 * AsyncTaskLoader specific for ProfileActivity.
 * Loads the ProfileRequest function in ApiConnector class and delivers the result to ProfileActivity.
 */

public class ProfileLoader extends AsyncTaskLoader<String> {

  public static final int PROFILE_REQUEST = 4;

  private String mResult = null;
  private final Bundle args;

  public ProfileLoader(Context context, Bundle args) {
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
    return ApiConnector.profileRequest();
  }
}