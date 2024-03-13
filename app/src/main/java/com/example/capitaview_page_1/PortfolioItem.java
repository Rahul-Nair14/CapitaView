package com.example.capitaview_page_1;

public class PortfolioItem {

    private String CompanyName, Date, Industry;
    private int Amount;
    private double Price;


    public PortfolioItem(String companyName, double price, String date, String industry, int amount) {
        CompanyName = companyName;
        Price = price;
        Date = date;
        Industry = industry;
        Amount = amount;
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
}