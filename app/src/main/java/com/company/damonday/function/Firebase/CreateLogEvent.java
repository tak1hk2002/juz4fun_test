package com.company.damonday.function.Firebase;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by lamtaklung on 30/1/2017.
 */

public class CreateLogEvent {

    String itemName, contentType, itemID;
    Context context;
    FirebaseAnalytics firebaseAnalytics;

    public CreateLogEvent(FirebaseAnalytics firebaseAnalytics) {
        this.firebaseAnalytics = firebaseAnalytics;
    }

    public void setUserSetting(){

    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public void send(){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, itemID);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, itemName);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}
