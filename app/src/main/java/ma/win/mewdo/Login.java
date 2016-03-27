package ma.win.mewdo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;


public class Login extends Activity {

    CallbackManager callbackManager;
    LoginButton loginButton;
    SharedPreferences sharedPreferences ;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");

        final Context app = this.getApplicationContext();
        callbackManager = CallbackManager.Factory.create();

        sharedPreferences = getSharedPreferences("PROFILE", MODE_PRIVATE);

        if(isLogged())
        {
            Intent mainActivity = new Intent(this,MainActivity.class);
            startActivity(mainActivity);
        }


        //set fb permissions
        loginButton.setReadPermissions(Arrays.asList("public_profile,email"));

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {

                final ProfileTracker mProfileTracker;
                progressBar.setVisibility(View.VISIBLE);

                // new Login code

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {

                        Bundle bFacebookData = getFacebookData(jsonObject);

                        String fullName = bFacebookData.getString("first_name") + " " + bFacebookData.get("last_name");
                        String email = bFacebookData.getString("email");
                        String user_id = loginResult.getAccessToken().getUserId();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("LOGGED", "TRUE");
                        editor.putString("COMPLETE", "FALSE");
                        editor.putString("NAME", fullName);
                        editor.putString("EMAIL", email);
                        String profileImgUrl = "https://graph.facebook.com/" +
                                user_id + "/picture?type=large";
                        editor.putString("PICTURE", profileImgUrl);
                        editor.apply();

                        // register the user to the backed
                        RequestParams params = new RequestParams();
                        params.put("email",email);
                        params.put("full_name",fullName);
                        params.put("avatar",profileImgUrl);
                        params.put("facebook_id",user_id);
                        RestClient.post("loginorregister",params, new JsonHttpResponseHandler(){

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                               try{
                                   // put the token to shared Bundle 3/22/2016
                                   String backend_token = response.get("token").toString();


                               }catch (JSONException e){
                                   e.getStackTrace();
                               }

                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {

                            }


                        });
                        // end of registry

                        Intent interests = new Intent(app,SetInterests.class);
                        startActivity(interests);

                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location");
                request.setParameters(parameters);
                request.executeAsync();

                // end of new Login code


                String userId = loginResult.getAccessToken().getUserId();
                String accessToken = loginResult.getAccessToken().getToken();


            }

            private Bundle getFacebookData(JSONObject object){
                Bundle bundle = new Bundle();
                try {
                    String id = object.getString("id");

                    try {
                        URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                        bundle.putString("profile_pic", profile_pic.toString());

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        return null;
                    }

                    bundle.putString("idFacebook", id);
                    if (object.has("first_name"))
                        bundle.putString("first_name", object.getString("first_name"));
                    if (object.has("last_name"))
                        bundle.putString("last_name", object.getString("last_name"));
                    if (object.has("email"))
                        bundle.putString("email", object.getString("email"));
                    if (object.has("gender"))
                        bundle.putString("gender", object.getString("gender"));
                    if (object.has("birthday"))
                        bundle.putString("birthday", object.getString("birthday"));
                    if (object.has("location"))
                        bundle.putString("location", object.getJSONObject("location").getString("name"));
                }catch (JSONException e){
                    e.printStackTrace();
                }

                return bundle;
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    public boolean isLogged()
    {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
