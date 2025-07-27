package xyz.game.service.impl;

import org.springframework.transaction.annotation.Transactional;
import xyz.game.entity.AttributeValue;
import xyz.game.dao.AttributeValueDao;
import xyz.game.service.AttributeValueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

/**
 * 单位-属性-等级对应数据数值(AttributeValue)表服务实现类
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@Service("attributeValueService")
public class AttributeValueServiceImpl implements AttributeValueService {
    @Resource
    private AttributeValueDao attributeValueDao;

    /**
     * 通过ID查询单条数据
     *
     * @param  主键
     * @return 实例对象
     */
    @Override
    public List<AttributeValue> queryById(int indivId) {
        return this.attributeValueDao.queryById(indivId);
    }

    @Transactional
    @Override
    public boolean settingValues(List<AttributeValue> list) {
        if (list == null || list.isEmpty()){
            return false;
        }
        int id = list.get(0).getIndivId();
        this.attributeValueDao.deleteByIndivId(id);
        this.attributeValueDao.insertBatch(list);
        return true;
    }

    /**
     * 分页查询
     *
     * @param attributeValue 筛选条件
     * @return 查询结果
     */
    @Override
    public List<AttributeValue> query(AttributeValue attributeValue) {
        return this.attributeValueDao.query(attributeValue);
    }

    /**
     * 新增数据
     *
     * @param attributeValue 实例对象
     * @return 实例对象
     */
    @Override
    public AttributeValue insert(AttributeValue attributeValue) {
        this.attributeValueDao.insert(attributeValue);
        return attributeValue;
    }

    /**
     * 修改数据
     *
     * @param attributeValue 实例对象
     * @return 实例对象
     */
    @Override
    public AttributeValue update(AttributeValue attributeValue) {
        this.attributeValueDao.update(attributeValue);
        return this.queryById(0).get(0);
    }

    /**
     * 通过主键删除数据
     *
     * @param  主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById( ) {
        return this.attributeValueDao.deleteById() > 0;
    }
}
