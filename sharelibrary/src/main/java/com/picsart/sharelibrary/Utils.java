package com.picsart.sharelibrary;

import android.util.Log;
import android.webkit.MimeTypeMap;

/**
 * Created by Tigran on 2/15/16.
 */
public class Utils {

    public static Type getMimeType(String url) {
        String stringType = null;
        Type type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);

        if (extension != null) {
            stringType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            Log.d("filetype", stringType);
        }
        if (stringType != null) {
            if (stringType.toLowerCase().contains("image") && !stringType.toLowerCase().contains("gif")) {
                type = Type.IMAGE;
            } else if (stringType.toLowerCase().contains("gif")) {
                type = Type.GIF;
            } else if (stringType.toLowerCase().contains("video")) {
                type = Type.VIDEO;
            }
        } /*else {
            type = Type.IMAGE;
        }*/

        return type;
    }

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

}
