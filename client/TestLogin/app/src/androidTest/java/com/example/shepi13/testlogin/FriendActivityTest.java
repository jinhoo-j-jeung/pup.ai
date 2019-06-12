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
 * Tests for FriendActivity
 */
public class FriendActivityTest {

  @Rule
  public final ActivityTestRule<FriendActivity> mActivityTestRule = new ActivityTestRule<>(FriendActivity.class);
  private FriendActivity mActivity = null;

  @Before
  public void setUp() {
    mActivity = mActivityTestRule.getActivity();
  }

  // Checks layout elements
  @Test
  public void layoutTest() {
    TextView friend_header_text_view = mActivity.findViewById(R.id.tv_header);
    View friends_list_view = mActivity.findViewById(R.id.lv_friends);
    View friend_error_text_view = mActivity.findViewById(R.id.tv_friend_none);

    TextView pending_header_text_view = mActivity.findViewById(R.id.tv_pending_header);
    View pending_list_view = mActivity.findViewById(R.id.lv_pending);
    View pending_error_text_view = mActivity.findViewById(R.id.tv_pending_none);

    TextView request_header_text_view = mActivity.findViewById(R.id.tv_request_header);
    View request_list_view = mActivity.findViewById(R.id.lv_request);
    View request_error_text_view = mActivity.findViewById(R.id.tv_request_none);

    TextView suggest_header_text_View = mActivity.findViewById(R.id.tv_suggest_header);
    View suggest_list_view = mActivity.findViewById(R.id.lv_suggest);
    View suggest_error_text_view = mActivity.findViewById(R.id.tv_suggest_none);

    assertNotNull(friend_header_text_view);
    assertNotNull(friends_list_view);
    assertNotNull(friend_error_text_view);

    assertNotNull(pending_header_text_view);
    assertNotNull(pending_list_view);
    assertNotNull(pending_error_text_view);

    assertNotNull(request_header_text_view);
    assertNotNull(request_list_view);
    assertNotNull(request_error_text_view);

    assertNotNull(suggest_header_text_View);
    assertNotNull(suggest_list_view);
    assertNotNull(suggest_error_text_view);

    assertEquals(friend_header_text_view.getText().toString(), mActivity.getString(R.string.friendact_fheader));
    assertEquals(pending_header_text_view.getText().toString(), mActivity.getString(R.string.friendact_pheader));
    assertEquals(request_header_text_view.getText().toString(), mActivity.getString(R.string.friendact_rheader));
    assertEquals(suggest_header_text_View.getText().toString(), mActivity.getString(R.string.friendact_sheader));
  }

  @After
  public void tearDown() {
    mActivity = null;
  }
}