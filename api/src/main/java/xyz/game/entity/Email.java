package xyz.game.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("email")
public class Email implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private String emailAddress;
    private Integer canEdit;
    private Integer pay;
    private String token;
}