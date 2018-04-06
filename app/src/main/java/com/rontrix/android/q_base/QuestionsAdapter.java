package com.rontrix.android.q_base;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ron on 4/5/2018.
 */

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder> {
    private Activity mActivity;
    private List<Question> mQuestions = new ArrayList<>();


    public QuestionsAdapter(Activity activity, List<Question> questions) {
        mActivity = activity;
        mQuestions = questions;
    }

    @Override
    public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mActivity)
                .inflate(R.layout.list_item, parent, false);

        return new QuestionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(QuestionViewHolder holder, int position) {
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return mQuestions.size();
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView mQuestionTextView;
        TextView mMarksTextView;

        public QuestionViewHolder(View itemView) {
            super(itemView);
            mQuestionTextView = itemView.findViewById(R.id.question_text_view);
            mMarksTextView = itemView.findViewById(R.id.marks_text_view);


        }

        public void bind(int position) {
            Question question = mQuestions.get(position);
            mQuestionTextView.setText(question.getQuestion());
            mMarksTextView.setText(question.getMarks());
        }
    }
}
