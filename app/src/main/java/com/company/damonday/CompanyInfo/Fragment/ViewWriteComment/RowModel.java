package com.company.damonday.CompanyInfo.Fragment.ViewWriteComment;

/**
 * Created by lamtaklung on 29/11/2015.
 */
public class RowModel {
    String label;           //存储entry的当前文本显示内容，通过调用toString()给出，如果三星将提供大写显示。
    int range = 0;

    RowModel(String label){
        this.label = label;
    }
    public String getLabel(){
        return label;
    }

    public int getRange(){
        return range + 1;
    }
}
