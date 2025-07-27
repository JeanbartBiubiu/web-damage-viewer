package xyz.game.service;

import xyz.game.entity.IndivName;

import java.util.List;

/**
 * (IndivName)表服务接口
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public interface IndivNameService {

    /**
     * 通过ID查询单条数据
     *
     * @param indivId 主键
     * @return 实例对象
     */
    IndivName queryById(Integer indivId);

    /**
     * 分页查询
     *
     * @param indivName 筛选条件
     * @return 查询结果
     */
    List<IndivName> query(IndivName indivName);

    /**
     * 新增数据
     *
     * @param indivName 实例对象
     * @return 实例对象
     */
    IndivName insert(IndivName indivName);

    /**
     * 修改数据
     *
     * @param indivName 实例对象
     * @return 实例对象
     */
    IndivName update(IndivName indivName);

    /**
     * 通过主键删除数据
     *
     * @param indivId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer indivId);

}
