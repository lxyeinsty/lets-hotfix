package com.github.lzy.hotfix.model;

import lombok.Data;

/**
 * @author liuzhengyang
 */
@Data
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setData(data);
        return result;
    }
}
