package com.example.capitaview_page_1;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.Locale;

public class addPortfolioEntry extends AppCompatActivity {

    private Spinner companyNameSpinner;
    private EditText dateEditText, industryEditText, amountEditText;
    private Button addEntryButton, setDateButton;
    private DatabaseReference portfolioRef;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_portfolio_entry);

        companyNameSpinner = findViewById(R.id.companyNameSpinner);
        dateEditText = findViewById(R.id.dateEditText);
        industryEditText = findViewById(R.id.industryEditText);
        amountEditText = findViewById(R.id.amountEditText);
        addEntryButton = findViewById(R.id.addEntryButton);
        setDateButton = findViewById(R.id.setDate);
        mAuth = FirebaseAuth.getInstance();

        dateEditText.setText("");
        industryEditText.setText("");
        amountEditText.setText("");

        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        portfolioRef = FirebaseDatabase.getInstance("https://capitaviewdb-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference().child("Users").child(uid).child("PortfolioItem");


        // Fetch list of company names from API and populate spinner
        fetchCompanyNames();

        setDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        addEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });
    }

    private void fetchCompanyNames() {
        // Check if data is available in cache
        String cachedData = cacheManager.getListings(this);
        if (cachedData != null) {
            // Load data from cache
            loadCompanyNamesFromCache(cachedData);
        } else {
            // Fetch data from API

            fetchDataFromAPI();
        }

    }

    private void loadCompanyNamesFromCache(String cachedData) {
        // Parse cached data and populate the spinner
        try {
            List<String> companyNames = parseCompanyNames(cachedData);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(addPortfolioEntry.this,
                    android.R.layout.simple_spinner_item, companyNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            companyNameSpinner.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showDatePickerDialog() {
        // Get current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Subtract one day from the current date
        calendar.add(Calendar.DAY_OF_MONTH, -1);

        // Get the updated year, month, and dayOfMonth
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                addPortfolioEntry.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Update selected date text view
                        dateEditText.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                },
                year, month, dayOfMonth);

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        // Show date picker dialog
        datePickerDialog.show();
    }


    private void fetchDataFromAPI() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://www.alphavantage.co/query?function=LISTING_STATUS&apikey=DBU9W8268XTGDI1Y")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                List<String> companyNames = parseCompanyNames(responseData);
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(addPortfolioEntry.this,
                                        android.R.layout.simple_spinner_item, companyNames);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                companyNameSpinner.setAdapter(adapter);
                                cacheManager.saveListings(addPortfolioEntry.this, responseData);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    private List<String> parseCompanyNames(String csvData) {
        List<String> companyNames = new ArrayList<>();

        // Parse CSV data
        String[] lines = csvData.split("\n");
        for (int i = 1; i < lines.length; i++) { // Skip header line
            String[] parts = lines[i].split(",");
            if (parts.length > 1) {
                String symbol = parts[0].trim();
                String name = parts[1].trim();
                companyNames.add(name + " (" + symbol + ")");
            }
        }

        return companyNames;
    }


    private void addPortfolioEntry(double price) {
        // Get data from EditText fields
        String companyName = companyNameSpinner.getSelectedItem().toString().trim();
        String date = dateEditText.getText().toString().trim();
        String industry = industryEditText.getText().toString().trim();
        int amount = Integer.parseInt(amountEditText.getText().toString().trim());
        double totalPrice = amount * price;

        double tempTotalPrice = Double.parseDouble(String.format("%.2f",totalPrice));
        double tempPrice = Double.parseDouble(String.format("%.2f",price));

        // Create a new PortfolioEntry object
        PortfolioItem entry = new PortfolioItem(companyName, tempPrice, date, industry, amount, tempTotalPrice);

        String itemId = portfolioRef.push().getKey();
        entry.setItemId(itemId);
        portfolioRef.child(itemId).setValue(entry);

        // Display success message
        Toast.makeText(this, "Portfolio updated", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(addPortfolioEntry.this, ManageActivity.class);
        startActivity(intent);
        finish();
    }


    private void showConfirmationDialog() {

        String companyName = companyNameSpinner.getSelectedItem().toString().trim();
        String industryVar = industryEditText.getText().toString();
        String amountVar = amountEditText.getText().toString();
        String dateVar = dateEditText.getText().toString();
        String symbol = companyName.substring(companyName.lastIndexOf("(") + 1, companyName.lastIndexOf(")"));

        if(industryVar.isEmpty() || amountVar.isEmpty() || dateVar.isEmpty())
        {
            Toast.makeText(this, "Please Enter all fields", Toast.LENGTH_LONG).show();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm");
            builder.setMessage("Are you sure you want to add the entry?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    calculatePriceAndAddPortfolioEntry(symbol, dateVar);
                }
            });
            builder.setNegativeButton("No", null);
            builder.show();
        }
    }


    private void calculatePriceAndAddPortfolioEntry(String symbol, String date) {
        // Make API call to fetch historical price data for the selected company and date
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol="
                        + symbol + "&outputsize=full&apikey=DBU9W8268XTGDI1Y")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful()) {
                    final String responseData = response.body().string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonData = new JSONObject(responseData);
                                JSONObject timeSeriesData = jsonData.getJSONObject("Time Series (Daily)");
                                String[] dateParts = date.split("/");
                                String formattedDate = dateParts[2] + "-" + String.format("%02d", Integer.parseInt(dateParts[1])) + "-" + String.format("%02d", Integer.parseInt(dateParts[0])); // Construct date in YYYY-MM-DD format
                                JSONObject dayData = timeSeriesData.getJSONObject(formattedDate);
                                if(dayData!=null) {
                                    double closingPrice = Double.parseDouble(dayData.getString("4. close"));
                                    addPortfolioEntry(closingPrice);
                                }
                                else {
                                    Toast.makeText(addPortfolioEntry.this, "No data found for that date, try another date",Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                Toast.makeText(addPortfolioEntry.this, "No data found for that date, try another date",Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

    }

}
