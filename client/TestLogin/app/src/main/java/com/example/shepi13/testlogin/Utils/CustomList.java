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
 * Custom ArrayAdapter for displaying the classification result, which is used in ResultActivity
 */

public class CustomList extends ArrayAdapter<String> {
  private final Activity context;
  private final String[] breed;
  private final String[] prob;
  private final Integer[] imageId;

  public CustomList(Activity context, String[] breed, String[] prob, Integer[] imageId) {
    super(context, R.layout.list_single, breed);
    this.context = context;
    this.breed = breed;
    this.prob = prob;
    this.imageId = imageId;
  }

  @Override
  @NonNull
  public View getView(int position, View view, @NonNull ViewGroup parent) {
    LayoutInflater inflater = context.getLayoutInflater();
    // Recycler View is too unstable to use.
    @SuppressLint({"InflateParams", "ViewHolder"}) View rowView = inflater.inflate(R.layout.list_single, null, true);
    TextView txtTitle1 = rowView.findViewById(R.id.txt1);
    TextView txtTitle2 = rowView.findViewById(R.id.txt2);

    ImageView imageView = rowView.findViewById(R.id.img);
    txtTitle1.setText(breed[position]);

    txtTitle2.setText(prob[position]);

    imageView.setImageResource(imageId[position]);
    return rowView;
  }
}