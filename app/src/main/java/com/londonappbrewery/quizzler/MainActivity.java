package com.londonappbrewery.quizzler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static com.londonappbrewery.quizzler.R.color.color1;

public class MainActivity extends Activity {
    private TextView countLabel;
    ProgressBar mProgressBar;
    private TextView questionLabel;
    private TextView quizTimer;
    private Button answerButton1;
    private Button answerButton2;
    private Button answerButton3;
    private Button answerButton4;
    private CountDownTimer mCountDownTimer;
    private String rightAnswer;
    private int rightAnswerCount = 0;
    private int quizCount = 1;
    private TextView textView;

    static final private int QUIZ_COUNT = 10;
    static final long START_TIME_IN_MILIS = 60000;
    private long mTimeLeftinMillis = START_TIME_IN_MILIS;
    int PROGRESS_BAR_INCREMENT = 100 / QUIZ_COUNT;
    private String corect = "Corect";
    private String wrong = "Wrong...";

    private Activity mActivity;

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftinMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftinMillis = millisUntilFinished;
                quizTimer.setText("Time: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                quizTimer.setText("DONE!");
            }
        }.start();

    }

    ArrayList<ArrayList<String>> quizArray = new ArrayList<>();
    String quizData[][] = {

            //{"Question", "right answer", "choicea", "choiceb", "choicec", "choiced"}
            {"Snow color?", "White", "Green", "Blue", "yellow"},
            {"Coal color?", "Black", "White", "Blue", "Orange"},
            {"Donald Trump's color?", "Orange", "Black", "Green", "Blue"},
            {"Course number?", "cs3300", "cs4300", "cs1400", "cs4770"},
            {"Assignment number?", "assign3", "assign2", "assign69", "assign5"},
            {"Canada's capital city?", "Ottawa", "Toronto", "St.Johns", "Montreal"},
            {"The sound dog makes?", "woof?", "meow", "be be", "moo"},
            {"Number of planets in solar system?", "9", "8", "7", "11"},
            {"Biggest planet in solar system?", "Jupiter", "Neptune", "Saturn", "Uranus"},
            {"Biggest planet in solar system?", "Jupiter", "Neptune", "Saturn", "Uranus"}
    };

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countLabel = (TextView) findViewById(R.id.countLabel);
        questionLabel = (TextView) findViewById(R.id.question_text_view);
        quizTimer = (TextView) findViewById(R.id.timer);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        answerButton1 = (Button) findViewById(R.id.answer1);
        answerButton2 = (Button) findViewById(R.id.answer2);
        answerButton3 = (Button) findViewById(R.id.answer3);
        answerButton4 = (Button) findViewById(R.id.answer4);
        mActivity = MainActivity.this;

        startTimer();

//create quizArray from quizData
        for (int i = 0; i < quizData.length; i++) {

            //Prepare array
            ArrayList<String> tmpArray = new ArrayList<>();
            tmpArray.add(quizData[i][0]); //Country
            tmpArray.add(quizData[i][1]); //Right Answer
            tmpArray.add(quizData[i][2]); //Choice1
            tmpArray.add(quizData[i][3]); //Choice2
            tmpArray.add(quizData[i][4]); //Choice3

            //Add tmpArray to quizArray
            quizArray.add(tmpArray);
        }
        showNextQuiz();
    }


    public void showNextQuiz() {

        //Update quizCountLabel
        countLabel.setText("Вопрос" + quizCount);
        //Generate random number between 0 and 14(quizArray's size -1).
        Random random = new Random();
        int randomNum = random.nextInt(quizArray.size());

        //Pick one quiz set.
        ArrayList<String> quiz = quizArray.get(randomNum);

        //set quetion and right answer.
        //Array format as above;
        questionLabel.setText(quiz.get(0));
        rightAnswer = quiz.get(1);

        //remove "Question" from quiz and Shuffle choices.
        quiz.remove(0);
        Collections.shuffle(quiz);

        //set choices
        answerButton1.setText(quiz.get(0));
        answerButton2.setText(quiz.get(1));
        answerButton3.setText(quiz.get(2));
        answerButton4.setText(quiz.get(3));

        quizArray.remove(randomNum);
    }

    public void checkAnswer(View view) {

        //Get pushed button
        Button answerBtn = (Button) findViewById(view.getId());
        String btnText = answerBtn.getText().toString();

        final String alertTitle;

        if (btnText.equals(rightAnswer)) {
            //correct!
            alertTitle = corect;
            rightAnswerCount++;
        } else {
            //wrong
            alertTitle = wrong;
        }

//alert handler
        ContextThemeWrapper cw = new ContextThemeWrapper(this, R.style.AlertDialogTheme);
        AlertDialog.Builder builder = new AlertDialog.Builder(cw);
        builder.setTitle(alertTitle);

            if (alertTitle == corect) {
                LayoutInflater factory = LayoutInflater.from(MainActivity.this);
                final View view2 = factory.inflate(R.layout.sample, null);
                builder.setView(view2);
            } else {
                LayoutInflater factory = LayoutInflater.from(MainActivity.this);
                final View view1 = factory.inflate(R.layout.smale_fols, null);
                builder.setView(view1);
            }
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                //function handles the button click
                public void onClick(DialogInterface dialogInterface, int i) {


                    //if all question 5 has been reached, launch the RESULTS page.
                    if (quizCount == QUIZ_COUNT) {
                        //show Result
                        Intent intent = new Intent(getApplicationContext(), resultActivity.class);
                        mProgressBar.setProgress(0);
                        intent.putExtra("RIGHT_ANSWER_COUNT", rightAnswerCount);
                        startActivity(intent);
                    } else {
                        quizCount++;
                        mProgressBar.incrementProgressBy(PROGRESS_BAR_INCREMENT);
                        showNextQuiz();
                    }
                }
            });
            AlertDialog dialog1 = builder.create();
            dialog1.show();

            if (alertTitle == corect) {
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GREEN));
            } else {
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.CYAN));
            }
        }
    }


