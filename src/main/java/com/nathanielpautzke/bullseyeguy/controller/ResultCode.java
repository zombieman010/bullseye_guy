package com.nathanielpautzke.bullseyeguy.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultCode {
    private String code;
    private String message;
}
