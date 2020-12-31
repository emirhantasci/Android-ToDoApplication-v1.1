package com.example.todo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

public class AlarmReceiver2 extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent){
        Toast.makeText(context, "Daha önceden ayarlanan bir iş planınızın alarmı çalıyor. Kapatmak için uygulamaya gidin.", Toast.LENGTH_LONG).show();

        Uri alarmMelodisi = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmMelodisi==null){
            alarmMelodisi=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmMelodisi);
        ringtone.play();
    }
}
