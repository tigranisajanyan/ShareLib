package com.picsart.sharelibrary;

import android.content.SharedPreferences;

/**
 * Created by Tigran on 2/12/16.
 */
public class ShareConstants {

    public static final String[] socials = {"SMS", "Twitter", "Blogger", "Picasa", "Email", "Google+"};
    public static final String[] imageFormat = {"PNG", "JPEG"};
    public static final String[] gifFormat = {"GIF"};
    public static final String[] webmFormat = {"WEBM"};
    public static final String[] mp4Format = {"MP4"};

    public static final String[] sharedContentType = {""};

    public static final String[] mailApps = {"com.google.android.gm", "com.htc.android.mail"};

    public static final String FB_MESSENGER_PACKAGE_NAME = "com.facebook.orca";
    public static final String FB_PACKAGE_NAME = "com.facebook.katana";
    public static final String GPLUS_PACKAGE_NAME = "com.google.android.apps.plus";
    public static final String INSTAGRAM_PACKAGE_NAME = "com.instagram.android";
    public static final String MORE_PACKAGE_OPTIONS = "More";
    public static final String TWITTER_PACKAGE = "com.twitter.android";
    public static final String TUMBLR_PACKAGE = "com.tumblr";
    public static final String WHATSAPP_PACKAGE = "com.whatsapp";
    public static final String SINA_WEIBO_PACKAGE = "com.sina.weibo";
    public static final String TENCENT_WEIBO_PACKAGE = "com.tencent.WBlog";
    public static final String WECHAT_PACKAGE = "com.tencent.mm";
    public static final String QQ_PACKAGE = "com.tencent.mobileqqi";
    public static final String VIBER_PACKAGE = "com.viber.voip";
    public static final String KAKAO_TALK_PACKAGE = "com.kakao.talk";
    public static final String LINE_APP_PACKAGE = "jp.naver.line.android";
    public static final String SKYPE_PACKAGE = "com.skype.raider";
    public static final String HANGOUTS_PACKAGE = "com.google.android.talk";
    public static final String PINTEREST_PACKAGE = "com.pinterest";

    public static final String PICSART_ITEM_URL = "http://picsart.com";

    public static final String DATA_MIME_TYPE_IMAGE = "image/*";
    public static final String DATA_MIME_TYPE_IMAGE_PNG = "image/png";
    public static final String DATA_MIME_TYPE_IMAGE_JPEG = "image/jpeg";
    public static final String DATA_MIME_TYPE_VIDEO = "video/*";
    public static final String DATA_MIME_TYPE_GIF = "image/gif";

    public static final String SHARED_FILES_DESCRITPION = "testing";

    public static final String BASE_URL = "https://api.picsart.com/";

    public static final  String CLIENT_ID = "AnimatorqxVYkRPlrS5Fns4R";
    public static final String CLIENT_SECRET = "hMSqGNgYgq5Nr9uU1KLGnYjzivdsuuR4";
    public static final String SCOPE = "client-basic";
    public static final String GRANT_TYPE = "client_credentials";

    public static final String  PUBLIC_PROFILE   = "public_profile";
    public static final String PUBLISH_ACTION   = "publish_actions";

    public static final String MULTIPART_FORM = "multipart/form-data";

    public static  String TOKEN_PREFERANCE_STRING = "share.lib.pref";

    public static final String PUTEXREAS_STRING = "string.share.extra";

    public static final String TYPE_MP4 = "video/mp4";
    public static final String TYPE_3GP = "video/3gpp";
    public static final String TYPE_Gif = "image/gif";

    public static final String VIDEO = "video";
    public static final String GIF = "gif";
    public static final String IMAGE = "image";
    public static final String NONE = "none";

    public static final String NOTIFY_VIDEO_SHARED = "Video shared";
    public static final String NOTIFY_IMAGE_SHARED = "Image shared";
    public static final String NOTIFY_GIF_SHARED = "Gif shared";
    public static final String REMINDER = "please choose the file";

}
