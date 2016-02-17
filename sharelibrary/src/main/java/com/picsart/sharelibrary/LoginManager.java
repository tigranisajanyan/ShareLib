package com.picsart.sharelibrary;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * This singleton class handles access, validation
 * and OAuth 2.0 Token management.
 *
 * @author Tigran Isajanyan on 4/20/15.
 */
public class LoginManager {

    private static final String LOG_TAG = LoginManager.class.getSimpleName();

    private static final String TOKEN_URL = "https://api.picsart.com/oauth2/token";
    private static final String CLIENT_ID = "AnimatorqxVYkRPlrS5Fns4R";
    private static final String CLIENT_SECRET = "hMSqGNgYgq5Nr9uU1KLGnYjzivdsuuR4";
    private static final String SCOPE = "client-basic";
    private static final String GRANT_TYPE = "client_credentials";


    //private String s = "MvLuATDEBt6bTbxohKhgTZaamj6Ces2oAnimatorqxVYkRPlrS5Fns4R";

    public static final int ACCESS_TOKEN_REQUEST_SUCCESS_CODE = 101;
    public static final int ACCESS_TOKEN_REQUEST_FAILURE_CODE = 201;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private UserRequest userRequest;
    private Activity activity;
    private String accessToken;

    public LoginManager(Activity activity) {
        this.activity = activity;
    }

    public synchronized void getAccessToken() {
        RequestQueue queue = Volley.newRequestQueue(activity);
        StringRequest sr = new StringRequest(Request.Method.POST, TOKEN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(LOG_TAG, "login_user: " + response);
                JSONObject jsOOb;
                try {
                    jsOOb = new JSONObject(response);
                    accessToken = jsOOb.getString("access_token");
                    userRequest.onRequestReady(ACCESS_TOKEN_REQUEST_SUCCESS_CODE, accessToken);
                } catch (JSONException e) {
                    userRequest.onRequestReady(ACCESS_TOKEN_REQUEST_FAILURE_CODE, response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                userRequest.onRequestReady(ACCESS_TOKEN_REQUEST_FAILURE_CODE, error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("client_id", CLIENT_ID);
                params.put("client_secret", CLIENT_SECRET);
                params.put("grant_type", GRANT_TYPE);
                params.put("scope", SCOPE);
                return params;
            }
        };
        queue.add(sr);
    }


    public interface UserRequest {
        void onRequestReady(int requestNumber, String token);
    }

    public void setOnRequestReadyListener(UserRequest userRequest) {
        this.userRequest = userRequest;
    }

    public boolean verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            return true;
        }
        return false;
    }

}
