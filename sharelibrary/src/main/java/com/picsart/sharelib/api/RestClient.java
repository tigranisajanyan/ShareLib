package com.picsart.sharelib.api;

import android.content.Context;

import com.picsart.sharelib.api.service.PhotosApiService;
import com.picsart.sharelibrary.ShareConstants;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by user on 2/24/16.
 */
public class RestClient {
    private static final String BASE_URL = ShareConstants.BASE_URL;
    private PhotosApiService photosApiService;

    private Logger logger = null;
    private static RestClient thisInstance = null;


    private RestClient(Context context) {
        //setup cache
        File httpCacheDirectory = new File(context.getCacheDir(), "responses");
        int cacheSize = 10 * 1024 * 1024; // 10 MB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);

        List<Protocol> protocolList = new ArrayList<Protocol>();
        protocolList.add(Protocol.HTTP_1_1);
        protocolList.add(Protocol.HTTP_2);
        protocolList.add(Protocol.SPDY_3);
        OkHttpClient client = (new OkHttpClient.Builder().addInterceptor(new LoggingInterceptor()).protocols(protocolList).cache(cache).hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        })).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        photosApiService = retrofit.create(PhotosApiService.class);

        logger = Logger.getLogger("RetroFit");
    }


    public PhotosApiService getPhotosApiService() {
        return photosApiService;
    }

    class LoggingInterceptor implements Interceptor {
        @Override public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request().newBuilder().addHeader("app","com.mobilelabs.animator").build();
            long t1 = System.nanoTime();
            logger.info(String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            logger.info(String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            return response;
        }
    }

    public static RestClient getInstance(Context context) {
        if (thisInstance == null) {
            thisInstance = new RestClient(context);
        }
        return thisInstance;
    }
}
