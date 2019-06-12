package com.example.shepi13.testlogin;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.Button;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for RegisterActivity
 */
public class RegisterActivityTest {

  @Rule
  public final ActivityTestRule<RegisterActivity> mActivityTestRule = new ActivityTestRule<>(RegisterActivity.class);
  private RegisterActivity mActivity = null;

  private final Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(MainActivity.class.getName(), null, false);

  @Before
  public void setUp() {
    mActivity = mActivityTestRule.getActivity();
  }

  // Checks layout elements
  @Test
  public void layoutTest() {
    View img_view = mActivity.findViewById(R.id.iv_register_image);
    View edit_login = mActivity.findViewById(R.id.et_register_email);
    View edit_password = mActivity.findViewById(R.id.et_register_password);
    View edit_name = mActivity.findViewById(R.id.et_name);
    View edit_location = mActivity.findViewById(R.id.et_location);
    Button submit_button = mActivity.findViewById(R.id.bt_submit_profile);

    assertNotNull(img_view);
    assertNotNull(edit_login);
    assertNotNull(edit_password);
    assertNotNull(edit_name);
    assertNotNull(edit_location);
    assertNotNull(submit_button);

    assertEquals(submit_button.getText().toString(), mActivity.getString(R.string.submit));
  }

  // Checks whether the user can register succesfully
  @Test
  public void functionTest1() {
    String mEmail = "new5@test.com";//needs to change the email everytime we test
    Espresso.onView(withId(R.id.et_register_email)).perform(typeText(mEmail));
    String mPassword = "test";
    Espresso.onView(withId(R.id.et_register_password)).perform(typeText(mPassword));
    Espresso.closeSoftKeyboard();
    String mName = "test";
    Espresso.onView(withId(R.id.et_name)).perform(typeText(mName));
    Espresso.closeSoftKeyboard();
    String mLocation = "test";
    Espresso.onView(withId(R.id.et_location)).perform(typeText(mLocation));
    Espresso.closeSoftKeyboard();
    Espresso.onView(withId(R.id.bt_submit_profile)).perform(click());

    Activity mainActivty = getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);
    assertNotNull(mainActivty);
  }

  @After
  public void tearDown() {
    mActivity = null;
  }
}