package xyz.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 类型实体
 */
@Data
@TableName("type")
public class Type {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private String name;
    
    private String description;
} 