package com.tatx.partnerapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.abstractclasses.BaseActivity;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.commonutills.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 01-06-2016.
 */
public class FeedbackActivity extends BaseActivity  {
    @BindView(R.id.suggestions)
    TextView suggestions;
    @BindView(R.id.complaints)
    TextView complaints;
    @BindView(R.id.btn_contact)
    TextView contactus;
    private String tripId="0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        setTitleText(Common.getStringResourceText(R.string.feedback));
        tripId=getIntent().getStringExtra(Constants.IntentKeys.TRIP_ID);
    }


    @OnClick(R.id.suggestions)
    void setSuggestions() {
        Intent suggestions = new Intent(this, FeedbackFormActivity.class);
        suggestions.putExtra(Constants.IntentKeys.TRIP_ID,tripId);
        suggestions.putExtra(Constants.IntentKeys.FEEDBACK_TYPE,Constants.SUGGESTION);
        startActivity(suggestions);

    }

    @OnClick(R.id.complaints)
    void setComplaints() {
        Intent complaints = new Intent(this, FeedbackFormActivity.class);
        complaints.putExtra(Constants.IntentKeys.TRIP_ID,tripId);
        complaints.putExtra(Constants.IntentKeys.FEEDBACK_TYPE,Constants.COMPLAINTS);
        startActivity(complaints);
    }

    @OnClick(R.id.btn_contact)
    void setContactus() {
        Intent contact = new Intent(this, FeedbackFormActivity.class);
        contact.putExtra(Constants.IntentKeys.TRIP_ID,tripId);
        contact.putExtra(Constants.IntentKeys.FEEDBACK_TYPE,Constants.CONTACT_US);
        startActivity(contact);

    }


}
