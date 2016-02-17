package com.picsart.sharelib;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import com.picsart.sharelibrary.ShareConstants;
import com.picsart.sharelibrary.ShareContraller;

public class MainActivity extends AppCompatActivity {

    public static final String url1 = "http://cdn76.picsart.com/187889742003202.gif";

    private String aa = Environment.getExternalStorageDirectory() + "/IMG_20160208_104319386.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*LoginManager loginManager = new LoginManager(MainActivity.this);
        loginManager.getAccessToken();
        loginManager.setOnRequestReadyListener(new LoginManager.UserRequest() {
            @Override
            public void onRequestReady(int requestNumber, String token) {
                final UploadFileToPicsart uploadFileToPicsart = new UploadFileToPicsart(MainActivity.this, token, aa);
                uploadFileToPicsart.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                uploadFileToPicsart.setOnUploadedListener(new UploadFileToPicsart.ImageUploaded() {
                    @Override
                    public void uploadIsDone(boolean uploaded, String messege) {
                        if (uploaded) {
                            String url = uploadFileToPicsart.getUploadedImageUrl();
                            Log.d("gaga", messege + "  " + url);

                            ShareContraller shareContraller = new ShareContraller(url, MainActivity.this);
                            shareContraller.shareTo(ShareConstants.FB_PACKAGE_NAME);
                        }
                    }
                });
            }
        });*/

        ShareContraller shareContraller = new ShareContraller(aa, MainActivity.this);
        shareContraller.shareTo(ShareConstants.FB_PACKAGE_NAME);
    }
}
