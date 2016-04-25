package com.example.andrew.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

import java.util.concurrent.TimeUnit;

public class MyService extends Service {
    NotificationManager nm;

    @Override
    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendNotif();
        return super.onStartCommand(intent, flags, startId);
    }

    void sendNotif() {
        // 1
        Notification notif = new Notification(R.drawable.ic_launcher, "Text in status bar",
                System.currentTimeMillis());

        // 3
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.FILE_NAME, "somefile");
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // 2
        notif.setLatestEventInfo(this, "Notification's title", "Notification's text", pIntent);

        // повідомлення пропало після нажаття
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.defaults |= Notification.DEFAULT_SOUND;

        // відправляєм
        nm.notify(1, notif);
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }
}
