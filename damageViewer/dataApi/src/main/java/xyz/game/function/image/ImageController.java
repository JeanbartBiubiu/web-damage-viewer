package xyz.game.function.image;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.game.controller.global.DataWithPage;
import xyz.game.controller.global.ResponseData;

import java.util.List;

@RestController
@RequestMapping("/image")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }


    @GetMapping
    public ResponseEntity<ResponseData<DataWithPage<Image>>> query() {
        ResponseData<DataWithPage<Image>> resp = new ResponseData<>();
        DataWithPage<Image> data = new DataWithPage<>();
        List<Image> query = this.imageService.getImageList();
        data.setList(query);
        data.setTotal(data.getList().size());
        resp.setData(data);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{uri}")
    public ResponseEntity<ResponseData<Image>> getImageByUri(@PathVariable("uri") String uri) {
        ResponseData<Image> resp = new ResponseData<>();
        Image result = this.imageService.getImageByURI(uri);
        resp.setData(result);
        return ResponseEntity.ok(resp);
    }

    @PostMapping
    public ResponseEntity<ResponseData<String>> updateImage(@RequestBody Image image) {
        ResponseData<String> resp = new ResponseData<>();
        this.imageService.updateImage(image);
        resp.setData("okk");
        return ResponseEntity.ok(resp);
    }
}
