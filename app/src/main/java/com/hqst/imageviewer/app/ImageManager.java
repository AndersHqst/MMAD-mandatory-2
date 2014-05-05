package com.hqst.imageviewer.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by ahkj on 04/05/14.
 */
public class ImageManager {

    private static final String TAG = ImageManager.class.toString();

    /*
        Get relative image urls. Image urls are updated from the server
         and provided to the callback.
         */
    public static void getImageUrls(final Context context, final ICallback callback){
        final Handler handler = new Handler();
        new Thread(){
            public void run(){
                final ArrayList<String> newUrls = ImageDownloader.DownloadImageUrls(context.getString(R.string.server_endpoint));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.imageUrlsUpdated(newUrls);
                    }
                });
            }
        }.start();
    }

    /*
    Set the relevant image on the provided ImageView. If no image is
    cached locally, the image will be set when it has been
    fetched in the background.
     */
    public static void getImage(String imageUrl, ImageView imageView, Context context){
        String fileName = Uri.parse(imageUrl.toString()).getLastPathSegment();
        File file = context.getFileStreamPath(fileName);
        if(file.exists()) {
            Log.d(TAG, "Using cached image");
            Bitmap bm = BitmapFactory.decodeFile(file.getPath());
            imageView.setImageBitmap(bm);
        }else{
            Log.d(TAG, "Fetching image");
            ImageDownloader id = new ImageDownloader(imageView, context);
            id.execute(context.getString(R.string.server_endpoint) + imageUrl);
        }
    }

    public static Bitmap getThumbnail(String imageUrl, Context context){
        String fileName = Uri.parse(imageUrl.toString()).getLastPathSegment();
        File file = context.getFileStreamPath(fileName);
        Bitmap thumbNail = null;
        if(file.exists()) {
            thumbNail = BitmapFactory.decodeFile(file.getPath());
            // Scaled Bitmap, no filter
            Bitmap.createScaledBitmap(thumbNail, 85, 85, false);
        }
        else{
            Log.e(TAG, "Attempt to return thumbnail for image that has not been downloaded.");
        }
        return thumbNail;
    }


    public interface ICallback{
        public abstract void imageUrlsUpdated(ArrayList<String> imageUrls);
    }

}
