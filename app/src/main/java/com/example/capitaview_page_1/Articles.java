package com.example.capitaview_page_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class Articles extends AppCompatActivity {

    private TextView articleTitleTextView,articleSourceView;
    private TextView articleDescriptionTextView, articlePublishDate;

    ImageView articleImageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        articlePublishDate = findViewById(R.id.articlePublishDate);
        articleTitleTextView = findViewById(R.id.articleTitleTextView);
        articleDescriptionTextView = findViewById(R.id.articleDescriptionTextView);
        articleImageView = findViewById(R.id.articleImageView);
        articleSourceView = findViewById(R.id.articleSourceView);

        // Retrieve data passed from previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String articleTitle = extras.getString("title");
            String articleDescription = extras.getString("description");
            String articleDate = extras.getString("date");
            String articleImage = extras.getString("image");
            String articleSource = extras.getString("source");

            // Set data to views
            articleTitleTextView.setText(articleTitle);
            articleDescriptionTextView.setText(articleDescription);
            articlePublishDate.setText(articleDate);
            Glide.with(getApplicationContext()).load(articleImage).into(articleImageView);
            articleSourceView.setText(articleSource);

        }
    }
}
