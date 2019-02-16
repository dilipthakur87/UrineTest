package com.example.dilip.urinetest;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Scalar;

import java.util.ArrayList;

public class PatchClassifier {

    GetDataSet getDataSet = new GetDataSet();
    JSONArray dataSetArray = new JSONArray();

    public JSONArray getAllData(Context context){
        try {
            JSONObject obj = new JSONObject(getDataSet.loadJSONFromAsset(context));
            dataSetArray = obj.getJSONArray("values");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dataSetArray;
    }

    public void classifyData(ArrayList<Scalar> detectedPatchColor, Context context){
        System.out.println("Detected patches = "+detectedPatchColor);
        System.out.println("Present dataset = "+getAllData(context));
    }

}
