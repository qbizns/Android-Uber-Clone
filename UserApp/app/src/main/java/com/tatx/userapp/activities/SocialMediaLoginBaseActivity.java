package com.tatx.userapp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.tatx.userapp.abstractclasses.BaseActivity;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.commonutills.Constants;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by user on 24-06-2016.
 */
public abstract class SocialMediaLoginBaseActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 888;
    private TwitterAuthClient twitterAuthClient;

/*
    private static final String TWITTER_KEY = "L12ecpXldANR8TTycz0QcOIec";
    private static final String TWITTER_SECRET = "2TtD87oWERfyjL0ykZDtSvEvslLAoCjncX62b9y3Z0Rk9tIb5r";*/

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /*TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Crashlytics(),new Twitter(authConfig));*/

         /*Facebook Starts*/
        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();



        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {


                Common.Log.i("loginResult.toString() : "+loginResult.toString());

                Common.Log.i("loginResult.getAccessToken().getToken() : "+loginResult.getAccessToken().getToken());

                Common.Log.i("loginResult.getAccessToken().getUserId() : "+loginResult.getAccessToken().getUserId());


                final String socialMediaType = Constants.SocialMediaTypes.FACEBOOK;
                final String socialMediaToken = loginResult.getAccessToken().getToken();
//                final String socialMediaToken = loginResult.getAccessToken().getUserId();

                Common.Log.i("? - loginResult.getAccessToken().getUserId() : "+loginResult.getAccessToken().getUserId());


                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback()
                        {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response)
                            {

                                try
                                {

                                    String id = object.getString("id");
                                    Common.Log.i("id : " + id);

                                    String name = object.getString("name");
                                    Common.Log.i("name : " + name);

                                    String email = object.getString("email");
                                    Common.Log.i("email : " + email);

                                    String socialProfilePicUrl = "http://graph.facebook.com/" + id + "/picture";

                                    onSocialMediaLoginSuccess(socialMediaType, socialMediaToken, socialProfilePicUrl, getFLNames(name)[0], getFLNames(name)[1], email,id);



                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                    LoginManager.getInstance().logOut();

                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();

                LoginManager.getInstance().logOut();


            }

            @Override
            public void onCancel()
            {
                Common.Log.i("Inside Facebook onCancel().");

            }

            @Override
            public void onError(FacebookException exception)
            {
                exception.printStackTrace();

                Common.Log.i("Inside Facebook onError().");

                Common.Log.i("exception.toString() : "+exception.toString());

                Common.Log.i("Inside onError().");

                Common.customToast(getApplicationContext(),"exception.toString() : "+exception.toString());
                LoginManager.getInstance().logOut();


            }
        });

    /*Facebook Ends*/



        /*Google Plus Starts*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    /*Google Plus*/



        /*Google Plus*/


        /*Twitter*/
       /* TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));*/

        twitterAuthClient = new TwitterAuthClient();

        /*Twitter*/




    }

    private String[] getFLNames(String name)
    {

        String[] names = name.split(" ");

        int size = names.length;

        String firstName = size > 0 ? name.split(" ")[0] : "";
        String lastName = size > 1 ? name.split(" ")[1] : "";

        return new String[]{firstName,lastName};

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        twitterAuthClient.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);

    }



    private void handleSignInResult(GoogleSignInResult result)
    {
        Common.Log.i("handleSignInResult:" + result.isSuccess());

        if (result.isSuccess())
        {
            // Signed in successfully, show authenticated UI.
            final GoogleSignInAccount acct = result.getSignInAccount();


            final String id = acct.getId();

            Common.Log.i("acct.getId() : "+acct.getId());
            Common.Log.i("acct.getIdToken() : "+acct.getIdToken());
            Common.Log.i("acct.getPhotoUrl() : "+acct.getPhotoUrl());
            Common.Log.i("acct.getDisplayName() : " + acct.getDisplayName());
            Common.Log.i("acct.getEmail() : " + acct.getEmail());


            final String socialMediaType = Constants.SocialMediaTypes.GOOGLE_PLUS;

//            final String socialMediaToken = acct.getIdToken();

            final String socialProfilePicUrl = acct.getPhotoUrl() != null ? acct.getPhotoUrl().toString() : null;




/*

            onSocialMediaLoginSuccess(socialMediaType, socialMediaToken, socialProfilePicUrl, getFLNames(acct.getDisplayName())[0], getFLNames(acct.getDisplayName())[1], acct.getEmail());

            googleSignOut();
*/


            new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {

                String scopes = "oauth2:profile email";
                String token = null;
                try
                {
                    token = GoogleAuthUtil.getToken(getApplicationContext(), acct.getEmail(), scopes);


                }
                catch (IOException e)
                {

                    Common.Log.i(e.getMessage());

                    googleSignOut();

                }
                catch (UserRecoverableAuthException e)
                {
//                    startActivityForResult(e.getIntent(), RC_SIGN_IN);

                    e.printStackTrace();

                    Common.Log.i("? - e.toString() : "+e.toString());

                    googleSignOut();




                }
                catch (GoogleAuthException e)
                {
                    googleSignOut();

                    Common.Log.i("? - "+e.getMessage());
                }
                return token;
            }

            @Override
            protected void onPostExecute(String socialMediaToken)
            {
                onSocialMediaLoginSuccess(socialMediaType, socialMediaToken, socialProfilePicUrl, getFLNames(acct.getDisplayName())[0], getFLNames(acct.getDisplayName())[1], acct.getEmail(), id);

                googleSignOut();


            }
        }.execute();




        }
        else
        {

            Common.Log.i("? - result.isSuccess() : "+result.isSuccess());

        }

    }

    private void googleSignOut()
    {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                        Common.Log.i("? - SignOut Successfully.");

                    }
                });
    }


    public abstract void onSocialMediaLoginSuccess(String socialMediaType, String socialMediaToken, String profilePicUrl, String fName, String lName, String emailId, String id);


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {

        Common.Log.i("onConnectionFailed:" + connectionResult);

    }



    public void loginWithFacebookFunctionality(View view)
    {
        Common.Log.i("loginWithFacebookFunctionality");

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"));

    }

    public void loginWithGooglePlusFunctionality(View view)
    {
        Common.Log.i("loginWithGooglePlusFunctionality");

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void loginWithTwitterFunctionality(View view)
    {
        Common.Log.i("loginWithTwitterFunctionality");

        twitterAuthClient.authorize(this, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()

                Log.d("Twitter ", "Login sucessfull");
                TwitterSession twitterSession = result.data;

                String twitterUserName = twitterSession.getUserName();
                long twitterUserid = twitterSession.getUserId();


                TwitterAuthToken authToken = twitterSession.getAuthToken();
                String twitterToken = authToken.token;
                String twitterSecret = authToken.secret;

                final String socialMediaType = Constants.SocialMediaTypes.TWITTER;
                final String socialMediaToken = twitterToken;


                Common.Log.i("twitterUserName : " + twitterUserName);
                Common.Log.i("twitterToken : " + twitterToken);
                Common.Log.i("twitterSecret : " + twitterSecret);


                Twitter.getApiClient(twitterSession).getAccountService()
                        .verifyCredentials(true, false, new Callback<User>() {

                            @Override
                            public void failure(TwitterException e) {

                            }

                            @Override
                            public void success(Result<User> userResult) {

                                User user = userResult.data;


                                try {

                                    long id = user.id;
                                    String socialProfilePicUrl = user.profileImageUrl;
                                    String name = user.name;

                                    Log.d("twitterImage", socialProfilePicUrl);
                                    Log.d("name", user.name);
//                                  Log.d("email",user.email);
                                    Log.d("des", user.description);
                                    Log.d("followers ", String.valueOf(user.followersCount));
                                    Log.d("createdAt", user.createdAt);


                                    onSocialMediaLoginSuccess(socialMediaType, socialMediaToken, socialProfilePicUrl, name, "", "", String.valueOf(id));


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }

                        });


            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });



    }



}
