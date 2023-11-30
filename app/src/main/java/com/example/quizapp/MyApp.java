package com.example.quizapp;

import android.app.Application;

import java.util.ArrayList;


public class MyApp extends Application {
    ArrayList<Question> questionListMyApp;
    FileManager fileManager = new FileManager();
    int questionIndex;
    int correctAnswers;
    int numOfQuestions;

    public int getQuestionIndex() {
        return questionIndex;
    }

    public void setQuestionIndex(int questionIndex) {
        this.questionIndex = questionIndex;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getNumOfQuestions() {
        return numOfQuestions;
    }

    public void setNumOfQuestions(int numOfQuestions) {
        this.numOfQuestions = numOfQuestions;
    }

    public ArrayList<Question> getQuestionListMyApp() {
        if(questionListMyApp == null){
            questionListMyApp = new ArrayList<>();
        }

        return questionListMyApp;
    }

    public void setQuestionListMyApp(ArrayList<Question> questionListMyApp) {
        this.questionListMyApp = questionListMyApp;
    }
    public void updateQuestionList(ArrayList<Question> updatedList) {
        questionListMyApp = updatedList;
    }

    public int getQuestionListSize() {
        return questionListMyApp.size();
    }
}
