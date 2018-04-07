package com.rontrix.android.q_base;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

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
 * Created by Ron on 4/4/2018.
 */

public class FetchQuestionsBackgroundTask extends AsyncTask<Integer, Void, String> {

    //String url for connecting to mysql database using php script.
    private static final String STRING_URL = "http://192.168.0.6/WebDevWork/mysqlconnect/qbaseoperations/q-base-operation.php";

    private static final String ENCODING_SCHEME = "UTF-8";

    Activity mActivity;
    private int department;
    private int semester;
    private int subject;
    private int year;
    private int exam;

    public FetchQuestionsBackgroundTask(Activity activity) {
        mActivity = activity;
    }

    @Override
    protected String doInBackground(Integer... integers) {
        department = integers[0];
        semester = integers[1];
        subject = integers[2];
        year = integers[3];
        exam = integers[4];

        URL url = createURL(STRING_URL);

        String result = makeHTTPRequest(url);
        return result;
    }

    private String makeHTTPRequest(URL url) {
        String result = "";
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, ENCODING_SCHEME));

            String post_data = URLEncoder.encode("department", ENCODING_SCHEME) + "=" + URLEncoder.encode(Integer.toString(department), ENCODING_SCHEME) + "&"
                    + URLEncoder.encode("semester", ENCODING_SCHEME) + "=" + URLEncoder.encode(Integer.toString(semester), ENCODING_SCHEME) + "&"
                    + URLEncoder.encode("subject", ENCODING_SCHEME) + "=" + URLEncoder.encode(Integer.toString(subject), ENCODING_SCHEME) + "&"
                    + URLEncoder.encode("year", ENCODING_SCHEME) + "=" + URLEncoder.encode(Integer.toString(year), ENCODING_SCHEME) + "&"
                    + URLEncoder.encode("exam", ENCODING_SCHEME) + "=" + URLEncoder.encode(Integer.toString(exam), ENCODING_SCHEME);

            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasNext = scanner.hasNext();

            if (hasNext) {
                result = scanner.next();
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
            return result;
        }
    }

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
    protected void onPostExecute(String result) {
        Intent intent = new Intent(mActivity, QuestionsResultsActivity.class);
        intent.putExtra("jsonString" , result);
        mActivity.startActivity(intent);

    }
}
