package com.example.quizapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class QuestionFragment extends Fragment {
    TextView questionTextView;
    public static QuestionFragment newInstance(String questionText, int questionColor){
        QuestionFragment ff = new QuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("questionText", questionText); // Pass the question text as an argument
        bundle.putInt("questionColor", questionColor);
        ff.setArguments(bundle);

        return ff;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
         View view = inflater.inflate(R.layout.fragment_quesiton,container,false);
        questionTextView = view.findViewById(R.id.questionTextView);

        // Retrieve the passed argument
        Bundle args = getArguments();
        if (args != null) {
            String questionText = args.getString("questionText");
            int questionColor = args.getInt("questionColor");
            if (questionText != null && questionTextView != null) {
                questionTextView.setText(questionText);
                questionTextView.setBackgroundColor(questionColor);
            }
        }
        return view;
    }
}
