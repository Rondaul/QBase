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
import android.widget.EditText;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AdminActivity extends AppCompatActivity {

    @InjectView(R.id.admin_department_spinner)
    Spinner mAdminDepartmentSpinner;
    @InjectView(R.id.admin_semester_spinner)
    Spinner mAdminSemesterSpinner;
    @InjectView(R.id.admin_subject_spinner)
    Spinner mAdminSubjectsSpinner;
    @InjectView(R.id.admin_year_spinner)
    Spinner mAdminYearSpinner;
    @InjectView(R.id.admin_exam_spinner)
    Spinner mAdminExamSpinner;
    @InjectView(R.id.question_add_button)
    Button mQuestionAddButton;
    @InjectView(R.id.admin_question_editText)
    EditText mQuestionEditText;
    @InjectView(R.id.admin_marks_editText)
    EditText mMarksEditText;

    //HashMap to link the values
    private Map<String, Integer> mAdminDepartmentsHashMap = new HashMap<>();
    private Map<String, Integer> mAdminSubjectsHashMap = new HashMap<>();
    private Map<String, Integer> mAdminYearHashMap = new HashMap<>();
    private Map<String, Integer> mAdminExamHashMap = new HashMap<>();

    String question;
    String marks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ButterKnife.inject(this);

        mAdminDepartmentsHashMap.put("BCA" , 1);
        mAdminDepartmentsHashMap.put("BBA" , 2);

        mAdminSubjectsHashMap.put("Java Programming", 1);
        mAdminSubjectsHashMap.put("Financial Accounting", 2);

        //Create an array adapter from the array resouce
        final ArrayAdapter mDepartmentAdapter = ArrayAdapter.createFromResource(this,
                R.array.departments, android.R.layout.simple_spinner_item);

        mDepartmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Set the adapter for department spinner
        mAdminDepartmentSpinner.setAdapter(mDepartmentAdapter);
        mAdminDepartmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).toString().equals("--Select--")) {
                    //Do nothing
                } else if (parent.getItemAtPosition(position).toString().equals("BCA")) {
                    ArrayAdapter mSemesterAdapter = ArrayAdapter.createFromResource(AdminActivity.this,
                            R.array.semesters, android.R.layout.simple_spinner_item);
                    mSemesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mAdminSemesterSpinner.setAdapter(mSemesterAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mAdminSemesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int subjectArrayId = R.array.bca_first_sem;
                if(parent.getItemAtPosition(position).toString().equals("--Select--")) {
                    //Do nothing
                } else if (parent.getItemAtPosition(position).toString().equals("1st Semester") && mAdminDepartmentSpinner.getSelectedItem().toString().equals("BCA")) {
                    subjectArrayId = R.array.bca_first_sem;
                } else if (parent.getItemAtPosition(position).toString().equals("2nd Semester") && mAdminDepartmentSpinner.getSelectedItem().toString().equals("BCA")) {
                    subjectArrayId = R.array.bca_second_sem;
                } else if (parent.getItemAtPosition(position).toString().equals("3rd Semester") && mAdminDepartmentSpinner.getSelectedItem().toString().equals("BCA")) {
                    subjectArrayId = R.array.bca_third_sem;
                } else if (parent.getItemAtPosition(position).toString().equals("4th Semester") && mAdminDepartmentSpinner.getSelectedItem().toString().equals("BCA")) {
                    subjectArrayId = R.array.bca_fourth_sem;
                } else if (parent.getItemAtPosition(position).toString().equals("5th Semester") && mAdminDepartmentSpinner.getSelectedItem().toString().equals("BCA")) {
                    subjectArrayId = R.array.bca_fifth_sem;
                } else if(parent.getItemAtPosition(position).toString().equals("6th Semester") && mAdminDepartmentSpinner.getSelectedItem().toString().equals("BCA")) {
                    subjectArrayId = R.array.bca_sixth_sem;
                }

                ArrayAdapter mSubjectAdapter = ArrayAdapter.createFromResource(AdminActivity.this,
                        subjectArrayId, android.R.layout.simple_spinner_item);
                mSubjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mAdminSubjectsSpinner.setAdapter(mSubjectAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mAdminSemesterSpinner.setAdapter(null);
            }
        });

        mAdminSubjectsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter mYearAdapter = ArrayAdapter.createFromResource(AdminActivity.this,
                        R.array.year , android.R.layout.simple_spinner_item);
                mYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mAdminYearSpinner.setAdapter(mYearAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mAdminSemesterSpinner.setAdapter(null);
            }
        });

        mAdminYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter mExamsAdapter = ArrayAdapter.createFromResource(AdminActivity.this
                        , R.array.exams , android.R.layout.simple_spinner_item);
                mExamsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mAdminExamSpinner.setAdapter(mExamsAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mAdminSemesterSpinner.setAdapter(null);
            }
        });

        mQuestionAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question = mQuestionEditText.getText().toString();

                marks = mMarksEditText.getText().toString();

                int subject = mAdminSubjectsHashMap.get(mAdminSubjectsSpinner.getSelectedItem().toString());
                int year = Integer.parseInt(mAdminYearSpinner.getSelectedItem().toString());
                String exam = mAdminExamSpinner.getSelectedItem().toString();

                Log.d("values: ",+ subject + " " + year + " "+ exam);

                InsertQuestionsBackgroundTask mInsertQuestions = new InsertQuestionsBackgroundTask(AdminActivity.this);
                mInsertQuestions.execute(Integer.toString(subject), Integer.toString(year), exam, question, marks);
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
