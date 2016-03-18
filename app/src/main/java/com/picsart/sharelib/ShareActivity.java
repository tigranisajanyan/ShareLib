package com.picsart.sharelib;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.picsart.sharelibrary.FacebookContraller;
import com.picsart.sharelibrary.ShareConstants;
import com.picsart.sharelibrary.Utils;


///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////
//EXAMPLE: SHARE FILES TO FACEBOOK
///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////

public class ShareActivity extends AppCompatActivity {

    private String filePathForShare;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_share);

        findViewById(R.id.shareFileToFacebook).setOnClickListener(shareFileToFacebook);
        filePathForShare = getIntent().getExtras().getString(ShareConstants.PUTEXREAS_STRING);
    }


    View.OnClickListener shareFileToFacebook = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Utils.getMimeType(filePathForShare) == ShareConstants.TYPE_MP4 || Utils.getMimeType(filePathForShare) == ShareConstants.TYPE_3GP) {
                shareVideoToFacebook();
            } else if (Utils.getMimeType(filePathForShare) == ShareConstants.TYPE_Gif) {
                shareGifToFacebook();
            } else {
                shareImageToFacebook();
            }
        }
    };


    public void shareVideoToFacebook() {
        if (filePathForShare != null) {
            callbackManager = CallbackManager.Factory.create();
            FacebookContraller shareob = new FacebookContraller(ShareActivity.this, callbackManager, getApplicationContext(), filePathForShare,"title",ShareConstants.SHARED_FILES_DESCRITPION);
            shareob.shareVideoToFacebook();
            shareob.setUploadCallbacklistener(new FacebookContraller.onSucsseed() {
                @Override
                public void onSucsseed(boolean isSucseed) {
                    Toast.makeText(ShareActivity.this, ShareConstants.NOTIFY_VIDEO_SHARED, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(ShareActivity.this, ShareConstants.REMINDER, Toast.LENGTH_SHORT).show();
        }
    }

    public void shareGifToFacebook() {
        callbackManager = CallbackManager.Factory.create();
        FacebookContraller shareob = new FacebookContraller(ShareActivity.this, callbackManager, getApplicationContext(), filePathForShare,"title",ShareConstants.SHARED_FILES_DESCRITPION);
        shareob.startGifSharePoccessing();
        shareob.setUploadCallbacklistener(new FacebookContraller.onSucsseed() {
            @Override
            public void onSucsseed(boolean isSucseed) {
                Toast.makeText(ShareActivity.this, ShareConstants.NOTIFY_GIF_SHARED, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void shareImageToFacebook() {
        if (filePathForShare != null) {
            callbackManager = CallbackManager.Factory.create();
            FacebookContraller shareob = new FacebookContraller(ShareActivity.this, callbackManager,getApplicationContext(), filePathForShare,"title",ShareConstants.SHARED_FILES_DESCRITPION);
            shareob.shareImageToFacebook();
            shareob.setUploadCallbacklistener(new FacebookContraller.onSucsseed() {
                @Override
                public void onSucsseed(boolean isSucseed) {
                    Toast.makeText(ShareActivity.this, ShareConstants.NOTIFY_IMAGE_SHARED, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(ShareActivity.this, ShareConstants.REMINDER, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
