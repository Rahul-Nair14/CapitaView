package com.example.capitaview_page_1;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//Template for ViewActivity items which are added to the list after updating values from
//both Alphavantage as well as Firebase User values
public class ViewActivityAdapter extends ArrayAdapter<ViewActivityItem> {

    private Context context;
    private List<ViewActivityItem> viewItemList;
    private onGetChartsButtonClickListener onGetChartsButtonClickListener;
    public ViewActivityAdapter(Context context, List<ViewActivityItem> viewItemList) {
        super(context, 0, viewItemList);
        this.context = context;
        this.viewItemList = viewItemList;
    }

    public interface onGetChartsButtonClickListener {
        void onGetChartsClick(int position);
    }

    public void setOnGetChartsButtonClickListener(ViewActivityAdapter.onGetChartsButtonClickListener listener) {
        this.onGetChartsButtonClickListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.view_activity_item_layout, parent, false);
        }

        ViewActivityItem currentItem = viewItemList.get(position);

        TextView companyNameTextView = listItemView.findViewById(R.id.companyNameView);
        TextView priceTextView = listItemView.findViewById(R.id.PriceView);
        TextView dateTextView = listItemView.findViewById(R.id.dateView);
        TextView industryTextView = listItemView.findViewById(R.id.industryView);
        TextView amountTextView = listItemView.findViewById(R.id.amountView);
        TextView totalPriceTextView = listItemView.findViewById(R.id.totalPriceView);
        TextView lowValueTextView = listItemView.findViewById(R.id.lowValueTextView);
        TextView highvValueTextView = listItemView.findViewById(R.id.highValueTextView);
        TextView openValueTextView = listItemView.findViewById(R.id.openValueTextView);
        TextView closeValueTextView = listItemView.findViewById(R.id.closeValueTextView);
        TextView volumeTextView = listItemView.findViewById(R.id.volumeTextView);
        TextView percentChangeTextView = listItemView.findViewById(R.id.percentageChangeTextView);
        Button getChartsButton = listItemView.findViewById(R.id.getChartsButton);

        companyNameTextView.setText(currentItem.getCompanyName());
        priceTextView.setText("$" + currentItem.getPrice());
        dateTextView.setText("Stocks Bought on: " + currentItem.getDate());
        industryTextView.setText("Industry: " + currentItem.getIndustry());
        amountTextView.setText("No of stocks: " + currentItem.getAmount());
        totalPriceTextView.setText("Total Price: $"+currentItem.getTotalPrice());
        lowValueTextView.setText("$" + currentItem.getLowValue());
        highvValueTextView.setText("$" + currentItem.getHighValue());
        closeValueTextView.setText("$" + currentItem.getCloseValue());
        openValueTextView.setText("$" + currentItem.getOpenValue());
        volumeTextView.setText("" + currentItem.getVolume());
        percentChangeTextView.setText(String.format("%.2f%%", currentItem.getPercentchange()));
        percentChangeTextView.setTextColor(currentItem.getPercentageChangeColor());

        getChartsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onGetChartsButtonClickListener != null) {
                    onGetChartsButtonClickListener.onGetChartsClick(position);
                }
            }
        });

        return listItemView;
    }

}


