package xyz.game.function.event.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("event_type")
public class EventType implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer type;
}