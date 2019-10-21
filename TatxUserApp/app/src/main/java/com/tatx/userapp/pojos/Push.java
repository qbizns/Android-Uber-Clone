package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Home on 24-09-2016.
 */
public class Push implements Serializable {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("message_type")
    @Expose
    public String messageType;
    @SerializedName("message_title")
    @Expose
    public String messageTitle;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("send_to_roles")
    @Expose
    public String sendToRoles;
    @SerializedName("send_to")
    @Expose
    public String sendTo;
    @SerializedName("image")
    @Expose
    public String image;

}
