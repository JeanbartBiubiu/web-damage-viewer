package xyz.game.function.event.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.game.dao.event.EventTypeMapper;
import xyz.game.function.event.entity.EventType;
import xyz.game.function.event.service.EventTypeService;

@Service
public class EventTypeServiceImpl extends ServiceImpl<EventTypeMapper, EventType> implements EventTypeService {
}