package com.picsart.sharelibrary;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 3/16/16.
 */
public class Package {

    private String packagename;

    private boolean isInstalled;
    private boolean isImageSupported;
    private boolean isGifSupported;
    private boolean isVideoSupported;
    private boolean isNoneSupported;


    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public boolean isInstalled() {
        return isInstalled;
    }

    public void setIsInstalled(boolean isInstalled) {
        this.isInstalled = isInstalled;
    }

    public boolean isImageSupported() {
        return isImageSupported;
    }

    public void setIsImageSupported(boolean isImageSupported) {
        this.isImageSupported = isImageSupported;
    }

    public boolean isGifSupported() {
        return isGifSupported;
    }

    public void setIsGifSupported(boolean isGifSupported) {
        this.isGifSupported = isGifSupported;
    }

    public boolean isVideoSupported() {
        return isVideoSupported;
    }

    public void setIsVideoSupported(boolean isVideoSupported) {
        this.isVideoSupported = isVideoSupported;
    }

    public boolean isNoneSupported() {
        return isNoneSupported;
    }

    public void setIsNoneSupported(boolean isNoneSupported) {
        this.isNoneSupported = isNoneSupported;
    }

    public String suporttedTypes(){
        List<String> list = new ArrayList<>();
        if(isGifSupported){
            list.add(ShareConstants.GIF);
        }
        if(isImageSupported){
            list.add(ShareConstants.IMAGE);
        }
        if(isVideoSupported){
            list.add(ShareConstants.VIDEO);
        }
        if(isNoneSupported){
            list.add(ShareConstants.NONE);
        }
        String  suporttedTypes ="";
        for(String item : list){
            suporttedTypes+=item+" ";
        }
        return  suporttedTypes;
    }

    public void getInfo() {

        Log.d("packageInfo", "APP is Installed= " + isInstalled + "  Supported Types= " + suporttedTypes());

    }

}
