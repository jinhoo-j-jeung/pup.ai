package com.example.shepi13.testlogin;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests related to SplashAcitivty
 */
public class SplashActivityTest {

  @Rule
  public final ActivityTestRule<SplashActivity> mActivityTestRule = new ActivityTestRule<>(SplashActivity.class);
  private SplashActivity mActivity = null;

  @Before
  public void setUp() {
    mActivity = mActivityTestRule.getActivity();
  }

  // Checks layout elements
  @Test
  public void layoutTest() {
    View image_view = mActivity.findViewById(R.id.iv_splash_image);
    TextView text_view = mActivity.findViewById(R.id.tv_splash_title);

    assertNotNull(image_view);
    assertEquals(text_view.getText().toString(), mActivity.getString(R.string.app_name));
  }

  // Checks whether the shared preference exists
  @Test
  public void sahredPreferenceTest() {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
    assertNotNull(sharedPreferences);
  }

  @After
  public void tearDown() {
    mActivity = null;
  }
}