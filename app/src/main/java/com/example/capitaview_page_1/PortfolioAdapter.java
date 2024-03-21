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
import java.util.List;

public class PortfolioAdapter extends ArrayAdapter<PortfolioItem> {

    private Context context;
    private List<PortfolioItem> portfolioItemList;

    private OnDeleteButtonClickListener onDeleteButtonClickListener;

    public PortfolioAdapter(Context context, List<PortfolioItem> portfolioItemList) {
        super(context, 0, portfolioItemList);
        this.context = context;
        this.portfolioItemList = portfolioItemList;
    }

    public interface OnDeleteButtonClickListener {
        void onDeleteButtonClick(int position);
    }

    public void setOnDeleteButtonClickListener(OnDeleteButtonClickListener listener) {
        this.onDeleteButtonClickListener = listener;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.portfolio_entry_layout, parent, false);
        }

        PortfolioItem currentItem = portfolioItemList.get(position);

        TextView companyNameTextView = listItemView.findViewById(R.id.companyNameTextView);
        TextView priceTextView = listItemView.findViewById(R.id.priceTextView);
        TextView dateTextView = listItemView.findViewById(R.id.dateTextView);
        TextView industryTextView = listItemView.findViewById(R.id.industryTextView);
        TextView amountTextView = listItemView.findViewById(R.id.amountTextView);
        TextView totalPriceTextView = listItemView.findViewById(R.id.totalPriceTextView);
        Button removeButton = listItemView.findViewById(R.id.removeButton);


        companyNameTextView.setText(currentItem.getCompanyName());
        priceTextView.setText("Price: $" + currentItem.getPrice());
        dateTextView.setText("Date: " + currentItem.getDate());
        industryTextView.setText("Industry: " + currentItem.getIndustry());
        amountTextView.setText("Amount: " + currentItem.getAmount());
        totalPriceTextView.setText("Total Price: $"+currentItem.getTotalPrice());

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteButtonClickListener != null) {
                    onDeleteButtonClickListener.onDeleteButtonClick(position);
                }
            }
        });

        return listItemView;
    }
}

