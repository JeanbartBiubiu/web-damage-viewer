package xyz.game.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("pv_uv")
public class PvUv implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long hour;
    private Long pv;
    private Long uv;
}