package com.rontrix.android.q_base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG= "LoginActivity";

    //Dependency Injection using ButterKnife
    @InjectView(R.id.btnLinkToRegisterScreen) Button mRegisterLinkButton;
    @InjectView(R.id.login_email) EditText mLogEmailEditText;
    @InjectView(R.id.login_password) EditText mLogPassEditText;
    @InjectView(R.id.btnLogin) Button mLoginButton;
    @InjectView(R.id.progressBar) ProgressBar mLoginProgressBar;

    //Background Task input variables
    String email;
    String password;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Injecting views in this activity using Butterknife
        ButterKnife.inject(this);

        //Handle login button onClick event
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        //Set onClickListener to pass an intent and go to the Register activity
        mRegisterLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }

    /** Method to start a background thread using AsyncTask and
     * login the user by the credentials provided in the
     * EditText of respective fields and checking the data in the
     * database.
     */
    private void login() {
        Log.d(TAG, "Login");
        if(!validate()) {
            onLoginFailed();
        } else {
                BackgroundTask backgroundTask = new BackgroundTask(this);
                backgroundTask.execute(type, email, password);
        }
    }

    /** Method to validate whether and fields are properly entered.
     * Like whether the length matches the required length and email is
     * of correct format and return a boolean value to indicate whether
     * the validation is true or false.
     * @return
     */
    private boolean validate() {
        boolean valid = true;

        email = mLogEmailEditText.getText().toString();
        password = mLogPassEditText.getText().toString();
        type = "Login";

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mLogEmailEditText.setError("Enter a valid email address");
            valid = false;
        } else {
            mLogEmailEditText.setError(null);
        }

        if(password.isEmpty() || password.length() < 6 || password.length() > 16) {
            mLogPassEditText.setError("Between 6 to 16 characters");
            valid = false;
        } else {
            mLogPassEditText.setError(null);
        }

        return valid;
    }

    //Method for creating a Toast message on login Fail.
    private void onLoginFailed() {
        Toast.makeText(this, "Login Failed!" , Toast.LENGTH_SHORT).show();
        mLoginButton.setEnabled(true);
    }

}
