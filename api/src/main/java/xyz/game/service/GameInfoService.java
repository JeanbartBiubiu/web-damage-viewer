package xyz.game.service;

import xyz.game.entity.GameInfo;

import java.util.List;

/**
 * (GameInfo)表服务接口
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public interface GameInfoService {

    /**
     * 通过ID查询单条数据
     *
     * @param gameId 主键
     * @return 实例对象
     */
    GameInfo queryById(Integer gameId);

    /**
     * 分页查询
     *
     * @return 查询结果
     */
    List<GameInfo> query();

    /**
     * 新增数据
     *
     * @param gameInfo 实例对象
     * @return 实例对象
     */
    GameInfo insert(GameInfo gameInfo);

    /**
     * 修改数据
     *
     * @param gameInfo 实例对象
     * @return 实例对象
     */
    GameInfo update(GameInfo gameInfo);

    /**
     * 通过主键删除数据
     *
     * @param gameId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer gameId);

}
