package com.example.capitaview_page_1;

//Template for Portfolio Item which is uploaded to the database
public class PortfolioItem {

    private String CompanyName, Date, Industry, itemId;
    private int Amount;
    private double Price, TotalPrice;

    public PortfolioItem() {
        // Default constructor required for Firebase
    }


    public PortfolioItem(String companyName, double price, String date, String industry, int amount, double totalPrice) {
        CompanyName = companyName;
        Price = price;
        Date = date;
        Industry = industry;
        Amount = amount;
        TotalPrice = totalPrice;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getIndustry() {
        return Industry;
    }

    public void setIndustry(String industry) {
        Industry = industry;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        TotalPrice = totalPrice;
    }

}