package com.example.shepi13.testlogin;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.Button;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for MainActivity
 */
public class MainActivityTest {

  @Rule
  public final ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
  private MainActivity mActivity = null;
  private final Instrumentation.ActivityMonitor monitor1 = getInstrumentation().addMonitor(BreedInfoActivity.class.getName(), null, false);
  private final Instrumentation.ActivityMonitor monitor2 = getInstrumentation().addMonitor(ProfileActivity.class.getName(), null, false);
  private final Instrumentation.ActivityMonitor monitor3 = getInstrumentation().addMonitor(FavoriteActivity.class.getName(), null, false);
  private final Instrumentation.ActivityMonitor monitor4 = getInstrumentation().addMonitor(FriendActivity.class.getName(), null, false);

  @Before
  public void setUp() {
    mActivity = mActivityTestRule.getActivity();
  }

  // Checks layout elements
  @Test
  public void layoutTest() {
    Button take_picture_button = mActivity.findViewById(R.id.bt_take_picture);
    Button browse_picture_button = mActivity.findViewById(R.id.bt_browse_picture);
    Button submit_picture_button = mActivity.findViewById(R.id.bt_submit_main);

    View image_preview_view = mActivity.findViewById(R.id.iv_main);
    View progress_bar_view = mActivity.findViewById(R.id.main_progressBar);

    assertNotNull(take_picture_button);
    assertNotNull(browse_picture_button);
    assertNotNull(submit_picture_button);

    assertNotNull(image_preview_view);
    assertNotNull(progress_bar_view);

    assertEquals(take_picture_button.getText().toString(), mActivity.getString(R.string.picture_bt));
    assertEquals(browse_picture_button.getText().toString(), mActivity.getString(R.string.gallery_bt));
    assertEquals(submit_picture_button.getText().toString(), mActivity.getString(R.string.classify_bt));
  }

  // Checks whether the user can search the breed by typing its name.
  @Test
  public void functionTest1() {
    onView(withText("Search")).perform(click());
    onView(withId(R.id.search_src_text)).perform(typeText("bagle\n"));

    Activity breedInfoActivty = getInstrumentation().waitForMonitorWithTimeout(monitor1, 5000);
    assertNotNull(breedInfoActivty);
  }

  // Checks whether the user can go to ProfileActivity by opening options menu and clicking 'Profile'.
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
    Espresso.onView(withText("Profile")).perform(click());

    Activity profileActivty = getInstrumentation().waitForMonitorWithTimeout(monitor2, 3500);
    assertNotNull(profileActivty);
  }

  // Checks whether the user can go to FavoriteActivity by opening options menu and clicking 'Favorite'.
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
    Espresso.onView(withText("Favorite")).perform(click());

    Activity favoriteActivty = getInstrumentation().waitForMonitorWithTimeout(monitor3, 5000);
    assertNotNull(favoriteActivty);
  }

  // Checks whether the user can go to FriendActivity by opening options menu and clicking 'Friends'.
  @Test
  public void functionTest4() {
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

    Activity friendActivty = getInstrumentation().waitForMonitorWithTimeout(monitor4, 3500);
    assertNotNull(friendActivty);
  }

  // Checks when the user logs out, the token in the shared preference gets deleted.
  @Test
  public void functionTest5() {
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
    Espresso.onView(withText("Log out")).perform(click());

    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
    String token = sharedPreferences.getString("token", "");
    assertEquals("", token); //Make sure to log in back for future tests
  }

  @After
  public void tearDown() {
    mActivity = null;
  }
}