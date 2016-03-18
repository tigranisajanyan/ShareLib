package com.picsart.sharelibrary;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class CommonShareController {
    private static final String Title = CommonShareController.class.getSimpleName();

    private Activity activity;

    private String filePath;
    private String fileMimeType;

    private ArrayList<String> supportedPackageNames = new ArrayList<>();

    public CommonShareController(String filePath, Activity activity) {
        this.filePath = filePath;
        this.activity = activity;
        fileMimeType = Utils.checkFileMimeType(filePath);
        supportedPackageNames = Utils.getSupportedType(fileMimeType, activity);
    }

    public void commonShareTo(String packageName) {
        if (filePath != null) {
            if (Utils.isAppInstalled(packageName, activity)) {
                androidNativeShareTo(packageName);
            } else {
                Toast.makeText(activity, R.string.found_error, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, R.string.file_found_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void androidNativeShareTo(String packageName) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_TEXT, Title);
        share.setType(fileMimeType);
        Uri uri = Uri.fromFile(new File(filePath));  //Uri.parse(filePath);
        try {
            share.putExtra(Intent.EXTRA_STREAM, uri);
            share.setPackage(packageName);
            activity.startActivity(share);
        } catch (Exception e) {

        }
    }


    public ArrayList<String> getSupportedPackageNames() {
        return supportedPackageNames;
    }
}
