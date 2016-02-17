package com.picsart.sharelibrary;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by Tigran on 11/24/15.
 */
public class UploadFileToPicsart extends AsyncTask<Void, Integer, JSONObject> {

    private static final String UPLOAD_PHOTO_TO_PICSART = "https://api.picsart.com/files?token=";

    private Context context;
    private InputStream is = null;
    private ImageUploaded imageUploaded;
    private ProgressDialog progressDialog;

    private volatile JSONObject jObj = null;
    private static boolean cancelFlag = false;

    private String json = "";
    private String filePath;
    private String uploadedImageUrl;
    private String accessToken;

    public UploadFileToPicsart(Context context, String accessToken, String filePath) {
        this.filePath = filePath;
        this.accessToken = accessToken;
        this.context = context;

    }

    public static void setCancelFlag(boolean cancelFlag) {
        UploadFileToPicsart.cancelFlag = cancelFlag;
    }


    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.d("Upload", " Uploading Picture...");
    }

    @Override
    protected synchronized JSONObject doInBackground(Void... params) {

        final int[] iter = new int[1];

        Looper.prepare();

        try {
            final File file = new File(filePath);
            final long[] totalSize = new long[1];
            totalSize[0] = file.length();
            final HttpClient httpClient = new DefaultHttpClient();
            String url = UPLOAD_PHOTO_TO_PICSART + accessToken;
            MultiPartEntityMod multipartContent = null;
            try {
                multipartContent = new MultiPartEntityMod(new ProgressListener() {
                    @Override
                    public boolean doneFlag(boolean b) {
                        return false;
                    }

                    @Override
                    public void transferred(long num) {
                        long checker = -1;
                        if (UploadFileToPicsart.this.isCancelled()) {
                            httpClient.getConnectionManager().shutdown();
                        }
                        if (num != checker && num % 2 == 0) {
                            checker = num;
                            publishProgress((int) ((num / (float) totalSize[0] * 100)), iter[0]);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            multipartContent.addPart("file", new FileBody(file));

            totalSize[0] = multipartContent.getContentLength();

            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("app", "com.mobilelabs.animator");
            HttpContext httpContext = new BasicHttpContext();
            httpPost.setEntity(multipartContent);
            HttpResponse response = null;
            try {
                response = httpClient.execute(httpPost, httpContext);
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return null;
            }
            HttpEntity httpEntity = response.getEntity();
            is = httpEntity.getContent();


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();

            json = sb.toString();
            Log.e("JSONStr", json);
        } catch (Exception e) {
            e.getMessage();
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        try {
            jObj = new JSONObject(json);
        } catch (Exception e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        iter[0]++;
        // Return JSON String
        return jObj;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        if (cancelFlag) {
            UploadFileToPicsart.this.cancel(true);
            cancelFlag = false;
        }
        Log.d("Uploaded", "photo #" + progress[1] + " done " + progress[0] + "%");
        progressDialog.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(JSONObject sResponse) {
        progressDialog.setProgress(100);
        if (sResponse != null) {
            try {
                Log.d("response Upload", sResponse.toString());
                uploadedImageUrl = sResponse.getString("url");
                imageUploaded.uploadIsDone(true, "success");
            } catch (Exception e) {
                Log.e(e.getClass().getName(), e.getMessage(), e);
                imageUploaded.uploadIsDone(false, "error");
            }
        }
        progressDialog.dismiss();
    }

    private static class MultiPartEntityMod extends MultipartEntity {

        private final ProgressListener listener;

        public MultiPartEntityMod(final ProgressListener listener) {
            super();
            this.listener = listener;
        }

        public MultiPartEntityMod(final HttpMultipartMode mode, final ProgressListener listener) {
            super(mode);
            this.listener = listener;
        }

        public MultiPartEntityMod(HttpMultipartMode mode, final String boundary, final Charset charset, final ProgressListener listener) {
            super(mode, boundary, charset);
            this.listener = listener;
        }

        @Override
        public void writeTo(final OutputStream outstream) throws IOException {
            super.writeTo(new CountingOutputStream(outstream, this.listener));
        }

        /**
         * This class serves for counting progress of transferred streams.
         */
        private class CountingOutputStream extends FilterOutputStream {

            private final ProgressListener listener;
            private long transferred;

            public CountingOutputStream(final OutputStream out, final ProgressListener listener) {
                super(out);
                this.listener = listener;
                this.transferred = 0;
            }

            public void write(byte[] b, int off, int len) throws IOException {
                out.write(b, off, len);
                this.transferred += len;
                this.listener.transferred(this.transferred);
            }

            public void write(int b) throws IOException {
                out.write(b);
                this.transferred++;
                this.listener.transferred(this.transferred);
            }
        }
    }

    public interface ProgressListener {
        boolean doneFlag(boolean b);

        void transferred(long num);

    }

    public interface ImageUploaded {
        void uploadIsDone(boolean uploaded, String messege);
    }

    public void setOnUploadedListener(ImageUploaded listener) {
        this.imageUploaded = listener;
    }

    public String getUploadedImageUrl() {
        return uploadedImageUrl;
    }


}
