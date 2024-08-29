package com.zzy.result;

public enum Code {
    ERROR(404),OK(200);
    private int codeNum;
    private Code(int codeNum){
        this.codeNum = codeNum;
    }
}
