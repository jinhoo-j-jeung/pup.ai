package com.example.shepi13.testlogin;

import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.Button;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for Profile Edit Activity
 */
public class ProfileEditActivityTest {

  @Rule
  public final ActivityTestRule<ProfileEditActivity> mActivityTestRule = new ActivityTestRule<>(ProfileEditActivity.class);
  private ProfileEditActivity mActivity = null;

  @Before
  public void setUp() {
    mActivity = mActivityTestRule.getActivity();
  }

  // Checks layout elements
  @Test
  public void layoutTest() {
    View edit_picture_image_view = mActivity.findViewById(R.id.iv_editpic);
    View edit_name_view = mActivity.findViewById(R.id.et_edit_name);
    View edit_location_view = mActivity.findViewById(R.id.et_edit_location);
    Button take_picture_button = mActivity.findViewById(R.id.bt_edit_camera);
    Button browse_picture_button = mActivity.findViewById(R.id.bt_edit_gallery);
    Button sumbit_button = mActivity.findViewById(R.id.bt_edit_submit);

    assertNotNull(edit_picture_image_view);
    assertNotNull(edit_name_view);
    assertNotNull(edit_location_view);

    assertNotNull(take_picture_button);
    assertNotNull(browse_picture_button);
    assertNotNull(sumbit_button);

    assertEquals(take_picture_button.getText().toString(), mActivity.getString(R.string.picture_bt));
    assertEquals(browse_picture_button.getText().toString(), mActivity.getString(R.string.gallery_bt));
    assertEquals(sumbit_button.getText().toString(), mActivity.getString(R.string.submit));
  }

  @After
  public void tearDown() {
    mActivity = null;
  }
}