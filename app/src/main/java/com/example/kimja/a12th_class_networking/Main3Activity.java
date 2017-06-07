package com.example.kimja.a12th_class_networking;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static android.R.attr.data;

public class Main3Activity extends AppCompatActivity {
    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        data = new ArrayList<String>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,data) ;
        listView= (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter) ;
    }

    public void onClick(View v){
        if(v.getId() == R.id.next2){
            Intent ab  = new Intent(Main3Activity.this,Main4Activity.class);
            startActivity(ab);
        } else {
            thread.start();
        }

    }

    Handler handler = new Handler();
    Thread thread = new Thread() {
        @Override
        public void run() {
            URL url = null;
            try {
                url = new
                        URL("https://news.google.com/news?cf=all&hl=ko&pz=1&ned=kr&topic=m&output=rss");
                HttpURLConnection urlConnection =
                        (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() ==
                        HttpURLConnection.HTTP_OK) {
                    int itemCount = readData(urlConnection.getInputStream());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                    urlConnection.disconnect();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    };

    int readData(InputStream is) {
        DocumentBuilderFactory builderFactory =
                DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(is);
            int datacount = parseDocument(document);
            return datacount;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int parseDocument(Document doc) {
        Element docEle = doc.getDocumentElement();
        NodeList nodelist = docEle.getElementsByTagName("item");
        int count = 0;
        if ((nodelist != null) && (nodelist.getLength() > 0)) {
            for (int i = 0; i < nodelist.getLength(); i++) {
                String newsItem = getTagData(nodelist, i);
                if (newsItem != null) {
                    data.add(newsItem);
                    count++;
                }
            }
        }
        return count;
    }

    private String getTagData(NodeList nodelist, int index) {
        String newsItem = null;
        try {
            Element entry = (Element) nodelist.item(index);
            Element title = (Element) entry.getElementsByTagName("title").item(0);
            Element pubDate = (Element) entry.getElementsByTagName("pubDate").item(0);

            String titleValue = null;
            if (title != null) {
                Node firstChild = title.getFirstChild();
                if (firstChild != null) titleValue = firstChild.getNodeValue();
            }
            String pubDateValue = null;
            if (pubDate != null) {
                Node firstChild = pubDate.getFirstChild();
                if (firstChild != null) pubDateValue = firstChild.getNodeValue();
            }
            SimpleDateFormat simpleDateFormat= new SimpleDateFormat("YYYY-MM-dd");
            Date date = new Date();
            newsItem = titleValue + "-" + simpleDateFormat.format(date.parse(pubDateValue));

        } catch (DOMException e) {
            e.printStackTrace();
        }return newsItem;
    }
}
