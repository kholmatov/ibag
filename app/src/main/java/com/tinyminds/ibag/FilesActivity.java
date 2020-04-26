package com.tinyminds.ibag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class FilesActivity extends AppCompatActivity {
    private List<File> mListFiles;
    private FileAdapter mAdapter;
    SharedPreferences shared;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Электронная библиотека");
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        ListView files = findViewById(R.id.files);
        mListFiles = new ArrayList<File>();
        mAdapter = new FileAdapter(this, mListFiles);
        files.setAdapter(mAdapter);
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(getAssets().open("file.xml"));
            Element root = doc.getDocumentElement();
            root.normalize();
            shared = getSharedPreferences("tinyminds", Context.MODE_PRIVATE);
            Integer level = Integer.parseInt(shared.getString("level", "1"));
            String result = (1 <= level && 4 >= level) ? shared.getString("level", "1") : "1";
            NodeList nList = doc.getElementsByTagName("book_"+result);
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;
                    Integer id = Integer.parseInt(getValue("id", element2));
                    String name = getValue("name", element2);
                    String weight = getValue("weight", element2);
                    String file = getValue("file", element2);
                    mListFiles.add(new File(id, name, file, weight));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        files.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent mainIntent = new Intent(getApplicationContext(), PdfActivity.class);
                mainIntent.putExtra("title", mListFiles.get(position).getTitle());
                mainIntent.putExtra("file", mListFiles.get(position).getFile());
                startActivity(mainIntent);

            }
        });


        mAdapter.notifyDataSetChanged();

    }


    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}