package com.example.capitaview_page_1;

import android.graphics.Color;

public class ViewActivityItem {
    private String companyName;
    private double price;
    private String date;
    private String industry;
    private int amount;
    private double totalPrice;
    private double openValue;
    private double closeValue;
    private double lowValue;
    private double highValue;
    private int volume;
    private double percentchange;
    private int percentageChangeColor;

    public ViewActivityItem() {
    }

    public ViewActivityItem(String companyName, double price, String date, String industry, int amount, double totalPrice, double openValue,
                            double closeValue, double lowValue, double highValue, int volume, double percentchange) {
        this.companyName = companyName;
        this.price = price;
        this.date = date;
        this.industry = industry;
        this.amount = amount;
        this.totalPrice = totalPrice;
        this.openValue = openValue;
        this.closeValue = closeValue;
        this.lowValue = lowValue;
        this.highValue = highValue;
        this.volume = volume;
        this.percentchange = percentchange;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getOpenValue() {
        return openValue;
    }

    public void setOpenValue(double openValue) {
        this.openValue = openValue;
    }

    public double getCloseValue() {
        return closeValue;
    }

    public void setCloseValue(double closeValue) {
        this.closeValue = closeValue;
    }

    public double getLowValue() {
        return lowValue;
    }

    public void setLowValue(double lowValue) {
        this.lowValue = lowValue;
    }

    public double getHighValue() {
        return highValue;
    }

    public void setHighValue(double highValue) {
        this.highValue = highValue;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public double getPercentchange() {
        return percentchange;
    }

    public void setPercentchange(double percentchange) {
        this.percentchange = percentchange;
    }

    public void setPercentageChangeColor(int percentageChangeColor){
        this.percentageChangeColor = percentageChangeColor;
    }

    public int getPercentageChangeColor(){
        return percentageChangeColor;
    }


}
