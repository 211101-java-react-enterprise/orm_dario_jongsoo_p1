package com.revature.banking.orm.models;

import java.util.Objects;

public class Flashcard {

    private String id;
    private String questionText;
    private String answerText;

    public Flashcard(String questionText, String answerText) {
        this.questionText = questionText;
        this.answerText = answerText;
    }

    public Flashcard() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, questionText, answerText);
    }

    @Override
    public String toString() {
        return "Flashcard{" +
                "id='" + id + '\'' +
                ", questionText='" + questionText + '\'' +
                ", answerText='" + answerText + '\'' +
                '}';
    }

}
