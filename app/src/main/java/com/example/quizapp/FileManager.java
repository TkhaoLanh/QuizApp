package com.example.quizapp;

import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.io.InputStreamReader;


public class FileManager {
    String fileName = "QuizFile .txt";

    //save to file
    void writeQuizFile(Context context, int score) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_APPEND);
            fos.write((String.valueOf(score) + "\n").getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //reset the file
    void deleteScore(Context context){
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write("".getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Calculate total correct questions from the stored scores in the file
    public int calculateAverageScore(Context context) {

        ArrayList<Integer> scores = readScoresFromFile(context);
        int total = 0;
        if (scores.size() > 0) {

            for (int score : scores) {
                total += score;
            }
        }
        return total;
    }

    //read score from file
    private ArrayList<Integer> readScoresFromFile(Context context) {
        ArrayList<Integer> scores = new ArrayList<>();
        try {
            FileInputStream fis = context.openFileInput(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            String line;
            while ((line = reader.readLine()) != null) {
                scores.add(Integer.parseInt(line.trim()));
            }

            reader.close();
            fis.close();
        } catch (FileNotFoundException e) {
            Log.e("FileManager", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.e("FileManager", "Error reading file: " + e.getMessage());
        }
        return scores;
    }
    // Get the number of attempts saved in the file
    public int getNumberOfAttempts(Context context) {
        int attempts = 0;
        try {
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            // Read each line in the file, each line represents an attempt
            while (bufferedReader.readLine() != null) {
                attempts++;
            }

            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return attempts;
    }

}
