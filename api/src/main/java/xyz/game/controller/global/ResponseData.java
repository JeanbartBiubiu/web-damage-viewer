package xyz.game.controller.global;

import lombok.Data;

import java.util.List;

@Data
public class ResponseData <T>{
    private int code = 0;
    private T data;
    private String message = "";
}
