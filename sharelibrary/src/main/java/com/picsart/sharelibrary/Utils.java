package com.picsart.sharelibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tigran on 2/15/16.
 */
public class Utils {

    public static String checkFileMimeType(String filePath) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(filePath);
        if (extension != null) {
            try {
                return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }


    public static String getPath(Intent data, Context context) {

        String path = null;
        try {
            Uri uri = data.getData();
            if (uri != null) {
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    path = cursor.getString(columnIndex);
                } else {
                    path = uri.getPath();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //TODO try to change ACTION_GET_CONTENT to ACTION_PICK and test on all devices.
        if (TextUtils.isEmpty(path)) {
            // failed to get path
            try {
                String uriString = data.getDataString().replaceAll("%3A", "/");
                Uri uri = Uri.parse(uriString);
                if (uri != null) {
                    String[] projection = {MediaStore.Images.Media.DATA};
                    Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
                    if (cursor != null) {
                        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        path = cursor.getString(columnIndex);
                    } else {
                        path = uri.getPath();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return path;
    }


    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }


    public enum SupportedType {
        IMAGE {
            @Override
            public String toString() {
                return "image/*";
            }
        },
        GIF {
            @Override
            public String toString() {
                return "image/gif";
            }
        },
        VIDEO {
            @Override
            public String toString() {
                return "video/*";
            }
        },
        NONE {
            @Override
            public String toString() {
                return "";
            }
        }
    }


    public static ArrayList<String> getSupportedType(String url, Context context) {
        String stringType = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        ArrayList<String> packageNames = new ArrayList<>();
        packageNames.add(ShareConstants.FB_PACKAGE_NAME);
        if (extension != null) {
            stringType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            if (stringType != null) {
                PackageManager pm = context.getPackageManager();
                Intent sendIntent = new Intent(Intent.ACTION_SEND);

                List<ResolveInfo> resolveInfoList = pm.queryIntentActivities(sendIntent, 0);
                for (int i = 0; i < resolveInfoList.size(); i++) {
                    ResolveInfo ri = resolveInfoList.get(i);
                    String packageName = ri.activityInfo.packageName;
                    packageNames.add(packageName);

                }
                if (!isAppInstalled(ShareConstants.FB_PACKAGE_NAME, context)) {
                    packageNames.add(ShareConstants.FB_PACKAGE_NAME);
                }
            }
        } else {
            return null;
        }
        return packageNames;
    }

    public static ArrayList<String> getAllSupportedPackageNames(String fileMimeType, Context context) {

        if (fileMimeType == null) return null;
        PackageManager pm = context.getPackageManager();

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType(fileMimeType);

        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
        ArrayList<String> packageNames = new ArrayList<>();
        for (int i = 0; i < resInfo.size(); i++) {
            ResolveInfo ri = resInfo.get(i);
            String packageName = ri.activityInfo.packageName;
            packageNames.add(packageName);
        }
        return packageNames;
    }

    public static List<Package> chekForAppShare(List<String> inputPackageName, Context context) {

        List<Package> packageList = new ArrayList<>();
        for (int i = 0; i < inputPackageName.size(); i++) {
            packageList.add(new Package());
            packageList.get(i).setPackagename(inputPackageName.get(i));
            if (isAppInstalled(inputPackageName.get(i), context)) {
                packageList.get(i).setIsInstalled(true);
                if (getPackagesFrom(context, ShareConstants.DATA_MIME_TYPE_IMAGE).contains(packageList.get(i).getPackagename())) {
                    packageList.get(i).setIsImageSupported(true);
                }
                if (getPackagesFrom(context, ShareConstants.DATA_MIME_TYPE_GIF).contains(packageList.get(i).getPackagename())) {
                    packageList.get(i).setIsGifSupported(true);
                }
                if (getPackagesFrom(context, ShareConstants.DATA_MIME_TYPE_VIDEO).contains(packageList.get(i).getPackagename())) {
                    packageList.get(i).setIsVideoSupported(true);
                }
                if (packageList.get(i).getPackagename().contains(ShareConstants.INSTAGRAM_PACKAGE_NAME)) {
                    packageList.get(i).setIsImageSupported(true);
                    packageList.get(i).setIsVideoSupported(true);
                    packageList.get(i).setIsGifSupported(false);
                }
            }
        }
        return packageList;
    }

    public static boolean isAppInstalled(String packagname, Context context) {
        PackageManager pm = context.getPackageManager();
        boolean isInstalled;
        try {
            pm.getPackageInfo(packagname, PackageManager.GET_ACTIVITIES);
            isInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    private static ArrayList<String> getPackagesFrom(Context context, String type) {

        ArrayList<String> packageNames = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType(type);
        List<ResolveInfo> resolveInfoList = pm.queryIntentActivities(sendIntent, 0);
        for (int i = 0; i < resolveInfoList.size(); i++) {
            ResolveInfo ri = resolveInfoList.get(i);

            String packageName = ri.activityInfo.packageName;
            packageNames.add(packageName);
        }
        return packageNames;
    }



    public static void launchNativeApps(Activity activity,String title,SupportedType fileMimeType,String filePath ) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_TEXT, title);
        share.setType(fileMimeType.toString());
        Uri uri = null;
        if (filePath!=null) {
            File media = new File(filePath);
            uri = Uri.fromFile(media);
        }
        try {
            share.putExtra(Intent.EXTRA_STREAM, uri);
            activity.startActivity(Intent.createChooser(share, "Share Image!"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
