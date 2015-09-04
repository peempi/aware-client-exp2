
package com.aware.utils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;

public class Aware_TTS extends Service implements OnInitListener {

    public static final String ACTION_AWARE_TTS_SPEAK = "ACTION_AWARE_TTS_SPEAK";

    private static final String TAG = "AWARE::TTS";
    public static final String EXTRA_TTS_TEXT = "tts_text";
    
    private TextToSpeech tts;
    private boolean ready = false;
    private String text;

    /**
     * Speak the given text
     * @param text
     */
    public void speak(String text) {
        if( ready ) {
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                tts.speak(text, TextToSpeech.QUEUE_ADD, null, null);
            } else {
                tts.speak(text, TextToSpeech.QUEUE_ADD, null);
            }
        } else {
            Log.d(TAG, TAG +" not ready yet!");
        }
    }

    @Override
    public void onInit(int status) {
        if( status == TextToSpeech.SUCCESS ) {
            ready = true;
            if( text.length() > 0 ) {
                speak(text);
            }
        } else {
            ready = false;
            stopSelf();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        tts = new TextToSpeech(this, this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if( intent != null ) {
            text = intent.getStringExtra(EXTRA_TTS_TEXT);
            if( text.length() > 0 && tts != null ) {
                speak(intent.getStringExtra(EXTRA_TTS_TEXT));
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if( tts != null ) tts.shutdown();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static class Aware_TTS_Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if( intent.getAction().equals(ACTION_AWARE_TTS_SPEAK) ) {
                Intent tts_work = new Intent(context, Aware_TTS.class);
                tts_work.putExtra(EXTRA_TTS_TEXT, intent.getStringExtra(EXTRA_TTS_TEXT));
                context.startService(tts_work);
            }
        }
    }
}
