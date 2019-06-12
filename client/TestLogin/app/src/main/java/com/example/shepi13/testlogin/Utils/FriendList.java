package com.example.shepi13.testlogin.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.shepi13.testlogin.R;

import java.util.ArrayList;

/*
 * Custom ArrayAdapter for displaying freinds of the user, which is used in FriendActivity
 */

public class FriendList extends ArrayAdapter<String> {
  private final Activity context;
  private final ArrayList<String> fav;

  public FriendList(Activity context, ArrayList<String> fav_breeds) {
    super(context, R.layout.list_breedinfo, fav_breeds);
    this.context = context;
    this.fav = fav_breeds;
  }

  @Override
  @NonNull
  public View getView(final int position, View view, @NonNull ViewGroup parent) {
    LayoutInflater inflater = context.getLayoutInflater();
    @SuppressLint({"InflateParams", "ViewHolder"}) View rowView = inflater.inflate(R.layout.list_friend, null, true);

    TextView txtTitle = rowView.findViewById(R.id.friend_email);
    txtTitle.setText(fav.get(position));

    return rowView;
  }
}