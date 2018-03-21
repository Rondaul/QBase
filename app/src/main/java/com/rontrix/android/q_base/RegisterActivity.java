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

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    //Dependency injection using ButterKnife
    @InjectView(R.id.btnLinkToLoginScreen) Button mLoginLinkButton;
    @InjectView(R.id.register_name) EditText mRegNameEditText;
    @InjectView(R.id.register_email) EditText mRegEmailEditText;
    @InjectView(R.id.register_password) EditText mRegPasswordEditText;
    @InjectView(R.id.btnRegister) Button mRegisterButton;
    @InjectView(R.id.register_progress_bar) ProgressBar mRegisterProgressBar;

    //BackgroundTask input parameters
    String name;
    String email;
    String password;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Injecting views in this activity using Butterknife
        ButterKnife.inject(this);

        //Handle Register Button onClick event
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        //set onClickListener to pass an intent and go to Login Activity
        mLoginLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    /** Method to start a background thread using AsyncTask and
     * register the user by the credentials provided in the
     * EditText of respective fields and storing the data in the
     * database
     */
    private void register() {
        Log.i(TAG, "Register!");
        if(!validate()) {
            registerFailed();
        } else {
            //start Background AsyncTask if validate is successful
            BackgroundTask backgroundTask = new BackgroundTask(this, mRegisterProgressBar);
            backgroundTask.execute(type, email, password, name);
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
        name = mRegNameEditText.getText().toString();
        email = mRegEmailEditText.getText().toString();
        password = mRegPasswordEditText.getText().toString();
        type = "Register";

        if(name.isEmpty() || name.length() < 3) {
            mRegNameEditText.setError("Atleast 3 characters!");
            valid = false;
        } else {
            mRegNameEditText.setError(null);
        }

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mRegEmailEditText.setError("Enter a valid email address!");
            valid = false;
        } else {
            mRegEmailEditText.setError(null);
        }

        if(password.isEmpty() || password.length() < 6 || password.length() > 16) {
            mRegPasswordEditText.setError("Between 6 to 16 characters!");
            valid = false;
        } else {
            mRegPasswordEditText.setError(null);
        }

        return valid;
    }

    //Method for creating a Toast message on registration Fail.
    private void registerFailed() {
        Toast.makeText(this, "Registration Failed!", Toast.LENGTH_SHORT).show();
        mRegisterButton.setEnabled(true);
    }
}
