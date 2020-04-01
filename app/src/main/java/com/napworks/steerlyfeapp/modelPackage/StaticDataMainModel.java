package com.napworks.steerlyfeapp.modelPackage;

import java.util.ArrayList;
import java.util.List;

public class StaticDataMainModel {

    private String status;
    private String message;
    private List<CountryModel> countriesList = new ArrayList<>();
    private List<CategoryModel> categoriesList = new ArrayList<>();
    private List<QuizQuestionsModel> quizQuestions = new ArrayList<>();


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CountryModel> getCountriesList() {
        return countriesList;
    }

    public void setCountriesList(List<CountryModel> countriesList) {
        this.countriesList = countriesList;
    }

    public List<CategoryModel> getCategoriesList() {
        return categoriesList;
    }

    public void setCategoriesList(List<CategoryModel> categoriesList) {
        this.categoriesList = categoriesList;
    }

    public List<QuizQuestionsModel> getQuizQuestions() {
        return quizQuestions;
    }

    public void setQuizQuestions(List<QuizQuestionsModel> quizQuestions) {
        this.quizQuestions = quizQuestions;
    }
}
