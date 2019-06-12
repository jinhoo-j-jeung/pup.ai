package com.example.shepi13.testlogin;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for Login Activity
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

  @Rule
  public final ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);
  private LoginActivity mActivity = null;

  private final Instrumentation.ActivityMonitor monitor1 = getInstrumentation().addMonitor(MainActivity.class.getName(), null, false);
  private final Instrumentation.ActivityMonitor monitor2 = getInstrumentation().addMonitor(RegisterActivity.class.getName(), null, false);

  @Before
  public void setUp() {
    mActivity = mActivityTestRule.getActivity();
  }

  // Checks layout elements
  @Test
  public void layoutTest() {
    View img_view = mActivity.findViewById(R.id.iv_login_image);
    View edit_login = mActivity.findViewById(R.id.et_login_email);
    View edit_password = mActivity.findViewById(R.id.et_login_password);
    Button button = mActivity.findViewById(R.id.bt_login);
    TextView tv_register = mActivity.findViewById(R.id.link_signup);
    TextView tv_without = mActivity.findViewById(R.id.link_main);

    assertNotNull(img_view);
    assertNotNull(edit_login);
    assertNotNull(edit_password);

    assertEquals(button.getText().toString(), mActivity.getString(R.string.login));
    assertEquals(tv_register.getText().toString(), mActivity.getString(R.string.tv_login_register));
    assertEquals(tv_without.getText().toString(), mActivity.getString(R.string.tv_login_without));
  }

  @Test
  public void clickableTest() {
    TextView tv_register = mActivity.findViewById(R.id.link_signup);
    TextView tv_without = mActivity.findViewById(R.id.link_main);
    assertTrue(tv_register.getLinksClickable());
    assertTrue(tv_without.getLinksClickable());
  }

  // checks whether the user can login succesfully
  @Test
  public void FunctionTest1() {
    String mEmail = "pupai@test.com";
    Espresso.onView(withId(R.id.et_login_email)).perform(typeText(mEmail));
    String mPassword = "test";
    Espresso.onView(withId(R.id.et_login_password)).perform(typeText(mPassword));
    Espresso.closeSoftKeyboard();
    Espresso.onView(withId(R.id.bt_login)).perform(click());

    Activity mainActivty = getInstrumentation().waitForMonitorWithTimeout(monitor1, 5000);
    assertNotNull(mainActivty);
    mainActivty.finish();
  }

  // checks whether the user can go to mainActivty by clicking 'use without signing in'
  @Test
  public void FunctionTest2() {
    Espresso.closeSoftKeyboard();
    Espresso.onView(withId(R.id.link_main)).perform(click());

    Activity mainActivty = getInstrumentation().waitForMonitorWithTimeout(monitor1, 3500);
    assertNotNull(mainActivty);
    mainActivty.finish();
  }

  // checks whether the user can go to registerActivity by clicking 'create account'
  @Test
  public void FunctionTest3() {
    Espresso.closeSoftKeyboard();
    Espresso.onView(withId(R.id.link_signup)).perform(click());

    Activity registerActivty = getInstrumentation().waitForMonitorWithTimeout(monitor2, 3500);
    assertNotNull(registerActivty);
    registerActivty.finish();
  }

  @After
  public void tearDown() {
    mActivity = null;
  }

}