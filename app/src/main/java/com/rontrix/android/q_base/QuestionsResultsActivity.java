package com.rontrix.android.q_base;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class QuestionsResultsActivity extends AppCompatActivity {

    @InjectView(R.id.question_results_recycler_view)
    RecyclerView mRecyclerView;

    private QuestionsAdapter mQuestionsAdapter;

    private List<Question> mQuestions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_results);
        ButterKnife.inject(this);

        String result = getIntent().getStringExtra("jsonString");

        Log.d("result", result);

        createQuestionList(result);

        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mQuestionsAdapter = new QuestionsAdapter(this, mQuestions);
        mRecyclerView.setAdapter(mQuestionsAdapter);

    }

    private void createQuestionList(String result) {
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String question = jsonObject.getString("question");
                String marks = jsonObject.getString("marks");

                Question questionObject = new Question();
                questionObject.setQuestion(question);
                questionObject.setMarks(marks);

                Log.d("questionObject", questionObject.toString());

                mQuestions.add(questionObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedId = item.getItemId();

        if (selectedId == R.id.action_save_pdf) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    createPDF();
                    return true;
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    return false;
                }
            } else { //permission is automatically granted on sdk<23 upon installation
                createPDF();
                return true;
            }
        }

        if(selectedId == R.id.action_share) {
            sharePDF();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v("Permission", "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
            createPDF();

        }
    }

    private void sharePDF() {

        String questions =  "";

        for(Question question : mQuestions) {
            questions += question.getQuestion() + "          " + question.getMarks() + "\n" ;
        }

        //Create a String variable called mimeType and set it to "application/pdf"
        String mimeType = "text/plain";

        // COMPLETED (3) Create a title for the chooser window that will pop up
        String title = "Share Title";

        // COMPLETED (4) Use ShareCompat.IntentBuilder to build the Intent and start the chooser
        Intent intent = ShareCompat.IntentBuilder.from(this)
                .setChooserTitle(title)
                .setType(mimeType)
                .setText(questions)
                .getIntent();

        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    public void createPDF() {

        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File dir = new File(root + File.separator + "MyPDFDocument");

            Log.d("public Dir", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());

            Log.d("dir", "" + dir.mkdirs());

            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Toast.makeText(this, "Directory is not created", Toast.LENGTH_SHORT).show();
                }
            }

            File filePath = new File(dir, "questions.pdf");

            Document document = new Document(PageSize.A4, 0f, 0f, 0f, 0f);

            try {
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();

                StringBuilder stringBuilder = new StringBuilder();

                for (Question question : mQuestions) {
                    stringBuilder.append(question.getQuestion() + "              " + question.getMarks() + "\n");
                }

                 String questionString = stringBuilder.toString();

                Log.d("String", questionString);

                float fntSize = 25f;
                float lineSpacing = 30f;
                Paragraph paragraph = new Paragraph(new Phrase(lineSpacing, questionString, FontFactory.getFont(FontFactory.COURIER, fntSize)));
                paragraph.setAlignment(Paragraph.ALIGN_CENTER);
                paragraph.setFont(new Font(Font.FontFamily.COURIER));
                document.add(paragraph);
                Toast.makeText(this, "Saved to PDF", Toast.LENGTH_LONG).show();

                // close the document
                document.close();

            } catch (DocumentException e) {
                e.printStackTrace();
                Toast.makeText(this, "Save Unsuccessful!",
                        Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Save Unsuccessful!",
                        Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "External Storage not available!", Toast.LENGTH_SHORT).show();
        }

    }
}


