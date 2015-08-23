package com.hack.roadsafety.raksha;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProfileActivityFragment extends Fragment {

    public ProfileActivityFragment() {
    }
RadioGroup profile;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_profile, container, false);
        profile= (RadioGroup) v.findViewById(R.id.profile);
        profile.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.amb:
                        Intent in=new Intent(getActivity(),DriverActivity.class);
                        startActivity(in);
                        getActivity().finish();
                        break;
                    case R.id.user:


                        Intent ishintent = new Intent(getActivity(), ServiceClass.class);
                        PendingIntent pintent = PendingIntent.getService(getActivity(), 0, ishintent, 0);
                        AlarmManager alarm = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
                        alarm.cancel(pintent);
                        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),10000, pintent);
                        ShowNotification();
                        getActivity().finish();

//                        in=new Intent(getActivity(),DriverActivity.class);
//                        startActivity(in);in
                        break;


                }
            }
        });
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(4321);
        return v;
    }

    private void ShowNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(R.drawable.cpb_background)
                        .setContentTitle(getResources().getString(R.string.app_name))
                        .setOngoing(true)
                        .setContentText("Listener service running...");

// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(getActivity(), ProfileActivity.class);

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
        mNotificationManager.notify(4321, mBuilder.build());
        getActivity().finish();
    }
}
