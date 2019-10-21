package com.tatx.userapp.helpers;

/**
 * Created by Venkateswarlu SKP on 23-09-2016.
 */
public class UserProfile
{

    private String profilePicUrl;
    private String firstName;
    private String lastName;
    private String email;
    private String customerCountryCode;
    private String phoneNumber;
    private int languageCode;
    private String emergencyContactName;


    private String emergencyCountryCode;
    private String emergencyContactNumber;


    public UserProfile(String profilePicUrl, String firstName, String lastName, String email, String customerCountryCode, String phoneNumber, int languageCode, String emergencyContactName, String emergencyCountryCode, String emergencyContactNumber)
    {

        this.profilePicUrl = profilePicUrl;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.customerCountryCode = customerCountryCode;
        this.phoneNumber = phoneNumber;
        this.languageCode = languageCode;
        this.emergencyContactName = emergencyContactName;
        this.emergencyCountryCode = emergencyCountryCode;
        this.emergencyContactNumber = emergencyContactNumber;
    }


    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getCustomerCountryCode() {
        return customerCountryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getLanguageCode() {
        return languageCode;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public String getEmergencyContactNumber() {
        return emergencyContactNumber;
    }

    public String getEmergencyCountryCode() {
        return emergencyCountryCode;
    }

}
