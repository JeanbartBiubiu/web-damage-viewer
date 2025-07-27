package xyz.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 类型关系实体
 */
@Data
@TableName("type_relation")
public class TypeRelation {
    
    private Long typeId;
    
    private Long parentTypeId;
} 