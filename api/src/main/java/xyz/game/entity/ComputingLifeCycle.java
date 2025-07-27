package xyz.game.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * (ComputingLifeCycle)实体类
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@Data
public class ComputingLifeCycle implements Serializable {
    private static final long serialVersionUID = -91319314651923239L;

    private Integer cycleId;

    private Integer cycleOrder;

    private String cycleType;

    private String cycleCode;

    private String cycleName;

}

