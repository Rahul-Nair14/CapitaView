package com.example.capitaview_page_1;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class addPortfolioEntry extends AppCompatActivity {

    private Spinner companyNameSpinner;
    private EditText priceEditText, dateEditText, industryEditText, amountEditText;
    private Button addEntryButton, setDateButton;
    private DatabaseReference portfolioRef;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_portfolio_entry);

        companyNameSpinner = findViewById(R.id.companyNameSpinner);
        priceEditText = findViewById(R.id.priceEditText);
        dateEditText = findViewById(R.id.dateEditText);
        industryEditText = findViewById(R.id.industryEditText);
        amountEditText = findViewById(R.id.amountEditText);
        addEntryButton = findViewById(R.id.addEntryButton);
        setDateButton = findViewById(R.id.setDate);
        mAuth = FirebaseAuth.getInstance();

        priceEditText.setText("");
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


    private void showDatePickerDialog() {
        // Get current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

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

        // Show date picker dialog
        datePickerDialog.show();
    }
    private void fetchCompanyNames() {
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


    private void addPortfolioEntry() {
        // Get data from EditText fields
        String companyName = companyNameSpinner.getSelectedItem().toString().trim();
        double price = Double.parseDouble(priceEditText.getText().toString().trim());
        String date = dateEditText.getText().toString().trim();
        String industry = industryEditText.getText().toString().trim();
        int amount = Integer.parseInt(amountEditText.getText().toString().trim());

        // Create a new PortfolioEntry object
        PortfolioItem entry = new PortfolioItem(companyName, price, date, industry, amount);

        String itemId = portfolioRef.push().getKey();
        entry.setItemId(itemId);
        portfolioRef.child(itemId).setValue(entry);

        // Display success message
        Toast.makeText(this, "Portfolio updated", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(addPortfolioEntry.this, ManageActivity.class));

        finish();
    }


    private void showConfirmationDialog() {

        String priceVar = priceEditText.getText().toString();
        String industryVar = industryEditText.getText().toString();
        String amountVar = amountEditText.getText().toString();
        String dateVar = dateEditText.getText().toString();

        if(priceVar.isEmpty() || industryVar.isEmpty() || amountVar.isEmpty() || dateVar.isEmpty())
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

                    addPortfolioEntry();
                }
            });
            builder.setNegativeButton("No", null);
            builder.show();
        }
    }
}
