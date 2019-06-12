package com.example.shepi13.testlogin.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shepi13.testlogin.R;

/*
 * Custom ArrayAdapter for displaying information of the breed, which is used in BreedInfoActivity
 */

public class BreedInfoList extends ArrayAdapter<String> {
  private final Activity context;
  private final String[] category;
  private final Integer[] imageId;

  public BreedInfoList(Activity context, String[] category, Integer[] imageId) {
    super(context, R.layout.list_breedinfo, category);
    this.context = context;
    this.category = category;
    this.imageId = imageId;
  }

  @Override
  @NonNull
  public View getView(int position, View view, @NonNull ViewGroup parent) {
    LayoutInflater inflater = context.getLayoutInflater();
    @SuppressLint({"InflateParams", "ViewHolder"}) View rowView = inflater.inflate(R.layout.list_breedinfo, null, true);
    TextView txtTitle = rowView.findViewById(R.id.breed_txt);
    ImageView imageView = rowView.findViewById(R.id.breed_img);
    txtTitle.setText(category[position]);
    imageView.setImageResource(imageId[position]);
    return rowView;
  }
}