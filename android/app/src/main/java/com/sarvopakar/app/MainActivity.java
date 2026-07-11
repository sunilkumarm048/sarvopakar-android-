package com.sarvopakar.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {

    /**
     * High-importance notification channel with the custom order ring baked
     * in. Anything posted to this channel — including FCM pushes once
     * Firebase is wired in (phase 2) — plays shop_new_order.mp3 loudly,
     * heads-up, even when the app is closed. Channels are created once and
     * persist; safe to call on every launch.
     */
    private void createOrderAlertChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return;

        Uri sound = Uri.parse(
            "android.resource://" + getPackageName() + "/" + R.raw.shop_new_order
        );
        AudioAttributes attrs = new AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build();

        NotificationChannel channel = new NotificationChannel(
            "order_alerts",
            "New order alerts",
            NotificationManager.IMPORTANCE_HIGH
        );
        channel.setDescription("Rings loudly when a new order or service request arrives");
        channel.setSound(sound, attrs);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[] { 0, 400, 200, 400, 200, 400 });
        channel.setBypassDnd(false);
        channel.setShowBadge(true);

        NotificationManager nm = getSystemService(NotificationManager.class);
        if (nm != null) nm.createNotificationChannel(channel);
    }

    /** Android 13+ requires runtime opt-in before any notification shows. */
    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT < 33) return;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                new String[] { android.Manifest.permission.POST_NOTIFICATIONS },
                1001
            );
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createOrderAlertChannel();
        requestNotificationPermission();
    }
}
