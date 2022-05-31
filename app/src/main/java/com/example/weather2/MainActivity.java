package com.example.weather2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private TextView text5;
    private Button btn;
    private EditText editText;
    private String str;
    private int day = 0;
    private ArrayList<String> days = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.button);
        text1 = findViewById(R.id.textView);
        text5 = findViewById(R.id.textView5);




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url1 = "";
                url1 = "https://pro.openweathermap.org/data/2.5/forecast/climate?lat=35&lon=139&appid=ee318df497020f077901717e7b51cced&lang=ru&units=metric";
                System.out.println(url1);
                new GetUrlData().execute(url1);




            }
        });
    }
    private class GetUrlData extends AsyncTask<String, String, String> {
        protected void onPreExecute(){
            super.onPreExecute();

        }
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null){
                    buffer.append(line).append("\n");
                }
                System.out.println(buffer);
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null)
                    connection.disconnect();
                try {
                    if (reader != null) {
                        reader.close();
                    }
                }
                catch (IOException e){
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            try {
                JSONObject obj = new JSONObject(result);

                String str1 = "";
                if (days.size() == 0) {
                    for (int i = 0; i < 30; i++) {
                        str1 = str1 + "                 День " + String.valueOf(i + 1) + "\n" + "\n" + "\n" + "\n" + "\n";
                        str1 = str1 + "Температура днём: " + (String.valueOf(obj.getJSONArray("list").getJSONObject(i).getJSONObject("temp").getDouble("day")) + "\n" + "\n" + "\n" + "\n");
                        str1 = str1 + "Температура ночью: " + (String.valueOf(obj.getJSONArray("list").getJSONObject(i).getJSONObject("temp").getDouble("night")));
                        days.add(str1);
                        str1 = "";
                    }
                }
                text1.setText(days.get(0));
                text5.setText("JP");
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public void onV(View view) {
        if (days.size() == 30) {
            if (day != 29) {
                day++;
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Дальше дней нет", Toast.LENGTH_SHORT);
                toast.show();
            }
            text1.setText(days.get(day));

        }
    }

    public void onN(View view) {
        if (days.size() == 30) {
            if (day != 0) {
                day--;
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Раньше дней нет", Toast.LENGTH_SHORT);
                toast.show();
            }
            text1.setText(days.get(day));

        }
    }

}