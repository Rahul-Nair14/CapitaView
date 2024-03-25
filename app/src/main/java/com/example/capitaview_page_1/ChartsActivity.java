package com.example.capitaview_page_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChartsActivity extends AppCompatActivity {
    String companyName, date, industry;
    double price, low, open, high, close, percentchange, totalPrice;
    int amount, volume, percentColor;
    private LineChart lineChart;
    private BarChart barChart;
    private PieChart pieChart;
    TextView companyNameText, dateBoughtText ,currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        companyNameText = findViewById(R.id.chartsCompanyName);
        dateBoughtText = findViewById(R.id.chartsBoughtDate);
        currentDate = findViewById(R.id.currentDateText);

        Intent intent = getIntent();
        if (intent != null) {
            companyName = intent.getStringExtra("CompanyName");
            date = intent.getStringExtra("Date");
            industry = intent.getStringExtra("Industry");
            price = intent.getDoubleExtra("Price", 0.0);
            low = intent.getDoubleExtra("Low", 0.0);
            open = intent.getDoubleExtra("Open", 0.0);
            high = intent.getDoubleExtra("High", 0.0);
            close = intent.getDoubleExtra("Close", 0.0);
            percentchange = intent.getDoubleExtra("PercentageChange", 0.0);
            totalPrice = intent.getDoubleExtra("TotalPrice", 0.0);
            volume = intent.getIntExtra("Volume", 0);
            amount = intent.getIntExtra("Amount", 0);
            percentColor = intent.getIntExtra("PercentageChangeColor", 0);

            // Initialize charts
            lineChart = findViewById(R.id.lineChart);
            barChart = findViewById(R.id.barChart);
            pieChart = findViewById(R.id.pieChart);

            companyNameText.setText(companyName);
            dateBoughtText.setText("Bought on: " + date);
            currentDate.setText(setDate());

            // Line Chart
            List<Entry> lineEntries = new ArrayList<>();
            lineEntries.add(new Entry(0,(float) price));
            lineEntries.add(new Entry(1, (float) low));
            lineEntries.add(new Entry(2, (float) open));
            lineEntries.add(new Entry(3, (float) high));
            lineEntries.add(new Entry(4, (float) close));

            LineDataSet lineDataSet = new LineDataSet(lineEntries, "Price Variation");
            lineDataSet.setColors(Color.BLUE);
            lineDataSet.setValueTextColor(Color.BLACK);
            LineData lineData = new LineData(lineDataSet);
            lineChart.setData(lineData);
            lineChart.getDescription().setEnabled(false);
            lineChart.invalidate();

            // Bar Chart
            List<BarEntry> barEntries = new ArrayList<>();
            barEntries.add(new BarEntry(0, amount));
            barEntries.add(new BarEntry(1, volume));

            BarDataSet barDataSet = new BarDataSet(barEntries, "Amount Bought vs Volume Dealt Today");
            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            BarData barData = new BarData(barDataSet);
            barChart.setData(barData);
            barChart.getDescription().setEnabled(false);
            barChart.invalidate();

            // Pie Chart
            List<PieEntry> pieEntries = new ArrayList<>();
            pieEntries.add(new PieEntry((float) totalPrice, "Total Price"));
            pieEntries.add(new PieEntry((float) (totalPrice * percentchange / 100), "Profit/Loss"));

            PieDataSet pieDataSet = new PieDataSet(pieEntries, "Investment Overview");
            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            PieData pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);
            pieChart.getDescription().setEnabled(false);
            pieChart.invalidate();

        }
    }

    //Setting the current date
    String setDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Month is zero-based, so add 1
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String currentDate = String.format("%02d-%02d-%04d", dayOfMonth, month, year);

        return currentDate;
    }
}