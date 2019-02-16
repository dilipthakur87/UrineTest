package com.example.dilip.urinetest;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Scalar;

import java.util.ArrayList;
import java.util.HashMap;

public class PatchClassifier{

    public void classifyData(ArrayList<Scalar> detectedPatchColor, Context context) {

        // model class for required data
        RequiredData requiredData = new RequiredData();

        // setting color and context
        requiredData.setColors(detectedPatchColor);
        requiredData.setContext(context);

        new CompareClass().execute(requiredData);
    }

}

 class CompareClass extends AsyncTask<RequiredData, Integer, String>{

    GetDataSet getDataSet = new GetDataSet();

    // JSONArray to get respective array
    JSONArray leu_value_array = new JSONArray();
    JSONArray nit_value_array = new JSONArray();
    JSONArray uro_value_array = new JSONArray();
    JSONArray pro_value_array = new JSONArray();
    JSONArray ph_value_array = new JSONArray();
    JSONArray blo_value_array = new JSONArray();
    JSONArray sg_value_array = new JSONArray();
    JSONArray ket_value_array = new JSONArray();
    JSONArray bil_value_array = new JSONArray();
    JSONArray glu_value_array = new JSONArray();
    JSONArray asc_value_array = new JSONArray();


    // declaring modal class
    CalculatedDataModelClass calculatedDataModelClass;

    // ArrayList of type modal class
    ArrayList<CalculatedDataModelClass> leu_result = new ArrayList<>();
    ArrayList<CalculatedDataModelClass> nit_result = new ArrayList<>();
    ArrayList<CalculatedDataModelClass> uro_result = new ArrayList<>();
    ArrayList<CalculatedDataModelClass> pro_result = new ArrayList<>();
    ArrayList<CalculatedDataModelClass> ph_result = new ArrayList<>();
    ArrayList<CalculatedDataModelClass> blo_result = new ArrayList<>();
    ArrayList<CalculatedDataModelClass> sg_result = new ArrayList<>();
    ArrayList<CalculatedDataModelClass> ket_result = new ArrayList<>();
    ArrayList<CalculatedDataModelClass> bil_result = new ArrayList<>();
    ArrayList<CalculatedDataModelClass> glu_result = new ArrayList<>();
    ArrayList<CalculatedDataModelClass> asc_result = new ArrayList<>();

    // hashmap type variable to store the final results after comparing
    HashMap<String, Double> compareResults = new HashMap<String, Double>();


     @Override
    protected void onPreExecute() {
        //Setup precondition to execute some task

    }

    @Override
    protected String doInBackground(RequiredData... requiredData) {

        try {
            JSONObject obj = new JSONObject(getDataSet.loadJSONFromAsset(requiredData[0].getContext()));
            leu_value_array = obj.getJSONArray("leu_value");
            nit_value_array = obj.getJSONArray("nit_value");
            uro_value_array = obj.getJSONArray("uro_value");
            pro_value_array = obj.getJSONArray("pro_value");
            ph_value_array = obj.getJSONArray("ph_value");
            blo_value_array = obj.getJSONArray("blo_value");
            sg_value_array = obj.getJSONArray("sg_value");
            ket_value_array = obj.getJSONArray("ket_value");
            bil_value_array = obj.getJSONArray("bil_value");
            glu_value_array = obj.getJSONArray("glu_value");
            asc_value_array = obj.getJSONArray("asc_value");

            // euclidian distance for leu
            for (int i=0; i<leu_value_array.length(); i++){
                JSONObject jsonObject = leu_value_array.getJSONObject(i);
                JSONArray jsonArray = jsonObject.getJSONArray("min");
                calculatedDataModelClass = new CalculatedDataModelClass(EuclidianDistance(requiredData[0].getColors().get(0), jsonArray), jsonObject.getString("value"), "leu_value");

                leu_result.add(calculatedDataModelClass);

            }

            // euclidian distance for nit
            for (int i=0; i<nit_value_array.length(); i++){
                JSONObject jsonObject = nit_value_array.getJSONObject(i);
                JSONArray jsonArray = jsonObject.getJSONArray("min");
                calculatedDataModelClass = new CalculatedDataModelClass(EuclidianDistance(requiredData[0].getColors().get(0), jsonArray), jsonObject.getString("value"), "nit_value");

                nit_result.add(calculatedDataModelClass);

            }

            // euclidian distance for uro
            for (int i=0; i<uro_value_array.length(); i++){
                JSONObject jsonObject = uro_value_array.getJSONObject(i);
                JSONArray jsonArray = jsonObject.getJSONArray("min");
                calculatedDataModelClass = new CalculatedDataModelClass(EuclidianDistance(requiredData[0].getColors().get(0), jsonArray), jsonObject.getString("value"), "uro_value");

                uro_result.add(calculatedDataModelClass);

            }

            // euclidian distance for pro
            for (int i=0; i<pro_value_array.length(); i++){
                JSONObject jsonObject = pro_value_array.getJSONObject(i);
                JSONArray jsonArray = jsonObject.getJSONArray("min");
                calculatedDataModelClass = new CalculatedDataModelClass(EuclidianDistance(requiredData[0].getColors().get(0), jsonArray), jsonObject.getString("value"), "pro_value");

                pro_result.add(calculatedDataModelClass);
            }

            // euclidian distance for ph
            for (int i=0; i<ph_value_array.length(); i++){
                JSONObject jsonObject = ph_value_array.getJSONObject(i);
                JSONArray jsonArray = jsonObject.getJSONArray("min");
                calculatedDataModelClass = new CalculatedDataModelClass(EuclidianDistance(requiredData[0].getColors().get(0), jsonArray), jsonObject.getString("value"), "ph_value");

                ph_result.add(calculatedDataModelClass);
            }

            // euclidian distance for blo
            for (int i=0; i<blo_value_array.length(); i++){
                JSONObject jsonObject = blo_value_array.getJSONObject(i);
                JSONArray jsonArray = jsonObject.getJSONArray("min");
                calculatedDataModelClass = new CalculatedDataModelClass(EuclidianDistance(requiredData[0].getColors().get(0), jsonArray), jsonObject.getString("value"), "blo_value");

                blo_result.add(calculatedDataModelClass);
            }

            // euclidian distance for sg
            for (int i=0; i<sg_value_array.length(); i++){
                JSONObject jsonObject = sg_value_array.getJSONObject(i);
                JSONArray jsonArray = jsonObject.getJSONArray("min");
                calculatedDataModelClass = new CalculatedDataModelClass(EuclidianDistance(requiredData[0].getColors().get(0), jsonArray), jsonObject.getString("value"), "sg_value");

                sg_result.add(calculatedDataModelClass);
            }

            // euclidian distance for ket
            for (int i=0; i<ket_value_array.length(); i++){
                JSONObject jsonObject = ket_value_array.getJSONObject(i);
                JSONArray jsonArray = jsonObject.getJSONArray("min");
                calculatedDataModelClass = new CalculatedDataModelClass(EuclidianDistance(requiredData[0].getColors().get(0), jsonArray), jsonObject.getString("value"), "ket_value");

                ket_result.add(calculatedDataModelClass);
            }

            // euclidian distance for bil
            for (int i=0; i<bil_value_array.length(); i++){
                JSONObject jsonObject = bil_value_array.getJSONObject(i);
                JSONArray jsonArray = jsonObject.getJSONArray("min");
                calculatedDataModelClass = new CalculatedDataModelClass(EuclidianDistance(requiredData[0].getColors().get(0), jsonArray), jsonObject.getString("value"), "bil_value");

                bil_result.add(calculatedDataModelClass);
            }

            // euclidian distance for glu
            for (int i=0; i<glu_value_array.length(); i++){
                JSONObject jsonObject = glu_value_array.getJSONObject(i);
                JSONArray jsonArray = jsonObject.getJSONArray("min");
                calculatedDataModelClass = new CalculatedDataModelClass(EuclidianDistance(requiredData[0].getColors().get(0), jsonArray), jsonObject.getString("value"), "glu_value");

                glu_result.add(calculatedDataModelClass);
            }

            // euclidian distance for asc
            for (int i=0; i<asc_value_array.length(); i++){
                JSONObject jsonObject = asc_value_array.getJSONObject(i);
                JSONArray jsonArray = jsonObject.getJSONArray("min");
//                calculatedDataModelClass.setEuclidian_disance(EuclidianDistance(requiredData[0].getColors().get(0), jsonArray));
//                calculatedDataModelClass.setTag_name("asc_value");
//                calculatedDataModelClass.setTag_value(jsonObject.getString("value"));
                calculatedDataModelClass = new CalculatedDataModelClass(EuclidianDistance(requiredData[0].getColors().get(0), jsonArray), jsonObject.getString("value"), "asc_value");

                asc_result.add(calculatedDataModelClass);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
         super.onPostExecute(s);
        //Setup precondition to execute some task

        // using bubble sort algorithm to fing the minimum distance
        // initializing smalles with the first indexed value of each array
        double smallest_leu = leu_result.get(0).getEuclidian_disance();
        double smallest_nit = nit_result.get(0).getEuclidian_disance();
        double smallest_uro = uro_result.get(0).getEuclidian_disance();
        double smallest_pro = pro_result.get(0).getEuclidian_disance();
        double smallest_ph = ph_result.get(0).getEuclidian_disance();
        double smallest_blo = blo_result.get(0).getEuclidian_disance();
        double smallest_sg = sg_result.get(0).getEuclidian_disance();
        double smallest_ket = ket_result.get(0).getEuclidian_disance();
        double smallest_bil = bil_result.get(0).getEuclidian_disance();
        double smallest_glu = glu_result.get(0).getEuclidian_disance();
        double smallest_asc = asc_result.get(0).getEuclidian_disance();

        // calculating the minimum euclidian distance for leu
        for (int i=0; i<leu_result.size()-1; i++){
            System.out.println("result leu = "+ leu_result.get(i).getEuclidian_disance());
            if (smallest_leu > leu_result.get(i+1).getEuclidian_disance()){
                smallest_leu = leu_result.get(i+1).getEuclidian_disance();
            }
        }

        // calculating the minimum euclidian distance for nit
        for (int i=0; i<nit_result.size()-1; i++){
            System.out.println("result nit = "+ nit_result.get(i).getEuclidian_disance());
            if (smallest_nit > nit_result.get(i+1).getEuclidian_disance()){
                smallest_nit = nit_result.get(i+1).getEuclidian_disance();
            }
        }

        // calculating the minimum euclidian distance for uro
        for (int i=0; i<uro_result.size()-1; i++){
            System.out.println("result uro = "+ uro_result.get(i).getEuclidian_disance());
            if (smallest_uro > uro_result.get(i+1).getEuclidian_disance()){
                smallest_uro = uro_result.get(i+1).getEuclidian_disance();
            }
        }

        // calculating the minimum euclidian distance for pro
        for (int i=0; i<pro_result.size()-1; i++){
            System.out.println("result pro = "+ pro_result.get(i).getEuclidian_disance());
            if (smallest_pro > pro_result.get(i+1).getEuclidian_disance()){
                smallest_pro = pro_result.get(i+1).getEuclidian_disance();
            }
        }

        // calculating the minimum euclidian distance for ph
        for (int i=0; i<ph_result.size()-1; i++){
            System.out.println("result ph = "+ ph_result.get(i).getEuclidian_disance());
            if (smallest_ph > ph_result.get(i+1).getEuclidian_disance()){
                smallest_ph = ph_result.get(i+1).getEuclidian_disance();
            }
        }

        // calculating the minimum euclidian distance for blo
        for (int i=0; i<blo_result.size()-1; i++){
            System.out.println("result blo = "+ blo_result.get(i).getEuclidian_disance());
            if (smallest_blo > blo_result.get(i+1).getEuclidian_disance()){
                smallest_blo = blo_result.get(i+1).getEuclidian_disance();
            }
        }

        // calculating the minimum euclidian distance for sg
        for (int i=0; i<sg_result.size()-1; i++){
            System.out.println("result sg = "+ sg_result.get(i).getEuclidian_disance());
            if (smallest_sg > sg_result.get(i+1).getEuclidian_disance()){
                smallest_sg = sg_result.get(i+1).getEuclidian_disance();
            }
        }

        // calculating the minimum euclidian distance for ket
        for (int i=0; i<ket_result.size()-1; i++){
            System.out.println("result ket = "+ ket_result.get(i).getEuclidian_disance());
            if (smallest_ket > ket_result.get(i+1).getEuclidian_disance()){
                smallest_ket = ket_result.get(i+1).getEuclidian_disance();
            }
        }

        // calculating the minimum euclidian distance for bil
        for (int i=0; i<bil_result.size()-1; i++){
            System.out.println("result bil = "+ bil_result.get(i).getEuclidian_disance());
            if (smallest_bil > bil_result.get(i+1).getEuclidian_disance()){
                smallest_bil = bil_result.get(i+1).getEuclidian_disance();
            }
        }

        // calculating the minimum euclidian distance for glu
        for (int i=0; i<glu_result.size()-1; i++){
            System.out.println("result glu = "+ glu_result.get(i).getEuclidian_disance());
            if (smallest_glu > glu_result.get(i+1).getEuclidian_disance()){
                smallest_glu = glu_result.get(i+1).getEuclidian_disance();
            }
        }

        // calculating the minimum euclidian distance for asc
        for (int i=0; i<asc_result.size()-1; i++){
            System.out.println("result asc = "+ asc_result.get(i).getEuclidian_disance());
            if (smallest_asc > asc_result.get(i+1).getEuclidian_disance()){
                smallest_asc = asc_result.get(i+1).getEuclidian_disance();
            }
        }

        System.out.println("Smallest leu = "+smallest_leu);
        System.out.println("Smallest nit = "+smallest_nit);
        System.out.println("Smallest uro = "+smallest_uro);
        System.out.println("Smallest pro = "+smallest_pro);
        System.out.println("Smallest ph = "+smallest_ph);
        System.out.println("Smallest blo = "+smallest_blo);
        System.out.println("Smallest sg = "+smallest_sg);
        System.out.println("Smallest ket = "+smallest_ket);
        System.out.println("Smallest bil = "+smallest_bil);
        System.out.println("Smallest glu = "+smallest_glu);
        System.out.println("Smallest asc = "+smallest_asc);

        // setting the final results in the HashMap compareResults
        compareResults.put("Leu Value", new Double(smallest_leu));
        compareResults.put("Nit Value", new Double(smallest_nit));
        compareResults.put("Uro Value", new Double(smallest_uro));
        compareResults.put("Pro Value", new Double(smallest_pro));
        compareResults.put("Ph Value", new Double(smallest_ph));
        compareResults.put("Blo Value", new Double(smallest_blo));
        compareResults.put("Sg Value", new Double(smallest_sg));
        compareResults.put("Ket Value", new Double(smallest_ket));
        compareResults.put("Bil Value", new Double(smallest_bil));
        compareResults.put("Glu Value", new Double(smallest_glu));
        compareResults.put("Asc Value", new Double(smallest_asc));

        // printing the final result
        System.out.println("Final compared result = "+compareResults);

    }

    public double EuclidianDistance(Scalar current_data, JSONArray match_data){
         double redDifference=0.0, greenDifference=0.0, blueDifference=0.0;

        try {
                redDifference = current_data.val[2] - (int) match_data.get(2);
                greenDifference = current_data.val[1] - (int) match_data.get(1);
                blueDifference = current_data.val[0] - (int) match_data.get(0);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Math.sqrt(redDifference * redDifference + greenDifference * greenDifference + blueDifference * blueDifference);
    }
}


class RequiredData{

    ArrayList<Scalar> colors = new ArrayList<>();
    Context context;

    public ArrayList<Scalar> getColors() {
        return colors;
    }

    public void setColors(ArrayList<Scalar> colors) {
        this.colors = colors;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}
