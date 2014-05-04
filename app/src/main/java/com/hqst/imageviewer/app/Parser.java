package com.hqst.imageviewer.app;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by ahkj on 04/05/14.
 */
public class Parser {
    /*
    Returns the relative endpoints provided in the json-array formatted
    response from the root endpoint.
     */
    public static ArrayList<String> parseResponse(String responseString){
        ArrayList<String> strings = new ArrayList<String>();
        try {
            JSONArray jsonArray = new JSONArray(responseString);
            for (int i = 0; i < jsonArray.length(); i++){
                strings.add(jsonArray.getString(i));
            }
        }
        catch (JSONException je){
            je.printStackTrace();
        }
        return strings;
    }
}
