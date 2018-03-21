package com.rontrix.android.q_base;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class QuestionSearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @InjectView(R.id.department_spinner) Spinner mDepartmentSpinner;
    @InjectView(R.id.semester_spinner) Spinner mSemesterSpinner;
    @InjectView(R.id.subject_spinner) Spinner mSubjectsSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_search);
        ButterKnife.inject(this);

        //Create an array adapter from the array resouce
        ArrayAdapter mDepartmentAdapter = ArrayAdapter.createFromResource(this,
                R.array.departments, android.R.layout.simple_spinner_dropdown_item);

        //Set the adapter for department spinner
        mDepartmentSpinner.setAdapter(mDepartmentAdapter);
        mDepartmentSpinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ArrayAdapter mSemesterAdapter = ArrayAdapter.createFromResource(this,
                R.array.semesters, android.R.layout.simple_spinner_dropdown_item);
        mSemesterSpinner.setAdapter(mSemesterAdapter);

        Log.d("Spinner value", mDepartmentSpinner.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedId = item.getItemId();

        /**If selected item matches R.id.action_logout, then
         * change the value of sharepreference key="isLogin" to false
         * and save it and start the loginActivity
         */
        if(selectedId == R.id.action_logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLogin", false);
            editor.apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
