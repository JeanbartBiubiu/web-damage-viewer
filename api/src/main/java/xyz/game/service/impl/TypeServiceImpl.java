package xyz.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import xyz.game.dao.TypeMapper;
import xyz.game.dao.TypeRelationMapper;
import xyz.game.entity.Type;
import xyz.game.entity.TypeDTO;
import xyz.game.entity.TypeRelation;
import xyz.game.service.TypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 类型服务实现
 */
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements TypeService {

    private static final Logger log = LoggerFactory.getLogger(TypeServiceImpl.class);

    @Autowired
    private TypeRelationMapper typeRelationMapper;

    @Override
    public List<Type> Types(String keyword) {
        LambdaQueryWrapper<Type> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like(Type::getName, keyword)
                    .or()
                    .like(Type::getDescription, keyword);
        }
        return this.list(queryWrapper);
    }

    @Override
    public Type getTypeById(Long id) {
        return this.getById(id);
    }

    @Override
    public boolean createType(Type type) {
        return this.save(type);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createTypeWithRelations(TypeDTO typeDTO) {
        Type type = typeDTO.getType();
        
        // 保存类型信息
        boolean saveResult = this.save(type);
        if (!saveResult) {
            return false;
        }
        
        // 确保能获取到ID（MyBatis-Plus应该已自动回填ID）
        Long typeId = type.getId();
        if (typeId == null) {
            // 通过名称查询获取ID（保证唯一性）
            LambdaQueryWrapper<Type> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Type::getName, type.getName());
            Type savedType = this.getOne(queryWrapper);
            if (savedType != null) {
                typeId = savedType.getId();
            } else {
                // 无法获取ID，无法建立关系
                return true;
            }
        }
        
        // 保存类型关系
        List<Long> parentTypeIds = typeDTO.getParentTypeIds();
        return saveTypeRelations(typeId, parentTypeIds);
    }

    @Override
    public boolean updateType(Type type) {
        return this.updateById(type);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTypeWithRelations(TypeDTO typeDTO) {
        // 更新类型信息
        boolean updateResult = this.updateById(typeDTO.getType());
        if (!updateResult) {
            return false;
        }
        
        // 更新类型关系：先删除所有关系，再重新建立关系
        Long typeId = typeDTO.getType().getId();
        LambdaQueryWrapper<TypeRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TypeRelation::getTypeId, typeId);
        typeRelationMapper.delete(wrapper);
        
        // 保存新的类型关系
        return saveTypeRelations(typeId, typeDTO.getParentTypeIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteType(Long id) {
        // 删除关联关系
        LambdaQueryWrapper<TypeRelation> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(TypeRelation::getTypeId, id);
        typeRelationMapper.delete(wrapper1);

        LambdaQueryWrapper<TypeRelation> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(TypeRelation::getParentTypeId, id);
        typeRelationMapper.delete(wrapper2);

        // 删除类型
        return this.removeById(id);
    }
    
    /**
     * 保存类型关系
     */
    private boolean saveTypeRelations(Long typeId, List<Long> parentTypeIds) {
        // 检查typeId是否有效
        if (typeId == null) {
            return false;
        }
        
        // 如果父类型ID列表为空，直接返回成功
        if (CollectionUtils.isEmpty(parentTypeIds)) {
            return true;
        }
        
        List<TypeRelation> relations = new ArrayList<>();
        for (Long parentId : parentTypeIds) {
            // 跳过无效ID或自引用
            if (parentId == null || typeId.equals(parentId)) {
                continue;
            }
            
            TypeRelation relation = new TypeRelation();
            relation.setTypeId(typeId);
            relation.setParentTypeId(parentId);
            relations.add(relation);
        }
        
        if (relations.isEmpty()) {
            return true;
        }
        
        // 批量插入关系
        for (TypeRelation relation : relations) {
            try {
                // 先检查是否已存在相同关系
                LambdaQueryWrapper<TypeRelation> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(TypeRelation::getTypeId, relation.getTypeId())
                        .eq(TypeRelation::getParentTypeId, relation.getParentTypeId());
                if (typeRelationMapper.selectCount(queryWrapper) == 0) {
                    typeRelationMapper.insert(relation);
                }
            } catch (Exception e) {
                // 忽略数据库约束错误，保证幂等性
                log.error("保存类型关系失败: typeId={}, parentTypeId={}, 错误: {}", 
                        relation.getTypeId(), relation.getParentTypeId(), e.getMessage());
            }
        }
        
        return true;
    }

    @Override
    public List<Type> getParentTypes(Long typeId) {
        return typeRelationMapper.findParentTypes(typeId);
    }

    @Override
    public List<Type> getChildTypes(Long parentTypeId) {
        return typeRelationMapper.findChildTypes(parentTypeId);
    }

    @Override
    public boolean addTypeRelation(Long typeId, Long parentTypeId) {
        // 不允许自我关联
        if (typeId.equals(parentTypeId)) {
            return false;
        }
        
        // 先检查是否已存在
        LambdaQueryWrapper<TypeRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TypeRelation::getTypeId, typeId)
                .eq(TypeRelation::getParentTypeId, parentTypeId);
        if (typeRelationMapper.selectCount(queryWrapper) > 0) {
            return true; // 关系已存在
        }
        
        TypeRelation relation = new TypeRelation();
        relation.setTypeId(typeId);
        relation.setParentTypeId(parentTypeId);
        return typeRelationMapper.insert(relation) > 0;
    }

    @Override
    public boolean removeTypeRelation(Long typeId, Long parentTypeId) {
        LambdaQueryWrapper<TypeRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TypeRelation::getTypeId, typeId)
                .eq(TypeRelation::getParentTypeId, parentTypeId);
        return typeRelationMapper.delete(queryWrapper) > 0;
    }
} 