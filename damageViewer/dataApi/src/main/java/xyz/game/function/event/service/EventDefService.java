package xyz.game.function.event.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.game.entity.PvUv;
import xyz.game.function.event.entity.Event;
import xyz.game.function.event.entity.EventDef;

import java.util.List;

public interface EventDefService extends IService<EventDef> {
    List<Event> getEventList();
    void saveEvent(Event event);
}