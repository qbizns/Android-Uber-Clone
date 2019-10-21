package com.tatx.partnerapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Home on 14-12-2016.
 */
public class NotificationsVo {
    @SerializedName("Push")
    @Expose
    public List<Push> push = null;
}
