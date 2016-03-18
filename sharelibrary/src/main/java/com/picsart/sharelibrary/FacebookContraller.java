package com.picsart.sharelibrary;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;


import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.*;
import com.facebook.login.LoginManager;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;

import com.facebook.share.widget.ShareDialog;
import com.picsart.sharelib.api.RestClient;
import com.picsart.sharelib.api.model.Token;
import com.picsart.sharelib.api.model.UploadResponse;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by user on 2/18/16.
 */
public class FacebookContraller {

    private String preference_string = ShareConstants.TOKEN_PREFERANCE_STRING;
    private onSucsseed onSucsseed;
    CallbackManager callbackManager;
    Activity activity;
    Context context;
    SharedPreferences sharedPreferance = null;
    private String url, title, description;

    public FacebookContraller(Activity activity, CallbackManager callbackManager, Context context, String strUrl, String title, String description) {
        this.url = strUrl;
        this.callbackManager = callbackManager;
        this.activity = activity;
        this.context = context;
        this.title = title;
        this.description = description;
        sharedPreferance = context.getSharedPreferences(preference_string, Context.MODE_PRIVATE);
    }

    // get Token
    // upload gif to PicsArt server
    // share gif link to Facebook
    public void startGifSharePoccessing() {

        String CLIENT_ID = ShareConstants.CLIENT_ID;
        String CLIENT_SECRET = ShareConstants.CLIENT_SECRET;
        String SCOPE = ShareConstants.SCOPE;
        String GRANT_TYPE = ShareConstants.GRANT_TYPE;
        if(sharedPreferance.getString(preference_string, null)==null){
            RestClient.getInstance(context).getPhotosApiService().getToken(CLIENT_ID, CLIENT_SECRET, GRANT_TYPE, SCOPE).enqueue(new Callback<Token>() {
                @Override
                public void onResponse(Call<Token> call, Response<Token> response) {
                    try {
                        SharedPreferences.Editor editor = sharedPreferance.edit();
                        editor.putString(preference_string, response.body().accessToken);
                        editor.commit();

                        uploadFileToPicsart();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Token> call, Throwable t) {
                    Toast.makeText(activity, R.string.failedGetAcces, Toast.LENGTH_SHORT).show();
                }
            });

        }else {
            uploadFileToPicsart();
        }
    }


    public void uploadFileToPicsart() {

        File file = new File(url);
        RequestBody fileBody = RequestBody.create(MediaType.parse(ShareConstants.MULTIPART_FORM), file);
        RestClient.getInstance(context).getPhotosApiService().uploadPhoto(fileBody, sharedPreferance.getString(preference_string, null)).enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, final Response<UploadResponse> response) {

                final String urlGIF = response.body().getUrl();
                final Uri uri = Uri.parse(urlGIF);

                shareGifToFacebook(uri);

            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                //TODO Auto Generated Stub
            }
        });
    }


   private void shareGifToFacebook(final Uri uri){
       if (Utils.isAppInstalled(ShareConstants.FB_PACKAGE_NAME, activity)) {
           ShareLinkContent linkContent = new ShareLinkContent.Builder()
                   .setContentTitle(title)
                   .setContentDescription(description)
                   .setContentUrl(uri).build();
           ShareDialog.show(activity, linkContent);
           ShareDialog sharedialog = new ShareDialog(activity);
           sharedialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
               @Override
               public void onSuccess(Sharer.Result result) {
                   onSucsseed.onSucsseed(true);
               }

               @Override
               public void onCancel() {
                   //TODO Auto Generated Stub

               }

               @Override
               public void onError(FacebookException e) {
                   //TODO Auto Generated Stub

               }
           });
       } else {
           List<String> permissions = new ArrayList<>();
           permissions.add(ShareConstants.PUBLISH_ACTION);
           LoginManager.getInstance().logInWithPublishPermissions(activity, permissions);
           LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
               @Override
               public void onSuccess(final LoginResult loginResult) {
                   ShareLinkContent linkContent = new ShareLinkContent.Builder()
                           .setContentTitle(title)
                           .setContentDescription(description)
                           .setContentUrl(uri).build();
                   ShareApi.share(linkContent, new FacebookCallback<Sharer.Result>() {
                       @Override
                       public void onSuccess(Sharer.Result result) {
                           onSucsseed.onSucsseed(true);
                       }

                       @Override
                       public void onCancel() {
                           //TODO Auto Generated Stub

                       }

                       @Override
                       public void onError(FacebookException e) {
                           //TODO Auto Generated Stub

                       }
                   });
               }

               @Override
               public void onCancel() {
               }

               @Override
               public void onError(FacebookException e) {
                   e.printStackTrace();
               }
           });
       }

            }


    public void shareVideoToFacebook() {
        final Uri uri = Uri.fromFile(new File(url));
        //check if app installed
        if (Utils.isAppInstalled(ShareConstants.FB_PACKAGE_NAME, activity)) {
            ShareVideo video = new ShareVideo.Builder()
                    .setLocalUrl(uri)
                    .build();
            ShareVideoContent content = new ShareVideoContent.Builder()
                    .setVideo(video)
                    .build();
            ShareDialog.show(activity, content);
        } else {
            LoginManager.getInstance().logInWithPublishPermissions(activity, Arrays.asList(ShareConstants.PUBLISH_ACTION));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    ShareVideo video = new ShareVideo.Builder()
                            .setLocalUrl(uri)
                            .build();
                    ShareVideoContent content = new ShareVideoContent.Builder()
                            .setVideo(video)
                            .setContentTitle(title)
                            .setContentDescription(description)
                            .build();
                    ShareApi.share(content, new FacebookCallback<Sharer.Result>() {
                        @Override
                        public void onSuccess(Sharer.Result result) {
                            onSucsseed.onSucsseed(true);
                        }

                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onError(FacebookException e) {

                        }
                    });
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException e) {
                    e.printStackTrace();

                }
            });

        }
    }


    public void shareImageToFacebook() {
        //check if app installed
        final Uri uri = Uri.fromFile(new File(url));
        if (Utils.isAppInstalled(ShareConstants.FB_PACKAGE_NAME, activity)) {
            SharePhoto photo = new SharePhoto.Builder()
                    .setImageUrl(uri)
                    .build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();
            ShareDialog.show(activity, content);
        } else {
            LoginManager.getInstance().logInWithPublishPermissions(activity, Arrays.asList(ShareConstants.PUBLISH_ACTION));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    SharePhoto photo = new SharePhoto.Builder()
                            .setImageUrl(uri)
                            .setCaption(description)
                            .build();
                    SharePhotoContent content = new SharePhotoContent.Builder()
                            .addPhoto(photo)
                            .build();
                    ShareApi.share(content, new FacebookCallback<Sharer.Result>() {
                        @Override
                        public void onSuccess(Sharer.Result result) {
                            onSucsseed.onSucsseed(true);
                        }

                        @Override
                        public void onCancel() {
                            //TODO Auto Generated Stub

                        }

                        @Override
                        public void onError(FacebookException e) {
                            //TODO Auto Generated Stub

                        }
                    });
                }

                @Override
                public void onCancel() {
                }

                @Override
                public void onError(FacebookException e) {
                    //TODO Auto Generated Stub

                    e.printStackTrace();
                }
            });
        }
    }


    public static void logOut() {
        try {
            com.facebook.login.LoginManager.getInstance().getInstance().logOut();
        } catch (Exception e) {

        }
    }

    public void setUploadCallbacklistener(onSucsseed l) {
        onSucsseed = l;
    }

    public interface onSucsseed {
        void onSucsseed(boolean isSucseed);
    }
}
