package com.rontrix.android.q_base;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

/**
 * Created by Ron on 4/7/2018.
 */

class InsertQuestionsBackgroundTask extends AsyncTask<String, Void, String> {

    //String url for connecting to mysql database using php script.
    //private static final String STRING_URL = "http://192.168.43.198/WebDevWork/mysqlconnect/qbaseoperations/q-base-operation.php";
    private static final String STRING_URL = "https://c4code.000webhostapp.com/mysqlconnect/qbaseoperations/q-base-operation.php";

    Activity mActivity;
    private String marks;
    private String question;
    private String exam;
    private String year;
    private String subject;

    public InsertQuestionsBackgroundTask(Activity activity) {
        mActivity = activity;
    }

    @Override
    protected String doInBackground(String... strings) {
        subject = strings[0];
        year = strings[1];
        exam = strings[2];
        question = strings[3];
        marks = strings[4];


        //Create url from String url by calling the createURL method and
        //passing the String url
        URL url = createURL(STRING_URL);

        return makeHTTPRequest(url);
    }

    private String makeHTTPRequest(URL url) {
        HttpURLConnection httpURLConnection = null;
        String result = "";
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            //Encoding the data by using URLEncoder for efficiently passing the data to the database
            String post_data = URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(subject, "UTF-8") + "&"
                    + URLEncoder.encode("year", "UTF-8") + "=" + URLEncoder.encode(year, "UTF-8") + "&"
                    + URLEncoder.encode("exam", "UTF-8") + "=" + URLEncoder.encode(exam, "UTF-8") + "&"
                    + URLEncoder.encode("question", "UTF-8") + "=" + URLEncoder.encode(question, "UTF-8") + "&"
                    + URLEncoder.encode("marks", "UTF-8") + "=" + URLEncoder.encode(marks, "UTF-8");

            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasNext = scanner.hasNext();

            if(hasNext) {
                result = scanner.next();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
             httpURLConnection.disconnect();
             return result;
        }
    }

    //Method for creating url from equivalent string url
    private URL createURL(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            Log.d("String Value" , s);
            JSONObject jsonObject = new JSONObject(s);

            Toast.makeText(mActivity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
