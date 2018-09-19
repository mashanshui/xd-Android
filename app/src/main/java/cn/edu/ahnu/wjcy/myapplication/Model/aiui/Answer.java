package cn.edu.ahnu.wjcy.myapplication.Model.aiui;

/**
 * Created by Cuibiming on 2017-08-21.
 * OpenQA回答
 */

public class Answer {
    private String answerType;
    private String emotion;
    private Question question;
    private String text;
    private String topic;
    private String topicID;
    private String type;

    public String getAnswerType() {
        return answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTopicID() {
        return topicID;
    }

    public void setTopicID(String topicID) {
        this.topicID = topicID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    class Question {
        private String question;
        private String question_ws;

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getQuestion_ws() {
            return question_ws;
        }

        public void setQuestion_ws(String question_ws) {
            this.question_ws = question_ws;
        }
    }
}
