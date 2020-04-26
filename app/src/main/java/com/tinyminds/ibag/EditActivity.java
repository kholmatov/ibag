package com.tinyminds.ibag;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class EditActivity extends AppCompatActivity {

    private VoiceAssistant VA;
    private SharedPreferences shared;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Настройка профиля");
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        shared = getSharedPreferences("tinyminds", Context.MODE_PRIVATE);

        VA = new VoiceAssistant(this, "Измени свой профиль");
        ImageView logo = findViewById(R.id.image);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VA.speak("Привет меня зовут ай бааг!");
            }
        });

        Button meet = findViewById(R.id.meet);
        final EditText firstname = findViewById(R.id.firstname);
        firstname.setText(shared.getString("firstname", ""));

        final EditText surname = findViewById(R.id.surname);
        surname.setText(shared.getString("surname", ""));

        final EditText level = findViewById(R.id.level);
        level.setText(shared.getString("level", "0"));

        final EditText weights = findViewById(R.id.weights);
        weights.setText(shared.getString("weights", "10"));

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        VA.ShatDown();
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


}
