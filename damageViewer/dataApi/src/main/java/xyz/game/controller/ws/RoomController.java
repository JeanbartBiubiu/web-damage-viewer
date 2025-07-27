package xyz.game.controller.ws;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.game.controller.global.DataWithPage;
import xyz.game.controller.global.ResponseData;
import xyz.game.entity.custom.AttributeReq;

import java.util.List;
import java.util.Set;

@RestController
public class RoomController {

    private final WebRTCSignalingHandler webRTCSignalingHandler;

    public RoomController(WebRTCSignalingHandler webRTCSignalingHandler) {
        this.webRTCSignalingHandler = webRTCSignalingHandler;
    }

    @GetMapping("/rooms")
    public ResponseEntity<ResponseData<List<String>>> getRoomIds() {
        Set<String> roomIds = webRTCSignalingHandler.getRoomIds();
        ResponseData<List<String>> response = new ResponseData<>();
        response.setData(roomIds.stream().toList());
        return ResponseEntity.ok(response);
    }
}