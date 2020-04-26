package com.tinyminds.ibag;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static com.tinyminds.ibag.R.*;
import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity {
    SharedPreferences shared;
    ProgressBar progressBar;
    ImageView image;
    VoiceAssistant VA;
    Map<Integer, Map> arrayNode;
    float count = 0;
    float ibagVal;
    TextView valText;
    Integer weights;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("iBag");
        setSupportActionBar(toolbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        image = findViewById(R.id.image);

        arrayNode = new HashMap<Integer, Map>();


        shared = getSharedPreferences("tinyminds", Context.MODE_PRIVATE);
        weights = Integer.parseInt(shared.getString("weights", "1"));
        ibagVal = weights * 1000 / 100 * 10;

        String firstname = shared.getString("firstname", "");
        TextView title = findViewById(R.id.textView2);
        firstname = "Привет " + firstname + "! ";
        title.setText(firstname);

        Integer level = Integer.parseInt(shared.getString("level", "1"));

        String today = new SimpleDateFormat("d MMMM, EEEE",
                new Locale("ru", "RU")).format(new Date());
        today = "Сегодня: " + today;
        TextView textDate = findViewById(id.textView3);
        textDate.setText(today);
        VA = new VoiceAssistant(this, firstname +
                today +
                ". Давай вместе соберём меня");

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                VA.ShatDown();
                Intent mainIntent;
                if (item.getItemId() == R.id.books) {
                    mainIntent = new Intent(getApplicationContext(), FilesActivity.class);
                    startActivity(mainIntent);
                } else if (item.getItemId() == R.id.user) {
                    mainIntent = new Intent(getApplicationContext(), EditActivity.class);
                    startActivity(mainIntent);
                } else if (item.getItemId() == R.id.close) {
                    finish();
                }


                return false;
            }
        });

        // Прогресс бар
        valText = findViewById(id.value123);
        valText.setText(valueOf(0));

        progressBar = findViewById(id.vertical_progressbar);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        progressBar.setProgress(0);
        progressBar.setProgressDrawable(getResources().getDrawable(drawable.verticalprogressbar));

        if (1 <= level && level <= 4) {
            showObject(String.valueOf(level));
        } else {
            showObject("1");
        }

    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void showObject(String level) {
        try {
            InputStream is = getAssets().open("file.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            Element root = doc.getDocumentElement();
            root.normalize();

            NodeList nList = doc.getElementsByTagName("book_" + level);
            LinearLayout ll1 = findViewById(id.ll1);

            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;
                    Integer id = Integer.parseInt(getValue("id", element2));
                    String name = getValue("name", element2);
                    String weight = getValue("weight", element2);
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("name", name);
                    map.put("weight", weight);
                    arrayNode.put(id, map);
                    CheckBox ch = new CheckBox(this);
                    ch.setId(id);
                    ch.setText(name);
                    ch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onCheckboxClicked(v);
                        }
                    });
                    ch.setTextSize(18);
                    ll1.addView(ch);
                }
            }
            Log.i("MainActivity", arrayNode.toString());

        } catch (Exception e) {
            Log.e("MainActivity", valueOf(e));
            e.printStackTrace();
        }

    }

    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        Map<String, String> result = arrayNode.get(view.getId());
        result.get("name");
        result.get("wieght");
        calc(Integer.parseInt(result.get("weight")), checked);
    }

    private void calc(Integer value, Boolean checked) {

        if (checked)
            count += value;
        else
            count -= value;
        Log.i("Main-val", String.valueOf(value) + " " + String.valueOf(count) + " " + String.valueOf(ibagVal));
        progressBar.post(new Runnable() {
            public void run() {
                Log.i("Main-run-val", String.valueOf(count / (ibagVal / 100)) + " " + String.valueOf(ibagVal));
                int val = (int) (count / (ibagVal / 100));
                valText.setText(String.valueOf(val));
                progressBar.setProgress(val);
                if (val > 100) {
                    image.setImageResource(drawable.logo_with_star_sad);
                    progressBar.setProgressDrawable(getResources().getDrawable(drawable.verticalprogressbar_red));
                    VA.speak("Прошу береги свое здоровья");
                } else {
                    image.setImageResource(drawable.logo_with_star);
                    progressBar.setProgressDrawable(getResources().getDrawable(drawable.verticalprogressbar));
                }


            }
        });

    }
}



