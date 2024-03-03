package com.example.capitaview_page_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsActivity extends AppCompatActivity {

    private ListView newsListView;
    private ArrayList<String> headlinesList,imagesList;
    private ArrayList<String> articlesList, dateList, sourcesList;
    private HeadlineAdapter adapter;
    ArrayList<Headline> headlines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);


        newsListView = (ListView)findViewById(R.id.NewsListView);
        headlinesList = new ArrayList<>();
        articlesList = new ArrayList<>();
        dateList = new ArrayList<>();
        imagesList = new ArrayList<>();
        sourcesList = new ArrayList<>();

        headlines = new ArrayList<>();

        fetchEconomicNews();

        adapter = new HeadlineAdapter(this, headlines);
        // Attach the adapter to a ListView
        newsListView.setAdapter(adapter);
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String articleTitle = headlinesList.get(position);
                String articleDescription = articlesList.get(position);
                String articleDate = dateList.get(position);
                String articleImage = imagesList.get(position);
                String articleSource = sourcesList.get(position);

                Intent intent = new Intent(NewsActivity.this, Articles.class);
                intent.putExtra("title", articleTitle);
                intent.putExtra("description", articleDescription);
                intent.putExtra("date",articleDate);
                intent.putExtra("image",articleImage);
                intent.putExtra("source",articleSource);
                startActivity(intent);
            }
        });

    }

    private void fetchEconomicNews() {
        OkHttpClient client = new OkHttpClient();
        String url = "https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=ec2e4544397a407f85e540d8d1cbde3e";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NewsActivity.this, "Failed to fetch news", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        JSONArray articlesArray = jsonObject.getJSONArray("articles");
                        headlinesList.clear();
                        articlesList.clear();
                        dateList.clear();
                        sourcesList.clear();
                        imagesList.clear();
                        headlines.clear();
                        for (int i = 0; i < articlesArray.length(); i++) {
                            JSONObject articleObject = articlesArray.getJSONObject(i);
                            String headline = articleObject.getString("title");
                            String article = articleObject.getString("description");
                            String date = articleObject.getString("publishedAt");
                            String image = articleObject.getString("urlToImage");
                            JSONObject articleSource = articleObject.getJSONObject("source");
                            String sourceName = articleSource.getString("name");
                            headlinesList.add(headline);
                            articlesList.add(article);
                            dateList.add(date);
                            imagesList.add(image);
                            sourcesList.add(sourceName);
                            headlines.add(new Headline(headline,image));
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                newsListView.setVisibility(View.VISIBLE);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
