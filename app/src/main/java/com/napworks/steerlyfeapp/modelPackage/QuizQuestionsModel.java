package com.napworks.steerlyfeapp.modelPackage;

import java.util.ArrayList;
import java.util.List;

public class QuizQuestionsModel {

    private String question_id;
    private String question_public_id;
    private String question;
    private List<String> options_list = new ArrayList<>();


    public QuizQuestionsModel(String question_id, String question) {
        this.question_id = question_id;
        this.question = question;
    }

//    public QuizQuestionsModel(String question_id, String question_public_id, String question, List<String> options_list) {
//        this.question_id = question_id;
//        this.question_public_id = question_public_id;
//        this.question = question;
//        this.options_list = options_list;
//    }


    public QuizQuestionsModel(String question_id, String question_public_id, String question) {
        this.question_id = question_id;
        this.question_public_id = question_public_id;
        this.question = question;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getQuestion_public_id() {
        return question_public_id;
    }

    public void setQuestion_public_id(String question_public_id) {
        this.question_public_id = question_public_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptions_list() {
        return options_list;
    }

    public void setOptions_list(List<String> options_list) {
        this.options_list = options_list;
    }
}
