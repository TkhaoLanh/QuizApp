package com.example.quizapp;

import static com.example.quizapp.R.*;
import static com.example.quizapp.R.id.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    ArrayList<Question> questionArrayList;
    QuestionFragment questionFragment;
    QuestionBank questionBank;
    Button trueBTN, falseBTN;

    FrameLayout frameLayout;
    ProgressBar progressBar;
    int currentQuestionIndex = 0;
    int correctAnswer = 0;

    FileManager fileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        trueBTN = findViewById(trueBtn);
        falseBTN = findViewById(falseBtn);
        progressBar = findViewById(progessBar);
        frameLayout = findViewById(framelayout);

        fileManager = ((MyApp)getApplication()).fileManager;

        trueBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        falseBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        // Instantiate the QuestionBank class and retrieve the list of questions
        questionBank = new QuestionBank();
        questionArrayList = questionBank.getQuestionList();

        progressBar.setMax(questionArrayList.size()); // Set the maximum value of the progress bar

        // Shuffle list
        Collections.shuffle(questionArrayList);
       ((MyApp)getApplication()).setQuestionListMyApp(questionArrayList);

        String questionText = getString(questionArrayList.get(currentQuestionIndex).getQuestion());
        int questionColor = questionArrayList.get(currentQuestionIndex).getColor();
        questionFragment = QuestionFragment.newInstance(questionText, questionColor);

       QuestionFragment check_fragment = (QuestionFragment) getSupportFragmentManager().findFragmentById(framelayout);
        if (check_fragment == null) {
            getSupportFragmentManager().beginTransaction().add(framelayout, questionFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().remove(check_fragment).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentQuestionIndex",currentQuestionIndex);
        outState.putInt("correctAnswer",correctAnswer);
        outState.putParcelableArrayList("questionArrayList", (ArrayList<? extends Parcelable>) questionArrayList);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentQuestionIndex = savedInstanceState.getInt("currentQuestionIndex");
        correctAnswer = savedInstanceState.getInt("correctAnswer");
        questionArrayList = savedInstanceState.getParcelableArrayList("questionArrayList");

        // Update progress bar
        progressBar.setMax(questionArrayList.size()); // Set the maximum value of the progress bar
        progressBar.setProgress(currentQuestionIndex); // Set the current progress

        //display current question
        displayQuestion(currentQuestionIndex);
    }

    private void checkAnswer(boolean selectedAnswer){
        Question currentQuestion = questionArrayList.get(currentQuestionIndex);
        boolean correctAns = currentQuestion.isAnswer();

        // Show a toast with the result
        if(selectedAnswer == correctAns){
            Toast.makeText(this, string.correct, Toast.LENGTH_SHORT).show();
            correctAnswer++;
        }else{
            Toast.makeText(this, string.incorrect, Toast.LENGTH_SHORT).show();
        }

        // Update progress bar
        ProgressBar progressBar = findViewById(progessBar);
        progressBar.setProgress(currentQuestionIndex + 1);

        // Move to the next question if available
        if (currentQuestionIndex < questionArrayList.size() - 1) {
            currentQuestionIndex++;
            displayQuestion(currentQuestionIndex);
        }else {
            //complete questions
            completeQuiz();
        }
    }

    //display question
    private void displayQuestion(int index) {
        if (index >= 0 && index < questionArrayList.size()) {
            Question currentQuestion = questionArrayList.get(index);
            String questionText = getString(currentQuestion.getQuestion());
            int questionColor = currentQuestion.getColor();

            // Update the QuestionFragment with the next question
            questionFragment = QuestionFragment.newInstance(questionText, questionColor);
            getSupportFragmentManager().beginTransaction()
                    .replace(framelayout, questionFragment)
                    .commit();
        }
    }
    //finish quiz
    private void completeQuiz(){
        //display AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(string.result);
        builder.setMessage(getString(string.yourScore) + " " + correctAnswer + " " + getString(string.outOf) + " " + questionArrayList.size());

        //buttons in diaglog
        builder.setPositiveButton(string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fileManager.writeQuizFile(MainActivity.this,correctAnswer);
                AlertDialog.Builder saveMess = new AlertDialog.Builder(MainActivity.this);
                saveMess.setMessage(string.saved);
                saveMess.setPositiveButton(string.ok,null);
                AlertDialog alertDialog = saveMess.create();
                alertDialog.show();
                resetQuiz();

            }
        });

        builder.setNegativeButton(string.ignore, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                resetQuiz();
            }
        });
        builder.show();
    }
    //reset quiz
    private void resetQuiz(){
        correctAnswer = 0;
        currentQuestionIndex = 0;
        //Shuffle questions
        Collections.shuffle(questionArrayList);
        //reset progress bar
        progressBar.setProgress(0);
        //display the first question
        displayQuestion(currentQuestionIndex);
    }

    public void showAverage(){
        int total = fileManager.calculateAverageScore(MainActivity.this);
        int attemtps = fileManager.getNumberOfAttempts(MainActivity.this);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setMessage(getString(string.correctAns)+" " + total+" "+getString(string.in)+" "+ attemtps +" "+getString(string.attempt));
        alertDialogBuilder.setPositiveButton(string.ok, null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    //reset the saved result
    public void resetSavedResult(){

        //display AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(string.resetSavedResult));

        //buttons in diaglog
        builder.setPositiveButton(string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //delete
                fileManager.deleteScore(MainActivity.this);
                //show confirm message
                AlertDialog.Builder delMess = new AlertDialog.Builder(MainActivity.this);
                delMess.setMessage(string.deleted);
                delMess.setPositiveButton(string.ok, null);
                AlertDialog alertDialog = delMess.create();
                alertDialog.show();;

            }
        });

        builder.setNegativeButton(string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    //set the number of questions
    private void handleNumberOfQuestionsSelected(int numOfQuestions) {
        // Get the full list of questions
        questionArrayList = questionBank.getQuestionList();
        // Shuffle the entire list of questions
        Collections.shuffle(questionArrayList);

        // Update the questionArrayList to contain only the selected number of questions
        if (numOfQuestions >= questionArrayList.size()) {
            // If the selected number is greater than or equal to the total questions available,
            // keep the entire list as it is
            return;
        }

        // Create a new list to store the selected number of questions
        ArrayList<Question> selectedQuestions = new ArrayList<>(questionArrayList.subList(0, numOfQuestions));
        questionArrayList = selectedQuestions;

        // Update the question list in MyApp
        ((MyApp) getApplication()).updateQuestionList(questionArrayList);

        // Update progress bar
        int selectedNumOfQuestions = questionArrayList.size();
        progressBar.setMax(selectedNumOfQuestions); // Update the maximum value of the progress bar
        // Reset the current question index
        currentQuestionIndex = 0;

        progressBar.setProgress(currentQuestionIndex); // Update the current progress

        //display question
        displayQuestion(currentQuestionIndex);

        // Reset the quiz
        resetQuiz();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.quiz_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.resetResult:
                resetSavedResult();
                return true;
            case R.id.getTotal:
                showAverage();
                return true;
            case R.id.num_3:
                handleNumberOfQuestionsSelected(3);
                return true;
            case R.id.num_5:
                handleNumberOfQuestionsSelected(5);
                return true;
            case createNewQues:
                createNewQuestionDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //create a new question
    public void createNewQuestionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(string.createNewQues);

        View view = getLayoutInflater().inflate(layout.dialog_new_question, null);
        builder.setView(view);

        EditText editTextQuestion = view.findViewById(newQuestion);
        RadioGroup radioGroupOptions = view.findViewById(radioGroup);
        Spinner spinnerColors = view.findViewById(colorSpinner);

        // Set up the spinner with color options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.color_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColors.setAdapter(adapter);
        builder.setPositiveButton(string.create, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get user input and create the new question
                String questionText = editTextQuestion.getText().toString();
                boolean isTrue = radioGroupOptions.getCheckedRadioButtonId() == trueBtn;
                String selectedColor = spinnerColors.getSelectedItem().toString();
                // Convert the selected color string to its corresponding color resource ID 
                //int colorResourceId = getColorResourceId(selectedColor);

               // Question newQuestion = new Question(questionText,isTrue,selectedColor);
            }
        });

        builder.setNegativeButton(string.ignore, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}