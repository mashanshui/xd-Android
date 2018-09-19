package cn.edu.ahnu.wjcy.myapplication.Model;

import java.util.List;

/**
 * Created by Cuibiming on 2017-09-21.
 */

public class ParseResult {
    private List<ParseResultItem> itemList;
    private int sc;

    public List<ParseResultItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<ParseResultItem> itemList) {
        this.itemList = itemList;
    }

    public int getSc() {
        return sc;
    }

    public void setSc(int sc) {
        this.sc = sc;
    }

    public ParseResult() {
    }

    public ParseResult(List<ParseResultItem> itemList, int sc) {
        this.itemList = itemList;
        this.sc = sc;
    }
}
