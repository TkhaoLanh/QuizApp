package com.example.quizapp;

import android.graphics.Color;

import com.example.quizapp.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {
    ArrayList<Question> questionList;

    public ArrayList<Question> getQuestionList() {
        ArrayList<Question> questionList = new ArrayList<>();
        //add Question
        questionList.add(new Question(R.string.question1,false, Color.RED));
        questionList.add(new Question(R.string.question2,false, Color.MAGENTA));
        questionList.add(new Question(R.string.question3,true, Color.GREEN));
        questionList.add(new Question(R.string.question4,true, Color.MAGENTA));
        questionList.add(new Question(R.string.question5,false, Color.BLUE));
        questionList.add(new Question(R.string.question6,true, Color.GREEN));
        questionList.add(new Question(R.string.question7,false, Color.RED));
        questionList.add(new Question(R.string.question8,true, Color.BLACK));
        questionList.add(new Question(R.string.question9,false, Color.GRAY));
        questionList.add(new Question(R.string.question10,true, Color.DKGRAY));

        return questionList;
    }

}
