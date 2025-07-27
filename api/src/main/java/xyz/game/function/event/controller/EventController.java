package xyz.game.function.event.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.game.controller.global.DataWithPage;
import xyz.game.controller.global.ResponseData;
import xyz.game.function.event.entity.Event;
import xyz.game.function.event.service.EventDefService;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {

    private final EventDefService eventDefService;

    public EventController(EventDefService eventDefService) {
        this.eventDefService = eventDefService;
    }

    @GetMapping
    public ResponseEntity<ResponseData<DataWithPage<Event>>> query() {
        ResponseData<DataWithPage<Event>> resp = new ResponseData<>();
        DataWithPage<Event> data = new DataWithPage<>();
        List<Event> query = this.eventDefService.getEventList();
        data.setList(query);
        data.setTotal(data.getList().size());
        resp.setData(data);
        return ResponseEntity.ok(resp);
    }

    @PostMapping
    public ResponseEntity<ResponseData<String>> save(@RequestBody Event req) {
        ResponseData<String> resp = new ResponseData<>();
        this.eventDefService.saveEvent(req);
        resp.setData("okk");
        return ResponseEntity.ok(resp);
    }


}
