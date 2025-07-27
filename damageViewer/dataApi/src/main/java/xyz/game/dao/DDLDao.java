package xyz.game.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import xyz.game.entity.Attribute;

import java.util.List;

/**
 * 属性表(Attribute)表数据库访问层
 *
 * @author makejava
 * @since 2024-06-15 19:17:12
 */
@Mapper
public interface DDLDao {

    void createDb(String gameCode);
    void createTableAttribute(String gameCode);
    void createTableAttributeName(String gameCode);
    void createTableAttributeValue(String gameCode);
    void createTableComputingLifeCycle(String gameCode);
    void createTableEquipId(String gameCode);
    void createTableEquipment(String gameCode);
    void createTableEquipmentName(String gameCode);
    void createTableFormulaDefault(String gameCode);
    void createTableFormulaImpl(String gameCode);
    void createTableIndividual(String gameCode);
    void createTableIndivName(String gameCode);
    void createTableIndivSkill(String gameCode);
    void createTableLanguage(String gameCode);
    void createTableLevel(String gameCode);
    void createTableSkillDef(String gameCode);
    void createTableSkillSub(String gameCode);
    void createTableVersion(String gameCode);


}

