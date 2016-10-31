package com.example.kwan.musicgraph;

import android.os.AsyncTask;
import android.view.View;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Kwan on 2016/7/4.
 */
public class HttpRequest {

    public static class HttpAsyncTask extends AsyncTask<String, String, String> {
        View v;
        long elapsedTime;
        HttpURLConnection urlConnection;
        public interface AsyncResponse {
            void processFinish(String output, View v,long elapsedTime);
        }
        public AsyncResponse delegate = null;
        public HttpAsyncTask(View v,AsyncResponse delegate){
            this.delegate = delegate;
            this.v =v;
        }
        @Override
        protected String doInBackground(String... args) {
            long startTime = System.currentTimeMillis();
            StringBuilder result = new StringBuilder();
            String Url = args[0];
            try {
                URL url = new URL(Url);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            }catch( Exception e) {
                e.printStackTrace();
            }
            finally {
                urlConnection.disconnect();
            }

            elapsedTime = System.currentTimeMillis() - startTime;
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            delegate.processFinish(result,v,elapsedTime);

        }

    }


    public static class HttpAsyncTaskPost extends AsyncTask<String, String, String> {
        View v;
        HttpURLConnection urlConnection;
        long elapsedTime;
        JSONObject requestJson= new JSONObject();

        public interface AsyncResponse {
            void processFinish(String output, View v,long elapsedTime);
        }
        public AsyncResponse delegate = null;
        public HttpAsyncTaskPost(JSONObject data,View v,AsyncResponse delegate){
            this.delegate = delegate;
            this.v =v;
            this.requestJson=data;
        }
        @Override
        protected String doInBackground(String... args) {
            long startTime = System.currentTimeMillis();
            StringBuilder result = new StringBuilder();
            String Url = args[0];
            try {
                URL url = new URL(Url);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection.setRequestMethod("POST");
                urlConnection.connect();

                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                String output = requestJson.toString();
                writer.write(output);
                writer.flush();
                writer.close();
                int HttpResult =urlConnection.getResponseCode();
                if(HttpResult ==HttpURLConnection.HTTP_OK){
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                }else{
                    System.out.println(urlConnection.getResponseMessage());
                }


            }catch( Exception e) {
                e.printStackTrace();
            }
            finally {
                urlConnection.disconnect();
            }

            elapsedTime = System.currentTimeMillis() - startTime;
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            delegate.processFinish(result,v,elapsedTime);

        }

    }
}
