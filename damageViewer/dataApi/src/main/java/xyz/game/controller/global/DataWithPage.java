package xyz.game.controller.global;

import lombok.Data;

import java.util.List;

@Data
public class DataWithPage<T> {
    private int total;
    private List<T> list;
}
