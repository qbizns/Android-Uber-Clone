package com.tatx.userapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Home on 24-09-2016.
 */
public class PromoListVo {
    @SerializedName("promo")
    @Expose
    public List<Promo> promo = new ArrayList<Promo>();
}
