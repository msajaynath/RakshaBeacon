package com.hack.roadsafety.raksha;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

public class ServiceClass extends Service implements
        TextToSpeech.OnInitListener{

    private OkHttpClient client;
    private TextToSpeech tts;
    private SharedPreferences prefs;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();



}

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        tts=null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        // Toast.makeText(this, "ServiceClass.onStart()", Toast.LENGTH_LONG).show();
        Log.d("Testing", "Service got started");
        try {
            Log.d("Testing", "Service got created");
            Toast.makeText(this, "ServiceClass.onCreate()", Toast.LENGTH_LONG).show();
            client = new OkHttpClient();

            String encodedUrl = "http://www.rakshaid.com/hackathon_user.php";
            tts = new TextToSpeech(this, this);

            new MyAsyncTask().execute(encodedUrl);

        }

        catch (Exception e)
        {

            Log.d("Test",e.toString());

        }
    }


    // String encodedUrl = java.net.URLEncoder.encode("http://www.rakshaid.com/hackathon_ambu.php?Data="+data,"UTF-8");

    public MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    String post(String url, String json) throws IOException {
        Log.d("Test",url+"");


        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Log.d("Test",response.body().string()+"");

        return response.body().string();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
               // btnSpeak.setEnabled(true);
                speakOut(mainResponse);
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }
String mainResponse;
    private class MyAsyncTask extends AsyncTask<String, Integer, Double> {
        OkHttpClient client;
        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            client = new OkHttpClient();
            try {
               // String encodedUrl="http://www.rakshaid.com/hackathon_ambu.php?Data="+"'[vno:"+params[0]+",aid:"+params[1]+",lat:"+latitude+",lon:"+longitude+"]'";

                // String encodedUrl = java.net.URLEncoder.encode("http://www.rakshaid.com/hackathon_ambu.php?Data="+data,"UTF-8");

                String response= post(params[0], "{}");
                Log.d("Test",response);
                mainResponse=response;
                Context ctx = getApplicationContext();
                 prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
                if(prefs.contains(response))
                { Log.d("Test", "Already alarmed");
                    return null;

                    // System.exit(0);
                }
//else
//                {
//
//
//                }

                speakOut(response);
            }
            catch (Exception e)
            {

                Log.d("Test",e.toString());

            }

            return null;
        }
        public   MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        String post(String url, String json) throws IOException {
            Log.d("Test",url+"");


            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            Log.d("Test",response.body().string()+"");

            return response.body().string();
        }
        protected void onPostExecute(Double result){
            Log.d("Test",result+"");

            // Toast.makeText(getApplicationContext(), "command sent", Toast.LENGTH_LONG).show();
        }
        protected void onProgressUpdate(Integer... progress)
        {
            //  pb.setProgress(progress[0]);
        }



    }
    private void speakOut(String response) {

        String text = getResources().getText(R.string.speechtext).toString();

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        prefs.edit().putBoolean(response,true).commit();
    }

}