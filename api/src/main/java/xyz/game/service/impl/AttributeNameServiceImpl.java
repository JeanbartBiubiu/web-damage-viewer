package xyz.game.service.impl;

import xyz.game.entity.AttributeName;
import xyz.game.dao.AttributeNameDao;
import xyz.game.service.AttributeNameService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

/**
 * 属性名称表(AttributeName)表服务实现类
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@Service("attributeNameService")
public class AttributeNameServiceImpl implements AttributeNameService {
    @Resource
    private AttributeNameDao attributeNameDao;

    /**
     * 通过ID查询单条数据
     *
     * @param attributeId 主键
     * @return 实例对象
     */
    @Override
    public AttributeName queryById(Integer attributeId) {
        return this.attributeNameDao.queryById(attributeId);
    }

    /**
     * 分页查询
     *
     * @param attributeName 筛选条件
     * @return 查询结果
     */
    @Override
    public List<AttributeName> query(AttributeName attributeName) {
        return this.attributeNameDao.query(attributeName);
    }

    /**
     * 新增数据
     *
     * @param attributeName 实例对象
     * @return 实例对象
     */
    @Override
    public AttributeName insert(AttributeName attributeName) {
        this.attributeNameDao.insert(attributeName);
        return attributeName;
    }

    /**
     * 修改数据
     *
     * @param attributeName 实例对象
     * @return 实例对象
     */
    @Override
    public AttributeName update(AttributeName attributeName) {
        this.attributeNameDao.update(attributeName);
        return this.queryById(attributeName.getAttributeId());
    }

    /**
     * 通过主键删除数据
     *
     * @param attributeId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer attributeId) {
        return this.attributeNameDao.deleteById(attributeId) > 0;
    }
}
