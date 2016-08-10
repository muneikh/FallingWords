package com.muneikh.fallingword.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class Translation {

    @SerializedName("text_eng")
    private String textEng;

    @SerializedName("text_spa")
    private String textSpa;

    private boolean isIncorrectTranslation;

    public String getTextEng() {
        return textEng;
    }

    public String getTextSpa() {
        return textSpa;
    }

    public boolean isIncorrectTranslation() {
        return isIncorrectTranslation;
    }

    public void setTextEng(String textEng) {
        this.textEng = textEng;
    }

    public void setTextSpa(String textSpa) {
        this.textSpa = textSpa;
    }

    public void setIncorrectTranslation(boolean incorrectTranslation) {
        this.isIncorrectTranslation = incorrectTranslation;
    }

    public static ArrayList<Translation> fromJson(String json) {
        return new Gson().fromJson(json, new TypeToken<ArrayList<Translation>>() {
        }.getType());
    }

    @Override
    public String toString() {
        return "Translation{" +
                "textEng='" + textEng + '\'' +
                ", textSpa='" + textSpa + '\'' +
                ", isIncorrectTranslation=" + isIncorrectTranslation +
                '}';
    }

    // Used for creating a deep copy of the object
    public static Translation create(String textEng, String textSpa, boolean isIncorrectTranslation) {
        Translation translation = new Translation();
        translation.textEng = textEng;
        translation.textSpa = textSpa;
        translation.isIncorrectTranslation = isIncorrectTranslation;
        return translation;
    }
}