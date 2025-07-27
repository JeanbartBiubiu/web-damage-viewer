package xyz.game.function.event.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.game.dao.event.EventDefMapper;
import xyz.game.dao.event.EventTypeMapper;
import xyz.game.function.event.entity.Event;
import xyz.game.function.event.entity.EventDef;
import xyz.game.function.event.entity.EventType;
import xyz.game.function.event.service.EventDefService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventDefServiceImpl extends ServiceImpl<EventDefMapper, EventDef> implements EventDefService {
    private final EventTypeMapper eventTypeMapper;

    public EventDefServiceImpl(EventTypeMapper eventTypeMapper) {
        this.eventTypeMapper = eventTypeMapper;
    }

    @Override
    public List<Event> getEventList() {
        List<EventDef> eventDefs = getBaseMapper().selectList(new QueryWrapper<EventDef>());
        List<EventType> eventTypes = eventTypeMapper.selectList(new QueryWrapper<EventType>());
        // types 是一个 List<Integer>，其中的 Integer 是 EventType 的 id
        Map<Integer, List<Integer>> typeMapping = getIntegerListMap(eventTypes);
        List<Event> events = new ArrayList<>();
        eventDefs.forEach(eventDef -> {
            Event event = new Event();
            event.setId(eventDef.getId());
            event.setName(eventDef.getName());
            event.setExpress(eventDef.getExpress());
            event.setTypes(typeMapping.get(eventDef.getId()));
            events.add(event);
        });
        return events;
    }

    @NotNull
    private static Map<Integer, List<Integer>> getIntegerListMap(List<EventType> eventTypes) {
        Map<Integer,List<Integer>> typeMapping = new HashMap<>();
        for (EventType eventType : eventTypes) {
            // 获取 EventDef 的 ID
            Integer eventDefId = eventType.getId();
            // 根据 EventDef 的 ID 从 typeMapping 中获取对应的 EventType ID 列表
            List<Integer> typeIds = typeMapping.computeIfAbsent(eventDefId, k -> new ArrayList<>());
            // 将当前 EventType 的 ID 添加到对应的列表中
            typeIds.add(eventType.getId());
        }
        return typeMapping;
    }

    @Override
    @Transactional
    public void saveEvent(Event event) {
        List<EventType> eventTypes = new ArrayList<>();
        if  (event.getId() == null) {
            // 批量时可能有事务问题，不管了
            EventDef id = getBaseMapper().selectOne(new QueryWrapper<EventDef>().orderByDesc("id"));
            if (id != null) {
                event.setId(id.getId() + 1);
            } else {
                event.setId(1);
            }
        } else{
            getBaseMapper().delete(new UpdateWrapper<EventDef>().eq("id",event.getId()));
        }
        EventDef eventDef = new EventDef();
        eventDef.setId(event.getId());
        eventDef.setName(event.getName());
        eventDef.setExpress(event.getExpress());
        getBaseMapper().insert(eventDef);
        List<Integer> types = event.getTypes();
        if (types != null && !types.isEmpty()) {
            types.forEach(type -> {
                EventType eventType = new EventType();
                eventType.setType(type);
                eventType.setId(eventDef.getId());
                eventTypes.add(eventType);
            });
            eventTypeMapper.insert(eventTypes);
        }
        eventTypeMapper.insert(eventTypes);
    }


}