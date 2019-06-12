package com.example.shepi13.testlogin;

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
 * Tests for FavoriteActivity
 */
public class FavoriteActivityTest {

  @Rule
  public final ActivityTestRule<FavoriteActivity> mActivityTestRule = new ActivityTestRule<>(FavoriteActivity.class);
  private FavoriteActivity mActivity = null;

  @Before
  public void setUp() {
    mActivity = mActivityTestRule.getActivity();
  }

  // Checks layout elements
  @Test
  public void layoutTest() {
    TextView title_text_view = mActivity.findViewById(R.id.tv_fav_title);
    TextView exp_text_view = mActivity.findViewById(R.id.tv_fav_exp);
    View empty_text_view = mActivity.findViewById(R.id.tv_fav_empty);
    View fav_list_view = mActivity.findViewById(R.id.lv_favs);

    assertNotNull(title_text_view);
    assertNotNull(exp_text_view);
    assertNotNull(empty_text_view);
    assertNotNull(fav_list_view);

    assertEquals(title_text_view.getText().toString(), mActivity.getString(R.string.favact_title));
    assertEquals(exp_text_view.getText().toString(), mActivity.getString(R.string.favact_exp));
  }

  @After
  public void tearDown() {
    mActivity = null;
  }
}