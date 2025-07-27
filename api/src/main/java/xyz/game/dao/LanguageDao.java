package xyz.game.dao;

import xyz.game.entity.Language;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (Language)表数据库访问层
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public interface LanguageDao {

    /**
     * 通过ID查询单条数据
     *
     * @param languageId 主键
     * @return 实例对象
     */
    Language queryById(Integer languageId);

    /**
     * 查询指定行数据
     *
     * @param language 查询条件
     * @return 对象列表
     */
    List<Language> query(Language language);

    /**
     * 统计总行数
     *
     * @param language 查询条件
     * @return 总行数
     */
    long count(Language language);

    /**
     * 新增数据
     *
     * @param language 实例对象
     * @return 影响行数
     */
    int insert(Language language);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Language> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Language> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Language> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Language> entities);

    /**
     * 修改数据
     *
     * @param language 实例对象
     * @return 影响行数
     */
    int update(Language language);

    /**
     * 通过主键删除数据
     *
     * @param languageId 主键
     * @return 影响行数
     */
    int deleteById(Integer languageId);

}

