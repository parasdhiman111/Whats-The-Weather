package com.example.paras.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText cityName;
    TextView weatherReort;
    Boolean flag=true;
    Toast toast;

    public void giveReport(View view)
    {
        Log.i("city name : ",cityName.getText().toString());

        InputMethodManager mgr=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        mgr.hideSoftInputFromWindow(cityName.getWindowToken(),0);

        try {
            String encodedCityName= URLEncoder.encode(cityName.getText().toString(),"UTF-8");
            DownloadTask task=new DownloadTask();
            task.execute("https://api.openweathermap.org/data/2.5/weather?q="+encodedCityName+"&APPID=406b0ce2242ddbd3e369e14006dd2649");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            //Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG).show();
toast.show();
        }






    }


    public class DownloadTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... urls) {
            String results="";
            URL url;

            HttpURLConnection urlConnection=null;

            try {
                url=new URL(urls[0]);
                urlConnection=(HttpURLConnection)url.openConnection();

                InputStream in =urlConnection.getInputStream();

                InputStreamReader reader=new InputStreamReader(in);

                int data=reader.read();

                while(data!=-1)
                {
                    char current=(char)data;

                    results+=current;

                    data=reader.read();

                }
                return results;

            } catch (Exception e) {
                //Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG).show();
                toast.show();
            }



            return null;



        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            JSONObject jsonObject=null;
            try {
                jsonObject = new JSONObject(result);


                String weather= jsonObject.getString("weather");




                JSONArray array=new JSONArray(weather);

                for(int i=0;i<array.length();i++)
                {
                    String message="";

                    JSONObject jsonPart=array.getJSONObject(i);

                    String id="";
                    String main="";
                    String description="";

                    id=jsonPart.getString("id");
                    main=jsonPart.getString("main");
                    description=jsonPart.getString("description");

                    if(main!="" && description!="" && id!="")
                    {

                        message+=main + " : " + description +"\n"+id+"\r\n";
                    }

                    if(message!="")
                    {
                        weatherReort.setText(message);
                    }
                    else
                    {
                       // Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG).show();
                    toast.show();
                    }

                    Log.i( "ID ",jsonPart.getString("id"));
                    Log.i( "Main ",jsonPart.getString("main"));
                    Log.i( "Description ",jsonPart.getString("description"));


                }

            } catch (JSONException e) {
             //   Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG).show();
            toast.show();
            }



        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName=(EditText)findViewById(R.id.etCheckWeather);
        weatherReort=(TextView)findViewById(R.id.tvReport);

        toast=Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG);

    }
}
