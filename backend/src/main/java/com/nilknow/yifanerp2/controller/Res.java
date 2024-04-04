package com.nilknow.yifanerp2.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Res<T> {
    private String successCode;
    private Integer statusCode;
    private String msg;
    private T body;

    public Res<T> success(T body) {
        return new Res<>("success", 200, null, body);
    }

    public Res<T> fail(String msg) {
        return new Res<>("fail", 200, msg, null);
    }

}
