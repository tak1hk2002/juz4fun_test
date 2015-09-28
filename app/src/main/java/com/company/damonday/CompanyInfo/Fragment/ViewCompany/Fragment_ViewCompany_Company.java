package com.company.damonday.CompanyInfo.Fragment.ViewCompany;

import java.util.ArrayList;

/**
 * Created by lamtaklung on 29/6/15.
 */
public class Fragment_ViewCompany_Company {

    private String  description, address, contactNumber, price, preferredTransport, businessHour;

    Fragment_ViewCompany_Company(){}

    public Fragment_ViewCompany_Company(String description, String address, String contactNumber, String price, String preferredTransport, String businessHour) {
        this.description = description;
        this.address = address;
        this.contactNumber = contactNumber;
        this.price = price;
        this.preferredTransport = preferredTransport;
        this.businessHour = businessHour;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getPrice() {
        return price;
    }

    public String getPreferredTransport() {
        return preferredTransport;
    }

    public String getBusinessHour() {
        return businessHour;
    }
}
