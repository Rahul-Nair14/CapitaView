package com.example.capitaview_page_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ViewActivity extends AppCompatActivity {

    TextView currentDate, welcomeText;
    private ListView viewListView;
    private List<ViewActivityItem> viewActivityItemList;
    private ViewActivityAdapter viewActivityAdapter;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    TextView totalInvestmentValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        //Getting the currentDate to display on top of the activity
        currentDate = (TextView) findViewById(R.id.viewDateText);
        currentDate.setText(setDate());
        //Getting the username from the MainDashboard since we can access
        //view activity only through dashboard
        welcomeText = (TextView)findViewById(R.id.viewWelcomeText);
        welcomeText.setText("Welcome " + MainDashboard.dashBoardUserNameString);

        //Displays total investment value on top
        totalInvestmentValue = (TextView) findViewById(R.id.viewTotalInvestmentValue);

        //Initializing the adapter for showing all the stocks of the user with complete data
        viewListView = findViewById(R.id.ViewItemListView);
        viewActivityItemList = new ArrayList<>();
        viewActivityAdapter = new ViewActivityAdapter(this, viewActivityItemList);
        viewListView.setAdapter(viewActivityAdapter);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // User is signed in
            String uid = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance("https://capitaviewdb-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference().child("Users").child(uid).child("PortfolioItem");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    viewActivityItemList.clear();
                    double totalPortfolioValue = 0.0, tempTotalPortfolioValue;
                    if (dataSnapshot.exists()) {
                        // User has stocks in the database, populate the ListView
                        int position = 0; // Initialize position counter
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ViewActivityItem viewActivityItem = snapshot.getValue(ViewActivityItem.class);
                            fetchRealTimeDataAndUpdateViews(viewActivityItem,position);
                            //Getting the totalInvestment Value to display on the ViewActivity
                            totalPortfolioValue += viewActivityItem.getTotalPrice();
                            viewActivityItemList.add(viewActivityItem);
                            //Need position to correctly update the realtime data
                            position++;
                        }
                        //Temporary variable to get trimmed double value
                        tempTotalPortfolioValue = Double.parseDouble(String.format("%.2f",totalPortfolioValue));
                        totalInvestmentValue.setText("Total investment: $"+Double.toString(tempTotalPortfolioValue));
                        viewActivityAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(), "No stocks in Portfolio, Add to continue", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ViewActivity.this, addPortfolioEntry.class));
                        finish();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ViewActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                }
            });

            //Dummy Item Click, we dont want item clicks
            viewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
        } else {
            // User is not signed in, navigate to login activity
            startActivity(new Intent(ViewActivity.this, MainActivity.class));
            finish(); // Finish this activity to prevent going back
        }

    }

    //Sets the current date at the top of the activity
    String setDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Month is zero-based, so add 1
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String currentDate = String.format("%02d-%02d-%04d", dayOfMonth, month, year);

        return currentDate;
    }

    //Fetches Real time data for the user from alpha vantage using the symbol of the company
    private void fetchRealTimeDataAndUpdateViews(ViewActivityItem item, int position) {
        // Make API request to Alpha Vantage to fetch real-time data
        // for open,close,high,low and volume data
        String symbol = item.getCompanyName().substring(item.getCompanyName().lastIndexOf("(") + 1, item.getCompanyName().lastIndexOf(")"));
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&" +
                        "symbol=" + symbol + "&apikey=DBU9W8268XTGDI1Y")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(ViewActivity.this, "Connection Failed. Please try again later", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //Getting the jsondata from API and getting required realtime values of the company needed
                            try {
                                JSONObject jsonData = new JSONObject(responseData);
                                JSONObject globalquote = jsonData.getJSONObject("Global Quote");
                                double closeValue = Double.parseDouble(String.format("%.2f",Double.parseDouble(globalquote.getString("05. price"))));
                                double openValue = Double.parseDouble(String.format("%.2f",Double.parseDouble(globalquote.getString("02. open"))));
                                double highValue = Double.parseDouble(String.format("%.2f",Double.parseDouble(globalquote.getString("03. high"))));
                                double lowValue = Double.parseDouble(String.format("%.2f",Double.parseDouble(globalquote.getString("04. low"))));
                                int volume = Integer.parseInt(globalquote.getString("06. volume"));
                                item.setCloseValue(closeValue);
                                item.setOpenValue(openValue);
                                item.setLowValue(lowValue);
                                item.setHighValue(highValue);
                                item.setVolume(volume);
                                //calculating percentage loss and profit and displaying
                                double percentageChange = ((closeValue - item.getPrice()) / item.getPrice()) * 100;
                                item.setPercentchange(percentageChange);

                                // Set the text color based on whether the price has increased or decreased
                                if (percentageChange > 0) {
                                    item.setPercentageChangeColor(Color.GREEN); // Set color to green
                                } else if (percentageChange < 0) {
                                    item.setPercentageChangeColor(Color.RED); // Set color to red
                                } else {
                                    item.setPercentageChangeColor(Color.WHITE); // Set color to black for no change
                                }

                                updateItem(position,item);

                            } catch (JSONException e) {
                                Toast.makeText(ViewActivity.this, "Failed to load data. Please try again later",Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }


    private void updateItem(int position, ViewActivityItem item) {
        // Update the item in the list at the given position
        viewActivityItemList.set(position, item);
        // Notify the adapter that the data has changed for this specific item
        viewActivityAdapter.notifyDataSetChanged();
    }

}