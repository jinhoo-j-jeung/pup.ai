package com.example.shepi13.testlogin;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shepi13.testlogin.Utils.ApiConnector;
import com.example.shepi13.testlogin.Utils.LoginRegisterLoader;

/**
 * The user can reigster with email, password, name and location.
 * If successfully registered, the app receives a token from the server and saves it in the shared preference.
 * When successfully registered, the user is sent to the main activity as logged in.
 */

public class RegisterActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Boolean> {

  private EditText mEmailEditText, mPasswordEditText, mUsernameEditText, mLocationEditText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);

    mEmailEditText = findViewById(R.id.et_register_email);
    mPasswordEditText = findViewById(R.id.et_register_password);
    mUsernameEditText = findViewById(R.id.et_name);
    mLocationEditText = findViewById(R.id.et_location);

    getLoaderManager().initLoader(LoginRegisterLoader.SIGNUP_REQUEST, null, this);
  }

  // When submit button is clicked, make a post request to the server.
  public void register(@SuppressWarnings("unused") View view) {
    // Creates a bundle to pass arguments to Login loader
    Bundle registerBundle = new Bundle();

    int requestType = LoginRegisterLoader.SIGNUP_REQUEST;
    registerBundle.putInt(LoginRegisterLoader.REQUEST_TYPE_KEY, requestType);
    registerBundle.putString(LoginRegisterLoader.EMAIL_KEY, mEmailEditText.getText().toString());
    registerBundle.putString(LoginRegisterLoader.PASSWORD_KEY, mPasswordEditText.getText().toString());
    registerBundle.putString(LoginRegisterLoader.NAME_KEY, mUsernameEditText.getText().toString());
    registerBundle.putString(LoginRegisterLoader.LOC_KEY, mLocationEditText.getText().toString());

    // Put the user email in the shared preference for future uses.
    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
    editor.putString("email", mEmailEditText.getText().toString());
    editor.apply();

    // Initializes Register loader
    LoaderManager loaderManager = getLoaderManager();
    Loader<Boolean> registerLoader = loaderManager.getLoader(requestType);

    if(registerLoader == null) {
      loaderManager.initLoader(requestType, registerBundle, this);
    } else {
      loaderManager.restartLoader(requestType, registerBundle, this);
    }
  }

  @Override
  public Loader<Boolean> onCreateLoader(int id, Bundle args) {
    return new LoginRegisterLoader(this, args);
  }

  @Override
  public void onLoadFinished(Loader<Boolean> loader, Boolean success) {
    // If successfully registered, save the token the shared preference and starts the main activity.
    if(success) {
      SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
      editor.putString(getString(R.string.api_token), ApiConnector.mApiToken);
      editor.apply();

      Intent mainActivity = new Intent(RegisterActivity.this, MainActivity.class);
      finish();
      startActivity(mainActivity);
    } else {
      Toast.makeText(this, R.string.toast_register_fail, Toast.LENGTH_LONG).show();
    }
  }

  @Override
  public void onLoaderReset(Loader<Boolean> loader) {
  }
}