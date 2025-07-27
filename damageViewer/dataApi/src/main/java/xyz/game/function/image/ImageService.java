package xyz.game.function.image;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ImageService extends IService<Image> {
    List<Image> getImageList();
    Image getImageByURI(String uri);
    void updateImage(Image image);

}
