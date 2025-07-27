package xyz.game.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xyz.game.entity.Type;
import xyz.game.entity.TypeRelation;

import java.util.List;

/**
 * 类型关系Mapper
 */
@Mapper
public interface TypeRelationMapper extends BaseMapper<TypeRelation> {
    
    /**
     * 查询类型的所有父类型
     * @param typeId 类型ID
     * @return 父类型列表
     */
    @Select("SELECT t.* FROM type t " +
            "JOIN type_relation tr ON t.id = tr.parent_type_id " +
            "WHERE tr.type_id = #{typeId}")
    List<Type> findParentTypes(@Param("typeId") Long typeId);
    
    /**
     * 查询类型的所有子类型
     * @param parentTypeId 父类型ID
     * @return 子类型列表
     */
    @Select("SELECT t.* FROM type t " +
            "JOIN type_relation tr ON t.id = tr.type_id " +
            "WHERE tr.parent_type_id = #{parentTypeId}")
    List<Type> findChildTypes(@Param("parentTypeId") Long parentTypeId);
} 