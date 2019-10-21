package com.tatx.userapp.pojos;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Home on 09-09-2016.
 */
public class FavLocation implements Serializable {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("latitude")
    @Expose
    public double latitude;

    @SerializedName("longitude")
    @Expose
    public double longitude;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("status")
    @Expose
    public String status;

    @Override
    public String toString() {
        return "FavLocation{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

}
