package com.hqst.imageviewer.app;

/**
 * ImageDownloader downloads images and
 * saves them locally. The class
 * can be constructed with a target ImageView
 * to be set when a download is done.
 *
 * Created by ahkj on 04/05/14.
 */

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageDownloader extends AsyncTask<String, Void, String> {
    private ImageView targetImageView;
    private Bitmap image;
    private Context applicationContext;

    public ImageDownloader(ImageView targetImageView, Context context) {
        this.applicationContext = context.getApplicationContext();
        this.targetImageView = targetImageView;
        System.out.println("target imageview passed in " + targetImageView);
    }

    /*
    Downloads a file from a given path and save it locally.
    Image files will be set if a target ImageView has
    been provided in the constructor.
     */
    @Override
    protected String doInBackground(String... params) {
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            System.out.println("download image: " + params[0]);
            URL url = new URL(params[0]);
            URLConnection connection = url.openConnection();
            inputStream = connection.getInputStream();

            // Download the image
            byte[] buffer = new byte[4096];
            int chunk = - 1;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            while((chunk = inputStream.read(buffer)) != -1){
                out.write(buffer, 0, chunk);
            }
            if(out != null) {
                out.close();
            }
            byte[] imageBytes = out.toByteArray();

            // Write the image bytes to a file with the same name as on the server
            String fileName = Uri.parse(url.toString()).getLastPathSegment();
            FileOutputStream outputStream;
            try {
                outputStream = this.applicationContext.openFileOutput(fileName, Context.MODE_PRIVATE);
                outputStream.write(imageBytes);
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Save the image as a bitmap for display
            this.image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            return fileName;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                System.out.println("Exception: " + e);
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String fileName) {
        if(this.image != null && this.targetImageView != null) {
            this.targetImageView.setImageBitmap(this.image);
        }
    }

    /*
    Returns array of all known relative image urls on the server.
     */
    public static ArrayList<String> DownloadImageUrls(String endpoint) {
        ArrayList<String> result = new ArrayList<String>();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(endpoint);
            URLConnection connection = url.openConnection();
            inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String responseString = reader.readLine();
            result = Parser.parseResponse(responseString);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                System.out.println("Exception: " + e);
                e.printStackTrace();
            }
        }
        return result;
    }
}
