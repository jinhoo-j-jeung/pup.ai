package com.example.shepi13.testlogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by jinhoo on 18. 4. 18.
 * Displays the title of the app and checks whether the user is logged in whenever the app reopens.
 */

public class SplashActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    // Checks whether the user is logged in or not by the token save in the shared preference
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    final String token = sharedPreferences.getString("token", "");
    Thread t = new Thread() {
      public void run() {
      try {
        // Time to display the splash screen
        sleep(3000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      // If not logged in, go to LoginActivity
      if(token.equals("")) {
        Intent login_intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(login_intent);
        finish();
      }

      // If logged in, go straight to MainActivity
      else {
        Intent main_intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(main_intent);
        finish();
      }
      }
    };
    t.start();
  }
}
