package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Home on 09-09-2016.
 */
public class GetSavedLocationVo implements Serializable {

    @SerializedName("recent_locations")
    @Expose
    public ArrayList<RecentLocation> recentLocations = new ArrayList<RecentLocation>();
    @SerializedName("fav_locations")
    @Expose
    public ArrayList<FavLocation> favLocations = new ArrayList<FavLocation>();
}
