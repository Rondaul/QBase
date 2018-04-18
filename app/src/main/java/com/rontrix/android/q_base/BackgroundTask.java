package com.rontrix.android.q_base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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
 * Created by Ron on 3/16/2018.
 */

public class BackgroundTask extends AsyncTask<String, Void, JSONObject> {

    //String url for connecting to mysql database using php script.
    //private static final String STRING_URL = "http://192.168.43.198/WebDevWork/mysqlconnect/index.php";
    private static final String STRING_URL = "https://c4code.000webhostapp.com/mysqlconnect/index.php";

    //instance variable to store the value passed from Activity
    private String email;
    private String password;
    private String type;
    private String name;
    private Activity mActivity;
    private ProgressBar mLoginProgressBar;

    public BackgroundTask(Activity activity, ProgressBar LoginProgressBar) {
                mActivity = activity;
                mLoginProgressBar = LoginProgressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mLoginProgressBar.setVisibility(View.VISIBLE);

    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        //If type equals "Login" , then execute the if condition
        if(strings[0].equals("Login")) {
            //Retrieve data passed from LoginActivity and set it to instance variable
            type = strings[0];
            email = strings[1];
            password = strings[2];
        }
        //else if type equals "Register" , then execute the else condition
        else if(strings[0].equals("Register")) {
            //Retrieve data passed from RegisterActivity and set it to instance variable
            type = strings[0];
            email = strings[1];
            password = strings[2];
            name = strings[3];
        }

        //Create url from String url by calling the createURL method and
        //passing the String url
        URL url = createURL(STRING_URL);

        JSONObject jsonObject = null;
        try {
            //Get jsonObject by calling makeHTTPRequest and performing the http request on
            //the url passed
            jsonObject = makeHTTPRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonObject;

    }

    /**Method for http request and performing output and input operations to the mysql database
      *and posting the data and getting the data from the database
     */
    private JSONObject makeHTTPRequest(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        OutputStream outputStream = httpURLConnection.getOutputStream();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
        String post_data = "";
        //Encoding the data by using URLEncoder for efficiently passing the data to the database
        if(type.equals("Login")) {
            post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&"
                    + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
        } else if(type.equals("Register")){
            post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&"
                    + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&"
                    + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
        }
        bufferedWriter.write(post_data);
        bufferedWriter.flush();
        bufferedWriter.close();
        outputStream.close();

        InputStream inputStream = httpURLConnection.getInputStream();

        //Create scanner object for getting the String data from the inputstream
        Scanner scanner = new Scanner(inputStream);
        scanner.useDelimiter("\\A");

        boolean hasNext = scanner.hasNext();
        String result = "";
        if (hasNext) {
            result = scanner.next();
        }

        JSONObject jsonObject = null;
        try {
            Log.d("Result" , result);
            //convert string to JSONObject
            jsonObject = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            httpURLConnection.disconnect();
            return jsonObject;
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
    protected void onPostExecute(JSONObject jsonObject) {
        mLoginProgressBar.setVisibility(View.GONE);
        if(jsonObject != null) {
            try {
                if(jsonObject.getString("success").equals("0")) {
                        //if operation is unsuccessfull, show a toast message that operation is failed.
                        Toast.makeText(mActivity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    if(jsonObject.getString("success").equals("1")) {
                        //if operation is successful and user is admin show a toast message for admin
                        if(jsonObject.getString("user").equals("admin")) {
                            Toast.makeText(mActivity, "Welcome admin!" , Toast.LENGTH_SHORT).show();
                        } else {
                            //If operation is successful and user is others, show a toast message that operation is successful.
                            Toast.makeText(mActivity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                        //If type is login, then start a new Activity using intent
                        if(type.equals("Login")) {

                            /**Added SharedPreferences to store the information whether the user is Login or not.
                            * If user is login, then a preference value is set with key="isLogin" and value="true"
                             */
                            SharedPreferences sharedPreferences = mActivity.getSharedPreferences("myPref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isLogin", true);

                            //if user is admin, set isAdmin to true
                            if(jsonObject.getString("user").equals("admin")) {
                                editor.putBoolean("isAdmin" , true);
                            } else {
                                //else if user is others, set isAdmin to false
                                editor.putBoolean("isAdmin" , false);
                            }
                            editor.apply();

                            Intent intent;

                            if(jsonObject.getString("user").equals("admin")) {
                                //If login is successful and user is admin, go to AdminActivity
                                intent = new Intent(this.mActivity, AdminActivity.class);
                            } else {

                                //If login is successful and user is others, go to QuestionSearchActivity
                                intent = new Intent(this.mActivity, QuestionSearchActivity.class);
                            }
                            this.mActivity.startActivity(intent);
                        }
                        //Else if type is register, then just finish the RegisterActivity and go back to
                        //LoginActivity
                        else if(type.equals("Register")) {
                            this.mActivity.finish();
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
