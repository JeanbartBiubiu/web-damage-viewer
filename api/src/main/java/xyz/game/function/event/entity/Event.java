package xyz.game.function.event.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Event implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Integer id;
    private String name;
    private String express;

    private List<Integer> types;
}