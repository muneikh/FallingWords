package com.muneikh.fallingword.manager;

import android.content.Context;
import android.util.Log;

import com.muneikh.fallingword.R;
import com.muneikh.fallingword.Utils;
import com.muneikh.fallingword.model.Translation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import rx.Observable;

public class TranslationManager {

    private static final String TAG = "TranslationManager";

    private ArrayList<Translation> translations;
    private int currentTranslationIndex = 0;
    private Translation currentTranslation;
    private Random random;

    private int rightCount = 0;
    private int wrongCount = 0;
    private int skippedCount = 0;

    public TranslationManager() {
        random = new Random();
    }

    public Observable<ArrayList<Translation>> getTranslations(Context context) {
        return Observable.defer(() -> Observable.just(readTranslationsFromFile(context)));
    }

    private ArrayList<Translation> readTranslationsFromFile(Context context) {
        try {
            String json = Utils.readFileFromRes(context, R.raw.words);
            return Translation.fromJson(json);
        } catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage());
            throw new RuntimeException("Unable to read the translations.json: " + e);
        }
    }

    public void setTranslations(ArrayList<Translation> translations) {
        this.translations = translations;

        // every time user starts a new session that translation list is shuffled. So, user don't
        // see the same sequence of words again and again. Just to provide a better user experience.
        Collections.shuffle(translations);
    }

    public Translation getNextTranslation() {
        Translation translation = null;

        if (translations != null) {
            int size = translations.size();

            if (currentTranslationIndex < size) {

                // return the correct translation
                if (random.nextBoolean()) {
                    translation = translations.get(currentTranslationIndex);

                } else {
                    int incorrectTranslationIndex = random.nextInt(size - currentTranslationIndex)
                            + currentTranslationIndex;

                    // corner case if the random index decides to take the current element
                    if (incorrectTranslationIndex == currentTranslationIndex) {
                        translation = translations.get(currentTranslationIndex);
                    } else {
                        // forge the correct translation into incorrect
                        translation = translations.get(currentTranslationIndex);
                        Translation sample = translations.get(incorrectTranslationIndex);
                        translation.setTextEng(sample.getTextEng());
                        translation.setIncorrectTranslation(true);
                    }

                }

                currentTranslationIndex++;
            }
        }

        this.currentTranslation = translation;
        return translation;
    }

    public void checkUserSelection(boolean isSkipped) {
        checkUserSelection(isSkipped, false);
    }

    public void checkUserSelection(boolean isSkipped, boolean userSelection) {

        // create deep copy to avoid dirty read/write.
        Translation translation = Translation.create(currentTranslation.getTextEng(),
                currentTranslation.getTextSpa(), currentTranslation.isIncorrectTranslation());

        // no user selection -- skipped case.
        if (isSkipped) {
            skippedCount += 1;
        } else {
            if (userSelection == !translation.isIncorrectTranslation()) {
                rightCount += 1;
            } else {
                wrongCount += 1;
            }
        }
    }

    public int getRightCount() {
        return rightCount;
    }

    public int getWrongCount() {
        return wrongCount;
    }

    public int getSkippedCount() {
        return skippedCount;
    }

    public void reset() {
        rightCount = 0;
        wrongCount = 0;
        skippedCount = 0;
        currentTranslationIndex = 0;
    }
}
