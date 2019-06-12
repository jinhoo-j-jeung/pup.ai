package com.example.shepi13.testlogin;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for ProfileActivity
 */
public class ProfileActivityTest {

  @Rule
  public final ActivityTestRule<ProfileActivity> mActivityTestRule = new ActivityTestRule<>(ProfileActivity.class);
  private ProfileActivity mActivity = null;

  private final Instrumentation.ActivityMonitor monitor1 = getInstrumentation().addMonitor(ProfileEditActivity.class.getName(), null, false);
  private final Instrumentation.ActivityMonitor monitor2 = getInstrumentation().addMonitor(FavoriteActivity.class.getName(), null, false);
  private final Instrumentation.ActivityMonitor monitor3 = getInstrumentation().addMonitor(FriendActivity.class.getName(), null, false);

  @Before
  public void setUp() {
    mActivity = mActivityTestRule.getActivity();
  }

  // Checks layout elements
  @Test
  public void layoutTest() {
    TextView profile_name_text_view = mActivity.findViewById(R.id.tv_profile_name);
    View profile_image_view = mActivity.findViewById(R.id.iv_profile);
    View profile_progress_bar = mActivity.findViewById(R.id.pb_profile);

    View email_image_view = mActivity.findViewById(R.id.iv_profile_email);
    TextView email_text_view = mActivity.findViewById(R.id.tv_profile_email);

    View location_image_view = mActivity.findViewById(R.id.iv_profile_location);
    TextView location_text_View = mActivity.findViewById(R.id.tv_profile_location);

    Button edit_profile_button = mActivity.findViewById(R.id.bt_profile);

    assertNotNull(profile_name_text_view);
    assertNotNull(profile_image_view);
    assertNotNull(profile_progress_bar);

    assertNotNull(email_image_view);
    assertNotNull(email_text_view);

    assertNotNull(location_image_view);
    assertNotNull(location_text_View);

    assertNotNull(edit_profile_button);

    assertEquals(edit_profile_button.getText().toString(), mActivity.getString(R.string.bt_profile_edit));
  }

  // Checks whether the user can go to ProfileEditActivity by clicking Edit button.
  @Test
  public void functionTest1() {
    Espresso.onView(withId(R.id.bt_profile)).perform(click());

    Activity profileEditActivty = getInstrumentation().waitForMonitorWithTimeout(monitor1, 3500);
    assertNotNull(profileEditActivty);
  }

  // Checks whether the user can go to FavoriteActivity by opening options menu and clicking 'Favorite'.
  @Test
  public void functionTest2() {
    Espresso.openActionBarOverflowOrOptionsMenu(getInstrumentation().getContext());
    Thread t = new Thread() {
      public void run() {
        try {
          sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    };
    t.start();
    try {
      t.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Espresso.onView(withText("Favorite")).perform(click());

    Activity favoriteActivty = getInstrumentation().waitForMonitorWithTimeout(monitor2, 5000);
    assertNotNull(favoriteActivty);
  }

  // Checks whether the user can go to FriendActivity by opening options menu and clicking 'Friends'.
  @Test
  public void functionTest3() {
    Espresso.openActionBarOverflowOrOptionsMenu(getInstrumentation().getContext());
    Thread t = new Thread() {
      public void run() {
        try {
          sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    };
    t.start();
    try {
      t.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Espresso.onView(withText("Friends")).perform(click());

    Activity friendActivty = getInstrumentation().waitForMonitorWithTimeout(monitor3, 3500);
    assertNotNull(friendActivty);
  }

  @After
  public void tearDown() {
    mActivity = null;
  }
}