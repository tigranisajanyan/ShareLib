package com.picsart.sharelib;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.facebook.FacebookSdk;
import com.picsart.sharelibrary.ShareConstants;
import com.picsart.sharelibrary.Utils;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_FILES = 200;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main);

        findViewById(R.id.chooseFile).setOnClickListener(chooseFile);
        findViewById(R.id.nextToShare).setOnClickListener(nextActivityToShare);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_FILES) {
                filePath = Utils.getPath(data, MainActivity.this);

            } else {

            }
        }
    }

    View.OnClickListener chooseFile = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, REQUEST_FILES);
        }
    };


    View.OnClickListener nextActivityToShare = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           /* Intent intent = new Intent(MainActivity.this, ShareActivity.class);
            intent.putExtra(ShareConstants.PUTEXREAS_STRING, filePath);
            startActivity(intent);*/

/*
            //////Usage
            CommonShareController shareContraller = new CommonShareController(filePath, MainActivity.this);
            shareContraller.commonShareTo(ShareConstants.FB_MESSENGER_PACKAGE_NAME);*/

            Utils.launchNativeApps(MainActivity.this,"testing", Utils.SupportedType.GIF,null);

        }
    };


}



