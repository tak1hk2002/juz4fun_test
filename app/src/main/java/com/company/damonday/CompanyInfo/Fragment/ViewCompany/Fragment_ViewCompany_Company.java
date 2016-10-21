package com.company.damonday.CompanyInfo.Fragment.ViewCompany;

import java.util.ArrayList;

/**
 * Created by lamtaklung on 29/6/15.
 */
public class Fragment_ViewCompany_Company {

    private String  description, address, contactNumber, price, preferredTransport, businessHour, cat, entName;

    Fragment_ViewCompany_Company(){}

    public Fragment_ViewCompany_Company(String description, String address, String contactNumber, String price, String preferredTransport, String businessHour, String cat, String entName) {
        this.description = description;
        this.address = address;
        this.contactNumber = contactNumber;
        this.price = price;
        this.preferredTransport = preferredTransport;
        this.businessHour = businessHour;
        this.cat = cat;
        this.entName = entName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPreferredTransport() {
        return preferredTransport;
    }

    public void setPreferredTransport(String preferredTransport) {
        this.preferredTransport = preferredTransport;
    }

    public String getBusinessHour() {
        return businessHour;
    }

    public void setBusinessHour(String businessHour) {
        this.businessHour = businessHour;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getEntName() {
        return entName;
    }

    public void setEntName(String entName) {
        this.entName = entName;
    }
}
