package com.example.capitaview_page_1;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

//Adapter for Headlines
public class HeadlineAdapter extends ArrayAdapter<Headline> {
    public HeadlineAdapter(Context context, ArrayList<Headline> headlines) {
        super(context, 0, headlines);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Headline headline = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.article_headlines, parent, false);
        }

        // Lookup view for data population
        TextView headlineTextView = convertView.findViewById(R.id.headlineTextView);
        ImageView headlineImageView = convertView.findViewById(R.id.headlineImageView);

        // Populate the data into the template view using the data object
        headlineTextView.setText(headline.getTitle());
        Glide.with(getContext()).load(headline.getImageResource()).into(headlineImageView);

        // Return the completed view to render on screen
        return convertView;
    }
}
