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
import android.widget.TextView;
import android.widget.Toast;
import com.example.shepi13.testlogin.Utils.ApiConnector;
import com.example.shepi13.testlogin.Utils.LoginRegisterLoader;

/**
 * The user can login with email and password.
 * If successfully logged in, the app receives a token from the server and saves it in the shared preference.
 * The user can also go to the register page to sign-up or go to the main page to use the app without signing in.
 */

public class LoginActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Boolean> {

  private EditText mEmailEditText, mPasswordEditText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    mEmailEditText = findViewById(R.id.et_login_email);
    mPasswordEditText = findViewById(R.id.et_login_password);
    TextView mRegisterTextView = findViewById(R.id.link_signup);
    TextView mMainTextView = findViewById(R.id.link_main);

    getLoaderManager().initLoader(LoginRegisterLoader.LOGIN_REQUEST, null, this);

    // When the register textview is clicked,
    mRegisterTextView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
      // Start the Signup activity
      Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
      startActivity(intent);
        }
    });

    // When the user chooses to use the app without signing in,
    mMainTextView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // Start the main activity
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
      }
    });
  }

  // When login button is clicked, make a post request to the server.
  public void login(@SuppressWarnings("unused") View view) {
    // Creates a bundle to pass arguments to Login loader
    Bundle loginBundle = new Bundle();

    int requestType = LoginRegisterLoader.LOGIN_REQUEST;
    loginBundle.putInt(LoginRegisterLoader.REQUEST_TYPE_KEY, requestType);
    loginBundle.putString(LoginRegisterLoader.EMAIL_KEY, mEmailEditText.getText().toString());
    loginBundle.putString(LoginRegisterLoader.PASSWORD_KEY, mPasswordEditText.getText().toString());

    // Put the user email in the shared preference for future uses.
    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
    editor.putString("email", mEmailEditText.getText().toString());
    editor.apply();

    // Initializes Login loader
    LoaderManager loaderManager = getLoaderManager();
    Loader<Boolean> loginLoader = loaderManager.getLoader(requestType);

    if(loginLoader == null) {
      loaderManager.initLoader(requestType, loginBundle, this);
    } else {
      loaderManager.restartLoader(requestType, loginBundle, this);
    }
  }

  @Override
  public Loader<Boolean> onCreateLoader(int id, Bundle args) {
    return new LoginRegisterLoader(this, args);
  }

  @Override
  public void onLoadFinished(Loader<Boolean> loader, Boolean success) {
    // If successfully logged in, save the token the shared preference and starts the main activity.
    if(success) {
      SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
      editor.putString(getString(R.string.api_token), ApiConnector.mApiToken);
      editor.apply();

      Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
      finish();
      startActivity(mainActivity);
    } else {
      Toast.makeText(this, R.string.toast_login_fail, Toast.LENGTH_LONG).show();
    }
  }

  @Override
  public void onLoaderReset(Loader<Boolean> loader) {
  }
}