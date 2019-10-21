package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Home on 24-09-2016.
 */
public class PushNotificationData {

    @SerializedName("Push")
    @Expose
    public List<Push> push = new ArrayList<Push>();
}
