package com.muneikh.fallingword.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.muneikh.fallingword.R;
import com.muneikh.fallingword.manager.TranslationManager;
import com.muneikh.fallingword.model.Translation;
import com.muneikh.fallingword.ui.widget.FallingTextView;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WordFallActivity extends AppCompatActivity implements Animation.AnimationListener {

    private static final String TAG = "WordFallActivity";

    private FallingTextView fallingTextView;
    private TextView textViewWord;

    private TextView textViewWrong;
    private TextView textViewRight;
    private TextView textViewSkipped;

    private boolean isSkipped;
    private boolean alreadyProcessed;

    private TranslationManager translationManager = new TranslationManager();
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_fall);

        textViewWord = (TextView) findViewById(R.id.text_view_word);
        textViewRight = (TextView) findViewById(R.id.text_view_right);
        textViewWrong = (TextView) findViewById(R.id.text_view_wrong);
        textViewSkipped = (TextView) findViewById(R.id.text_view_skipped);
        fallingTextView = (FallingTextView) findViewById(R.id.text_view_falling_word);

        fallingTextView.setAnimationListener(this);

        Observable<ArrayList<Translation>> observable = translationManager.getTranslations(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());

        subscription = observable.subscribe(translations -> {
            translationManager.setTranslations(translations);
            displayNextTranslation(translationManager.getNextTranslation());
        });
    }

    public void rightButtonPressed(View view) {
        if (!alreadyProcessed) {
            alreadyProcessed = true;
            isSkipped = false;
            translationManager.checkUserSelection(isSkipped, true);
            refreshStatistics();
        }
    }

    public void wrongButtonPressed(View view) {
        if (!alreadyProcessed) {
            alreadyProcessed = true;
            isSkipped = false;
            translationManager.checkUserSelection(isSkipped, false);
            refreshStatistics();
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {
        Log.d(TAG, "onAnimationStart: ");
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Log.d(TAG, "onAnimationEnd: ");

        if (!alreadyProcessed && isSkipped) {
            alreadyProcessed = true;
            translationManager.checkUserSelection(isSkipped);
            isSkipped = false;
            refreshStatistics();
        }

        displayNextTranslation(translationManager.getNextTranslation());
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        Log.d(TAG, "onAnimationRepeat: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        subscription.unsubscribe();
    }

    private void displayNextTranslation(Translation translation) {
        if (translation != null) {
            textViewWord.setText(translation.getTextSpa());
            fallingTextView.setText(translation.getTextEng());
            fallingTextView.startAnimation();
            alreadyProcessed = false;
            isSkipped = true;

        } else {

            textViewWord.setText("");
            fallingTextView.setText("");

            View view = findViewById(android.R.id.content).getRootView();
            Snackbar snackbar = Snackbar.make(view, "Game Over", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Retry", v -> restartGame());
            snackbar.show();
        }
    }

    private void restartGame() {
        translationManager.reset();

        textViewRight.setText("0");
        textViewWrong.setText("0");
        textViewSkipped.setText("0");

        displayNextTranslation(translationManager.getNextTranslation());
    }

    private void refreshStatistics() {
        textViewRight.setText(String.valueOf(translationManager.getRightCount()));
        textViewWrong.setText(String.valueOf(translationManager.getWrongCount()));
        textViewSkipped.setText(String.valueOf(translationManager.getSkippedCount()));
    }
}
