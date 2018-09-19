package cn.edu.ahnu.wjcy.myapplication.Model;

/**
 * Created by Cuibiming on 2017-09-21.
 */

public class ParseResultItem {
    private String text;
    private int id;
    private int sc;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSc() {
        return sc;
    }

    public void setSc(int sc) {
        this.sc = sc;
    }

    public ParseResultItem() {
    }

    public ParseResultItem(String text, int id, int sc) {
        this.text = text;
        this.id = id;
        this.sc = sc;
    }
}
