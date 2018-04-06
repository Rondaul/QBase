package com.rontrix.android.q_base;

/**
 * Created by Ron on 4/5/2018.
 */

public class Question {
    String question;
    String marks;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", marks='" + marks + '\'' +
                '}';
    }
}
