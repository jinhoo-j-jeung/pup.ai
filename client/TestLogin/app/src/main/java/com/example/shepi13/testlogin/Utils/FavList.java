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
 * Custom ArrayAdapter for displaying favorite breeeds of the user, which is used in FavoriteActivity
 */

public class FavList extends ArrayAdapter<String> {
  private final Activity context;
  private final ArrayList<String> fav;

  public FavList(Activity context, ArrayList<String> fav_breeds) {
    super(context, R.layout.list_breedinfo, fav_breeds);
    this.context = context;
    this.fav = fav_breeds;
  }

  @Override
  @NonNull
  public View getView(final int position, View view, @NonNull ViewGroup parent) {
    LayoutInflater inflater = context.getLayoutInflater();
    @SuppressLint({"InflateParams", "ViewHolder"}) View rowView = inflater.inflate(R.layout.list_favorite, null, true);

    TextView txtTitle = rowView.findViewById(R.id.fav_breedname);
    txtTitle.setText(fav.get(position));

    return rowView;
  }
}