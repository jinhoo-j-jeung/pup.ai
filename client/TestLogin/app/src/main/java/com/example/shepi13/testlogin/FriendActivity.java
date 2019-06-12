package com.example.shepi13.testlogin;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.example.shepi13.testlogin.Utils.ApiConnector;
import com.example.shepi13.testlogin.Utils.FriendList;
import com.example.shepi13.testlogin.Utils.ProfileLoader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The user can view his/her friend.
 * There are 4 lists: 1. A list displaying friends of the user, 2. A list displaying the pending requests that the user has sent.
 *                    3. A list displaying friend requests that the user has received. 4. A list displaying the suggested friends for the user.
 * By long-clicking the user's friend, the user can delete his/her friend.
 * By clicking a suggested friend, the user can send a friend request to other users.
 * By long-clicking a friend request sent by other users, the user can accept the requests.
 */

public class FriendActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

  private TextView mFriendErrorTextView, mPendingErrorTextView, mRequestErrorTextView, mSuggestErrorTextView;
  private ListView mFriendsListView, mPendingListView, mRequestListView, mSuggestListView;

  private byte[] delete_data;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_friend);

    mFriendsListView = findViewById(R.id.lv_friends);
    mFriendErrorTextView = findViewById(R.id.tv_friend_none);

    mPendingListView = findViewById(R.id.lv_pending);
    mPendingErrorTextView = findViewById(R.id.tv_pending_none);

    mRequestListView = findViewById(R.id.lv_request);
    mRequestErrorTextView = findViewById(R.id.tv_request_none);

    mSuggestListView = findViewById(R.id.lv_suggest);
    mSuggestErrorTextView = findViewById(R.id.tv_suggest_none);

    Bundle profileBundle = new Bundle();
    LoaderManager loaderManager = getLoaderManager();
    Loader<String> profileLoader = loaderManager.getLoader(4);

    if (profileLoader == null) {
      loaderManager.initLoader(4, profileBundle, this);
    } else {
      loaderManager.restartLoader(4, profileBundle, this);
    }
  }

  @Override
  public Loader<String> onCreateLoader(int i, Bundle bundle) {
    return new ProfileLoader(this, bundle);
  }

  @Override
  public void onLoadFinished(Loader<String> loader, final String data) {
    try {
      final JSONObject json = new JSONObject(data);

      JSONArray jsonArray1 = json.getJSONArray("friends");

      // If the user's friend data is empty, displays messages.
      if(jsonArray1.length() == 0) {
        mFriendErrorTextView.setVisibility(View.VISIBLE);
        mFriendErrorTextView.setText(R.string.friendact_fempty);
        mPendingErrorTextView.setVisibility(View.VISIBLE);
        mPendingErrorTextView.setText(R.string.friendact_pempty);
        mRequestErrorTextView.setVisibility(View.VISIBLE);
        mRequestErrorTextView.setText(R.string.friendact_rempty);
        mSuggestErrorTextView.setVisibility(View.VISIBLE);
        mSuggestErrorTextView.setText(R.string.friendact_sempty);
      }
      else {
        // Sets the 3 lists: friends, pending requests, and friend requests.
        final ArrayList<String> friends_arr = new ArrayList<>();
        ArrayList<String> pending_arr = new ArrayList<>();
        final ArrayList<String> request_arr = new ArrayList<>();

        for(int i = 0; i < jsonArray1.length(); i++) {
          JSONObject jsonObject = jsonArray1.getJSONObject(i);
          String f_response = jsonObject.getString("user2");
          boolean accepted = jsonObject.getBoolean("accepted");
          boolean initiator = jsonObject.getBoolean("initiator");

          JSONObject f_json = new JSONObject(f_response);
          String friend = f_json.getString("username");

          if(accepted) {
            friends_arr.add(friend);
          }
          else if(initiator) {
            pending_arr.add(friend);
          }
          else {
            request_arr.add(friend);
          }
        }
        final FriendList f_adapter = new FriendList(this, friends_arr);
        final FriendList p_adapter = new FriendList(this, pending_arr);
        final FriendList r_adapter = new FriendList(this, request_arr);

        if(f_adapter.isEmpty()) {
          mFriendErrorTextView.setVisibility(View.VISIBLE);
          mFriendErrorTextView.setText(R.string.friendact_fempty);
        }
        if(p_adapter.isEmpty()) {
          mPendingErrorTextView.setVisibility(View.VISIBLE);
          mPendingErrorTextView.setText(R.string.friendact_pempty);
        }
        if(r_adapter.isEmpty()) {
          mRequestErrorTextView.setVisibility(View.VISIBLE);
          mRequestErrorTextView.setText(R.string.friendact_rempty);
        }

        mFriendsListView.setAdapter(f_adapter);
        // If an item is long clicked, the user deletes the associated friend.
        mFriendsListView.setOnItemLongClickListener(new OnItemLongClickListener() {
          @Override
          public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
            Runnable runnable = new Runnable() {
              @Override
              public void run() {
                String friend_email = f_adapter.getItem(i);
                try {
                  JSONObject jsonObject = new JSONObject().put("username", friend_email);
                  String json = jsonObject.toString();
                  delete_data = json.getBytes();
                  String friend_url = "http://52.165.223.103:8000/user/friend_user";
                  ApiConnector.MakeDeleteRequest(friend_url, delete_data, "application/json");
                } catch (JSONException e) {
                  e.printStackTrace();
                } catch (MalformedURLException e) {
                  e.printStackTrace();
                } catch (IOException e) {
                  e.printStackTrace();
                }
              }
            };
            Thread t = new Thread(runnable);
            t.start();
            try {
              t.join();
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            // Refreshes the entire activity to display changes
            finish();
            startActivity(getIntent());
            return true;
          }
        });

        mPendingListView.setAdapter(p_adapter);

        mRequestListView.setAdapter(r_adapter);

        // If an item is long clicked, the user accepts the associated friend request.
        // The added friend's email will be displayed in the friend list.
        mRequestListView.setOnItemLongClickListener(new OnItemLongClickListener() {
          @Override
          public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
            Runnable runnable = new Runnable() {
              @Override
              public void run() {
                String friend_email = r_adapter.getItem(i);
                try {
                  JSONObject jsonObject = new JSONObject().put("username", friend_email);
                  String json = jsonObject.toString();
                  byte[] data_to_send = json.getBytes();

                  String friend_url = "http://52.165.223.103:8000/user/friend_user";
                  ApiConnector.MakePostRequest(new URL(friend_url), data_to_send, "application/json");
                } catch (JSONException e) {
                  e.printStackTrace();
                } catch (MalformedURLException e) {
                  e.printStackTrace();
                } catch (IOException e) {
                  e.printStackTrace();
                }
              }
            };
            Thread t = new Thread(runnable);

            t.start();
            try {
              t.join();

            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            finish();
            startActivity(getIntent());
            return true;
          }
        });
      }

      // Extracts data for friend suggestions
      JSONArray jsonArray2 = json.getJSONArray("recommendation");

      if(jsonArray2.length() == 0) {
        mSuggestErrorTextView.setVisibility(View.VISIBLE);
        mSuggestErrorTextView.setText(R.string.friendact_sempty);
      }
      else {
        final ArrayList<String> suggest_arr = new ArrayList<>();

        for(int i = 0; i < jsonArray2.length(); i++) {
          String suggested_f = jsonArray2.getJSONArray(i).getString(0);
          suggest_arr.add(suggested_f);
        }

        final FriendList s_adapter = new FriendList(this, suggest_arr);

        if(s_adapter.isEmpty()) {
          mSuggestErrorTextView.setVisibility(View.VISIBLE);
          mSuggestErrorTextView.setText(R.string.friendact_sempty);
        }

        mSuggestListView.setAdapter(s_adapter);

        // If an item is long-clicked, the user sends a friend request to the associated user.
        // Until the other user accepts the friendship, his/her email will be displayed in the pending list.
        mSuggestListView.setOnItemLongClickListener(new OnItemLongClickListener() {
          @Override
          public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
            Runnable runnable = new Runnable() {
              @Override
              public void run() {
                String friend_email = s_adapter.getItem(i);
                try {
                  JSONObject jsonObject = new JSONObject().put("username", friend_email);
                  String json = jsonObject.toString();
                  byte[] data_to_send = json.getBytes();

                  String friend_url = "http://52.165.223.103:8000/user/friend_user";
                  ApiConnector.MakePostRequest(new URL(friend_url), data_to_send, "application/json");
                } catch (JSONException e) {
                  e.printStackTrace();
                } catch (MalformedURLException e) {
                  e.printStackTrace();
                } catch (IOException e) {
                  e.printStackTrace();
                }
              }
            };
            Thread t = new Thread(runnable);
            t.start();
            try {
              t.join();
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            finish();
            startActivity(getIntent());
            return true;
          }
        });
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @Override
  public void onLoaderReset(Loader<String> loader) {
  }
}