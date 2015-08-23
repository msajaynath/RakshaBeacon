package com.hack.roadsafety.raksha;

import android.animation.ValueAnimator;
import android.app.Fragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import com.dd.CircularProgressButton;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class DriverActivityFragment extends Fragment {
CircularProgressButton startButton;
    public DriverActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_driver, container, false);
        startButton= (CircularProgressButton) v.findViewById(R.id.startButton);
        vehicleNo= (EditText) v.findViewById(R.id.vehicleNo);
        ambulanceID= (EditText) v.findViewById(R.id.ambulanceID);


        return v;
    }
    EditText vehicleNo,ambulanceID;
    AppLocationService appLocationService;

    double latitude=1.232,longitude=2.3434;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1234);
// Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
              Log.d("test",location.getLatitude()+" "+location.getLongitude());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };


// Register the listener with the Location Manager to receive location updates
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
        catch (SecurityException e){ Log.d("test",e.toString());}
            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MyAsyncTask().execute(vehicleNo.getText().toString(),ambulanceID.getText().toString());

                    if (startButton.getProgress() == 0) {
                        simulateSuccessProgress(startButton);
                    } else {
                        startButton.setProgress(0);
                    }
                    NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context
                            .NOTIFICATION_SERVICE);

                   ShowNotification();
                }
            });

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("Raksha", Context.MODE_PRIVATE);
        vehicleNo.setText(sharedpreferences.getString("vno","KL07N2322"));
        ambulanceID.setText(sharedpreferences.getString("vno","AL3687834"));

    }

    private void ShowNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(R.drawable.cpb_background)
                        .setContentTitle(getResources().getString(R.string.app_name))
                        .setOngoing(true)
                        .setContentText("Journey started Click to end.");

// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(getActivity(), DriverActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(DriverActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(1234, mBuilder.build());
        getActivity().finish();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private class MyAsyncTask extends AsyncTask<String, Integer, Double>{
        OkHttpClient client;
        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
             client = new OkHttpClient();
            try {
                String encodedUrl="http://www.rakshaid.com/hackathon_ambu.php?Data="+"'[vno:"+params[0]+",aid:"+params[1]+",lat:"+latitude+",lon:"+longitude+"]'";

               // String encodedUrl = java.net.URLEncoder.encode("http://www.rakshaid.com/hackathon_ambu.php?Data="+data,"UTF-8");

                String response= post(encodedUrl, "{}");
                Log.d("Test",response);
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
        protected void onProgressUpdate(Integer... progress){
          //  pb.setProgress(progress[0]);
        }



    }



    private void simulateSuccessProgress(final CircularProgressButton button) {
        ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 100);
        widthAnimation.setDuration(1500);
        widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                button.setProgress(value);
            }
        });
        widthAnimation.start();
    }





}
