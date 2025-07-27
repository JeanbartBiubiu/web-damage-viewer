package xyz.game.entity.custom;

import lombok.Data;

@Data
public class JwtBody {
    private String token;
    private Integer canEdit;
    private Integer pay;
}
