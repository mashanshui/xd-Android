package cn.edu.ahnu.wjcy.myapplication.Model.aiui;

/**
 * Created by Cuibiming on 2017-08-21.
 * 开放问答
 */

public class OpenQAAIUI extends BaseAIUI {
    private Answer answer;
    private String man_intv;
    private String no_nlu_result;
    private String operation;
    private String status;

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public String getMan_intv() {
        return man_intv;
    }

    public void setMan_intv(String man_intv) {
        this.man_intv = man_intv;
    }

    public String getNo_nlu_result() {
        return no_nlu_result;
    }

    public void setNo_nlu_result(String no_nlu_result) {
        this.no_nlu_result = no_nlu_result;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
