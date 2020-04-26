package com.tinyminds.ibag;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class HelloActivity extends AppCompatActivity {

    private VoiceAssistant VA;
    private SharedPreferences shared;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_hello);
        shared = getSharedPreferences("tinyminds", Context.MODE_PRIVATE);
        VA = new VoiceAssistant(this, "Привет меня зовут ай бааг! " +
                "Давай познокомимься, как тебя звать?");

        ImageView logo = findViewById(R.id.image);

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VA.speak("Привет меня зовут ай бааг!");
            }
        });


        Button meet = findViewById(R.id.meet);
        final EditText firstname = findViewById(R.id.firstname);
        final EditText surname = findViewById(R.id.surname);
        final EditText level = findViewById(R.id.level);
        final EditText weights = findViewById(R.id.weights);
        meet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (
                        firstname.getText().toString().matches("")
                                || surname.getText().toString().matches("")
                                || level.getText().toString().matches("")
                                || weights.getText().toString().matches("")
                ) {
                    VA.speak("заполни следующие поля");
                    return;
                }

                SharedPreferences.Editor editor = shared.edit();
                editor.putString("firstname", firstname.getText().toString());
                editor.putString("surname", surname.getText().toString());
                editor.putString("level", level.getText().toString());
                editor.putString("weights", weights.getText().toString());
                editor.commit();

                Intent mainIntent;
                mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainIntent);
                finish();
                return;
            }
        });

    }


}
