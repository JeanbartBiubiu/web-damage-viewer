package xyz.game.function.image;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.game.dao.ImageDao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ImageServiceImpl  extends ServiceImpl<ImageDao, Image> implements ImageService {
    private final ImageDao imageDao;

    public ImageServiceImpl(ImageDao imageDao) {
        this.imageDao = imageDao;
    }

    @Override
    public List<Image> getImageList() {
        return imageDao.selectList(new QueryWrapper<Image>());
    }

    @Override
    public Image getImageByURI(String uri) {
        return imageDao.selectOne(new QueryWrapper<Image>().eq("uri", uri));
    }

    @Override
    public void updateImage(Image image) {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 将当前时间格式化为符合 datetime 类型的字符串
        String formattedTime = now.format(formatter);
        image.setUpdateTime(formattedTime);
        imageDao.insertOrUpdate(image);
    }
}
