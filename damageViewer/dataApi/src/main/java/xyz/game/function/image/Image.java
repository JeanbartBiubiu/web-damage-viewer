package xyz.game.function.image;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Image {
    @TableId
    private String uri;
    private String createTime;
    private String updateTime;
    private String image;
}
