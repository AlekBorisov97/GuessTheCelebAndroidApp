package com.example.alek.guessthecelebapp;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.jar.Pack200;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {

    int correctPosition = 0;
    int chosenCeleb = 0;
    ArrayList<String> celebURLS = new ArrayList<String>();
    ArrayList<String> celebNAMES = new ArrayList<String>();
    String[] answers = new String[4];
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    ImageView img;


    public class InfoDownloader extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {

            String result = "";
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connectionHttp = (HttpURLConnection) url.openConnection();
                InputStream in = connectionHttp.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data!=-1){
                    result+=(char) data;
                    data = reader.read();
                }

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... params) {

            Bitmap bitmap = null;
            try {

                URL url = new URL(params[0]);

                HttpURLConnection connectionHttp = (HttpURLConnection) url.openConnection();

                InputStream in = connectionHttp.getInputStream();

                bitmap = BitmapFactory.decodeStream(in);

                return bitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public void generateQuestion() throws ExecutionException, InterruptedException {

        Random rnd = new Random();
        chosenCeleb = rnd.nextInt(celebNAMES.size());
        correctPosition = rnd.nextInt(4);
        ImageDownloader imgDownload = new ImageDownloader();

        for(int i = 0 ; i < 4 ; i++){
            if(i==correctPosition){
                answers[i] = celebNAMES.get(chosenCeleb);
            }else{
                int incorrect = rnd.nextInt(celebNAMES.size());
                while(incorrect==chosenCeleb){
                    incorrect = rnd.nextInt(celebNAMES.size());
                }
                answers[i] = celebNAMES.get(incorrect);
            }
        }

        img.setImageBitmap(imgDownload.execute(celebURLS.get(chosenCeleb)).get());
        btn1.setText(answers[0]);
        btn2.setText(answers[1]);
        btn3.setText(answers[2]);
        btn4.setText(answers[3]);
    }

    public void checkAnswer(View view) throws ExecutionException, InterruptedException {
        if(view.getTag().toString().equals(Integer.toString(correctPosition))){
            Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(), "Wrong! It was "+celebNAMES.get(chosenCeleb), Toast.LENGTH_LONG).show();
        }
        generateQuestion();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = (ImageView) findViewById(R.id.imageView);
        btn1 = (Button) findViewById(R.id.button);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);
        btn4 = (Button) findViewById(R.id.button4);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    checkAnswer(v);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    checkAnswer(v);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    checkAnswer(v);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    checkAnswer(v);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        InfoDownloader downloader = new InfoDownloader();
        try {

            String result = downloader.execute("http://www.posh24.se/kandisar").get();
            String[] splitResult = result.split("<div class=\"sidebarContainer\">");

            Pattern p = Pattern.compile("<img src=\"(.*?)\"");
            Matcher m = p.matcher(splitResult[0]);

            while(m.find()){
                celebURLS.add(m.group(1));
            }

            p = Pattern.compile("alt=\"(.*?)\"");
            m = p.matcher(splitResult[0]);

            while(m.find()){
                celebNAMES.add(m.group(1));
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            generateQuestion();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
