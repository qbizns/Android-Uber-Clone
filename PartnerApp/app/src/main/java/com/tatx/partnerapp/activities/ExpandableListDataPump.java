package com.tatx.partnerapp.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> technology = new ArrayList<String>();
        technology.add("Beats sued for noise-cancelling tech");
        technology.add("Wikipedia blocks US Congress edits");
        technology.add("Google quizzed over deleted links");
        technology.add("Nasa seeks aid with Earth-Mars links");
        technology.add("The Good, the Bad and the Ugly");

        List<String> entertainment = new ArrayList<String>();
        entertainment.add("Goldfinch novel set for big screen");
        entertainment.add("Anderson stellar in Streetcar");
        entertainment.add("Ronstadt receives White House honour");
        entertainment.add("Toronto to open with The Judge");
        entertainment.add("British dancer return from Russia");

        List<String> science = new ArrayList<String>();
        science.add("Eggshell may act like sunblock");
        science.add("Brain hub predicts negative events");
        science.add("California hit by raging wildfires");
        science.add("Rosetta's comet seen in close-up");
        science.add("Secret of sandstone shapes revealed");

        List<String> object = new ArrayList<String>();
        object.add("Eggshell may act like sunblock");
        object.add("Brain hub predicts negative events");
        object.add("California hit by raging wildfires");
        object.add("Rosetta's comet seen in close-up");
        object.add("Secret of sandstone shapes revealed");

        List<String> physics = new ArrayList<String>();
        physics.add("Eggshell may act like sunblock");
        physics.add("Brain hub predicts negative events");
        physics.add("California hit by raging wildfires");
        physics.add("Rosetta's comet seen in close-up");
        physics.add("Secret of sandstone shapes revealed");

        expandableListDetail.put("TECHNOLOGY NEWS", technology);
        expandableListDetail.put("ENTERTAINMENT NEWS", entertainment);
        expandableListDetail.put("SCIENCE & ENVIRONMENT NEWS", science);
        expandableListDetail.put("Object", object);
        expandableListDetail.put("Physics", physics);
        //expandableListDetail.put("SCIENCE & ENVIRONMENT NEWS", science);
       // expandableListDetail.put("SCIENCE & ENVIRONMENT NEWS", science);
        return expandableListDetail;
    }
}
