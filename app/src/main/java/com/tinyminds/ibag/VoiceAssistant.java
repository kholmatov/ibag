package com.tinyminds.ibag;

import android.app.Activity;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;


import java.util.Locale;

public class VoiceAssistant {
    public TextToSpeech TTS;
    private String SpeakText;
    public VoiceAssistant(Context context, String text) {
        SpeakText = text;
        TTS = new TextToSpeech(context, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int initStatus) {
                if (initStatus == TextToSpeech.SUCCESS) {
                    if (TTS.isLanguageAvailable(new Locale(Locale.getDefault().getLanguage()))
                            == TextToSpeech.LANG_AVAILABLE) {
                        Log.i("TTS", Locale.getDefault().getLanguage());
                        TTS.setLanguage(new Locale(Locale.getDefault().getLanguage()));
                    } else {
                        TTS.setLanguage(Locale.US);
                    }
                    TTS.setPitch(1.3f);
                    TTS.setSpeechRate(0.7f);
                    speak(SpeakText);
                } else if (initStatus == TextToSpeech.ERROR) {
                    Log.e("TTS", "Модуль звука не доступно");
                }
            }
        });
    }

    public void speak(String text) {
        int speechStatus = TTS.speak(text,
                TextToSpeech.QUEUE_FLUSH, null);

        if (speechStatus == TextToSpeech.ERROR) {
            Log.e("TTS", "Error in converting Text to Speech! ");
        }
    }

    public void ShatDown(){
       // TTS.shutdown();
        TTS.stop();
    }


}
