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
import android.widget.Button;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class QuestionSearchActivity extends AppCompatActivity {

    @InjectView(R.id.department_spinner)
    Spinner mDepartmentSpinner;
    @InjectView(R.id.semester_spinner)
    Spinner mSemesterSpinner;
    @InjectView(R.id.subject_spinner)
    Spinner mSubjectsSpinner;
    @InjectView(R.id.year_spinner)
    Spinner mYearSpinner;
    @InjectView(R.id.exam_spinner)
    Spinner mExamSpinner;
    @InjectView(R.id.question_search_button)
    Button mQuestionSearchButton;

    //HashMap to link the values
    private Map<String, Integer> mDepartmentsHashMap = new HashMap<>();
    private Map<String, Integer> mSubjectsHashMap = new HashMap<>();
    private Map<String, Integer> mYearHashMap = new HashMap<>();
    private Map<String, Integer> mExamHashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_search);
        ButterKnife.inject(this);

        mDepartmentsHashMap.put("BCA" , 1);
        mDepartmentsHashMap.put("BBA" , 2);

        mSubjectsHashMap.put("Java Programming", 1);
        mSubjectsHashMap.put("Financial Accounting", 2);

        mExamHashMap.put("Final Exam", 1);

        //Create an array adapter from the array resouce
        final ArrayAdapter mDepartmentAdapter = ArrayAdapter.createFromResource(this,
                R.array.departments, android.R.layout.simple_spinner_item);

        mDepartmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Set the adapter for department spinner
        mDepartmentSpinner.setAdapter(mDepartmentAdapter);
        mDepartmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).toString().equals("--Select--")) {
                    //Do nothing
                } else if (parent.getItemAtPosition(position).toString().equals("BCA")) {
                    ArrayAdapter mSemesterAdapter = ArrayAdapter.createFromResource(QuestionSearchActivity.this,
                            R.array.semesters, android.R.layout.simple_spinner_item);
                    mSemesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSemesterSpinner.setAdapter(mSemesterAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSemesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int subjectArrayId = R.array.bca_first_sem;
                if(parent.getItemAtPosition(position).toString().equals("--Select--")) {
                    //Do nothing
                } else if (parent.getItemAtPosition(position).toString().equals("1st Semester") && mDepartmentSpinner.getSelectedItem().toString().equals("BCA")) {
                    subjectArrayId = R.array.bca_first_sem;
                } else if (parent.getItemAtPosition(position).toString().equals("2nd Semester") && mDepartmentSpinner.getSelectedItem().toString().equals("BCA")) {
                    subjectArrayId = R.array.bca_second_sem;
                } else if (parent.getItemAtPosition(position).toString().equals("3rd Semester") && mDepartmentSpinner.getSelectedItem().toString().equals("BCA")) {
                    subjectArrayId = R.array.bca_third_sem;
                } else if (parent.getItemAtPosition(position).toString().equals("4th Semester") && mDepartmentSpinner.getSelectedItem().toString().equals("BCA")) {
                    subjectArrayId = R.array.bca_fourth_sem;
                } else if (parent.getItemAtPosition(position).toString().equals("5th Semester") && mDepartmentSpinner.getSelectedItem().toString().equals("BCA")) {
                    subjectArrayId = R.array.bca_fifth_sem;
                } else if(parent.getItemAtPosition(position).toString().equals("6th Semester") && mDepartmentSpinner.getSelectedItem().toString().equals("BCA")) {
                    subjectArrayId = R.array.bca_sixth_sem;
                }

                ArrayAdapter mSubjectAdapter = ArrayAdapter.createFromResource(QuestionSearchActivity.this,
                        subjectArrayId, android.R.layout.simple_spinner_item);
                mSubjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSubjectsSpinner.setAdapter(mSubjectAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSemesterSpinner.setAdapter(null);
            }
        });

        mSubjectsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter mYearAdapter = ArrayAdapter.createFromResource(QuestionSearchActivity.this,
                        R.array.year , android.R.layout.simple_spinner_item);
                    mYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mYearSpinner.setAdapter(mYearAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSemesterSpinner.setAdapter(null);
            }
        });

        mYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter mExamsAdapter = ArrayAdapter.createFromResource(QuestionSearchActivity.this
                , R.array.exams , android.R.layout.simple_spinner_item);
                mExamsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mExamSpinner.setAdapter(mExamsAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSemesterSpinner.setAdapter(null);
            }
        });

        mQuestionSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int department = mDepartmentsHashMap.get(mDepartmentSpinner.getSelectedItem().toString());
                int semester = mSemesterSpinner.getSelectedItemPosition();
                int subject = mSubjectsHashMap.get(mSubjectsSpinner.getSelectedItem().toString());
                int year = Integer.parseInt(mYearSpinner.getSelectedItem().toString());
                int exam = mExamHashMap.get(mExamSpinner.getSelectedItem().toString());

                Log.d("values: ", department +" "+ semester +" " + subject + " " + year + " "+ exam);

                FetchQuestionsBackgroundTask mFetchQuestions = new FetchQuestionsBackgroundTask(QuestionSearchActivity.this);
                mFetchQuestions.execute(department, semester, subject, year, exam);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedId = item.getItemId();

        /**If selected item matches R.id.action_logout, then
         * change the value of sharepreference key="isLogin" to false
         * and save it and start the loginActivity
         */
        if (selectedId == R.id.action_logout) {
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
