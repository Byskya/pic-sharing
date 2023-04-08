package com.example.picsharingspringboot.util;

import lombok.Data;

import java.io.Serializable;
@Data
public class ResponseResult<T> implements Serializable {
    private static final long serialVersionUID = -1364948307051412747L;
    private int state;
    private String message;
    private T data;
}
