package xyz.game.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 类型数据传输对象，包含类型信息和父类型ID列表
 */
@Data
public class TypeDTO {
    
    /**
     * 类型信息
     */
    private Type type = new Type();
    
    /**
     * 父类型ID列表
     */
    private List<Long> parentTypeIds = new ArrayList<>();
} 