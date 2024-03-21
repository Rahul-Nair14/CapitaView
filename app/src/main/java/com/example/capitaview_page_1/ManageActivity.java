package com.example.capitaview_page_1;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ManageActivity extends AppCompatActivity {

    Button addStocksButton;
    private ListView portfolioListView;
    private List<PortfolioItem> portfolioItemList;
    private PortfolioAdapter portfolioAdapter;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    TextView userNameForDisplay;
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        portfolioListView = findViewById(R.id.portfolioListView);
        portfolioItemList = new ArrayList<>();
        portfolioAdapter = new PortfolioAdapter(this, portfolioItemList);
        portfolioListView.setAdapter(portfolioAdapter);

        userNameForDisplay = findViewById(R.id.manageWelcomeText);
        builder = new AlertDialog.Builder(this);

        userNameForDisplay.setText("Welcome " + MainDashboard.dashBoardUserNameString);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // User is signed in
            String uid = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance("https://capitaviewdb-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference().child("Users").child(uid).child("PortfolioItem");
            portfolioAdapter.setOnDeleteButtonClickListener(new PortfolioAdapter.OnDeleteButtonClickListener() {
                @Override
                public void onDeleteButtonClick(int position) {

                    PortfolioItem selectedItem = portfolioItemList.get(position);
                    builder.setTitle("Confirm");
                    builder.setMessage("Are you sure you want to delete the entry?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            // Remove item from Firebase Realtime Database
                            databaseReference.child(selectedItem.getItemId()).removeValue();
                            // Remove item from list
                            portfolioItemList.remove(position);
                            portfolioAdapter.notifyDataSetChanged();

                            Toast.makeText(ManageActivity.this, "Stocks removed", Toast.LENGTH_SHORT).show();

                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (!(snapshot.exists())) {
                                        Toast.makeText(getApplicationContext(), "No stocks in Portfolio, Add to continue", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(ManageActivity.this, addPortfolioEntry.class));
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(ManageActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("No", null);
                    builder.show();
                }
            });
            // Check if there are any items in the database
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // User has stocks in the database, populate the ListView
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            PortfolioItem portfolioItem = snapshot.getValue(PortfolioItem.class);
                            portfolioItemList.add(portfolioItem);
                        }
                        portfolioAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(), "No stocks in Portfolio, Add to continue", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ManageActivity.this, addPortfolioEntry.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ManageActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                }
            });

            // Handle ListView item click events
            portfolioListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Handle item click here
                }
            });
        } else {
            // User is not signed in, navigate to login activity
            startActivity(new Intent(ManageActivity.this, MainActivity.class));
            finish(); // Finish this activity to prevent going back
        }

        addStocksButton = (Button) findViewById(R.id.AddStocksButton);
        addStocksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManageActivity.this, addPortfolioEntry.class));
                finish();
            }
        });

    }
}
