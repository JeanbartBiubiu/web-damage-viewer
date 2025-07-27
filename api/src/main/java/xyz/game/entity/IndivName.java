package xyz.game.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * (IndivName)实体类
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@Data
public class IndivName implements Serializable {
    private static final long serialVersionUID = -75171202507033402L;

    private Integer indivId;

    private Integer languageId;

    private String indivName;

    private String indivImg;

}

