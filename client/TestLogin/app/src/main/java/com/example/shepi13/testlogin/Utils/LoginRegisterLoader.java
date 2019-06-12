package com.example.shepi13.testlogin.Utils;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;

/**
 * AsyncTaskLoader for LoginActivity and RegisterActivity.
 * Loads the login and register method in ApiConnector class and delivers the result to one of the activities.
 */

public class LoginRegisterLoader extends AsyncTaskLoader<Boolean> {
  public static final String REQUEST_TYPE_KEY = "request";
  public static final String EMAIL_KEY = "email";
  public static final String PASSWORD_KEY = "password";
  public static final String NAME_KEY = "name";
  public static final String LOC_KEY = "location";
  public static final int LOGIN_REQUEST = 1;
  public static final int SIGNUP_REQUEST = 2;

  private Boolean mResult = null;
  private final Bundle args;

  public LoginRegisterLoader(Context context, Bundle args) {
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
  public void deliverResult(Boolean result) {
    mResult = result;
    super.deliverResult(result);
  }

  @Override
  public Boolean loadInBackground() {
    String email = args.getString(EMAIL_KEY);
    String password = args.getString(PASSWORD_KEY);
    if (args.getInt(REQUEST_TYPE_KEY) == LOGIN_REQUEST) {
      return ApiConnector.login(email, password);
    } else if (args.getInt(REQUEST_TYPE_KEY) == SIGNUP_REQUEST) {
      String name = args.getString(NAME_KEY);
      String location = args.getString(LOC_KEY);
      return ApiConnector.register(email, password, name, location);
    }
    return false;
  }
}
