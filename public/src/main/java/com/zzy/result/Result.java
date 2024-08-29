package com.zzy.result;

import lombok.Data;

@Data
public class Result<T> {
    private Code code;
    private String message;
    private T data;

    public static <T> Result<T> ok(T data){
        Result<T> result = new Result<>();
        result.setCode(Code.OK);
        result.setMessage("ok");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error(String message){
        Result<T> result = new Result<>();
        result.setCode(Code.ERROR);
        result.setMessage(message);
//        result.setData(null);
        return result;
    }
}
