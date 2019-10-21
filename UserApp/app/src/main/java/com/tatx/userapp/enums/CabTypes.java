package com.tatx.userapp.enums;

import com.tatx.userapp.R;

/**
 * Created by Home on 30-08-2016.
 */
public enum CabTypes {
    ECO("ECO",1,R.drawable.btn_eco_selector,R.drawable.car_1),
    SEDAN("SEDAN",2,R.drawable.btn_sedan_selector,R.drawable.car_2),
    FAMILY("FAMILY",3,R.drawable.btn_familt_selector,R.drawable.car_3),
    VIP("VIP",4,R.drawable.btn_vip_selector,R.drawable.car_4),
    PROMO("PROMO",5,R.drawable.btn_vip_selector,R.drawable.car_5),
    HIGHWAY("HIGHWAY",6,R.drawable.btn_vip_selector,R.drawable.car_5),
    VVIP("VVIP",7,R.drawable.btn_vip_selector,R.drawable.car_5);

    private final int carImageId;
    public String cabType;
    public int cabId;
    public int drawableImg;

    CabTypes(String cabType, int cabId,int drawableImg,int carImageId) {
        this.cabType=cabType;
        this.cabId=cabId;
        this.drawableImg=drawableImg;
        this.carImageId=carImageId;


    }

    public String getCabType() {
        return cabType;
    }

    public int getCabId() {
        return cabId;
    }
    public int getDrawableImg() {
        return drawableImg;
    }

    public int getCarImageId() {
        return carImageId;
    }


    public static CabTypes sliderDetails(int id){

        if (id==1)
                return ECO;

        if (id==2)
                return SEDAN;

        if (id==3)
                return FAMILY;

        if (id==4)
                return VIP;
        if (id==5)
                return PROMO;
        if (id==6)
                return HIGHWAY;
        if (id==7)
                return VVIP;


        return null;
    }


    }
