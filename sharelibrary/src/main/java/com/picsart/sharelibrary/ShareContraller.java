package com.picsart.sharelibrary;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Tigran on 10/30/15.
 */
public class ShareContraller {

    private static final String Title = ShareContraller.class.getSimpleName();
    private static final String MARKET_PREFIX = "market://details?id=";
    public static final String url1 = "http://cdn76.picsart.com/187889742003202.gif";

    private Activity activity;

    private String filePath;
    private String fileMimeType;

    private ArrayList<String> supportedPackageNames = new ArrayList<>();

    public ShareContraller(String filePath, Activity activity) {
        this.filePath = filePath;
        this.activity = activity;
        fileMimeType = Utils.checkFileMimeType(filePath);
        supportedPackageNames = PackageContraller.getAllSupportedPackageNames(fileMimeType, activity);
    }

    public void shareTo(String packageName) {
        if (filePath != null) {
            if (PackageContraller.isPackageExisted(activity, packageName)) {
                /*if (packageName == ShareConstants.FB_PACKAGE_NAME) {
                    shareFacebook(activity);
                } else {*/
                    androidNativeShare(packageName);
                //}
            } else {
                Toast.makeText(activity, "R.string.messege_application_doesnt_found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, "R.string.messege_file_doesnt_found", Toast.LENGTH_SHORT).show();
        }
    }

    private void androidNativeShare(String packageName) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_TEXT, Title);
        share.setType(fileMimeType);
        Uri uri = Uri.fromFile(new File(filePath));  //Uri.parse(filePath);
        try {
            share.putExtra(Intent.EXTRA_STREAM, uri);
            share.setPackage(packageName);
            activity.startActivity(share);
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(MARKET_PREFIX + packageName));
            activity.startActivity(intent);
        }
    }

    private void shareFacebook(Activity activity) {
        FacebookSdk.sdkInitialize(activity);
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder().setContentTitle(Title).setContentDescription(Title).
                    setContentUrl(Uri.parse(filePath)).build();
            ShareDialog.show(activity, linkContent);
        }
    }

    public void shareVideo() {
        FacebookSdk.sdkInitialize(activity);
        Uri videoFileUri = Uri.parse(filePath);
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareVideo video = new ShareVideo.Builder()
                    .setLocalUrl(videoFileUri)
                    .build();
            ShareVideoContent content = new ShareVideoContent.Builder()
                    .setVideo(video)
                    .build();
            ShareDialog.show(activity, content);
        }
    }

    public ArrayList<String> getSupportedPackageNames() {
        return supportedPackageNames;
    }


}
