package xyz.game.entity;

import java.io.Serializable;

/**
 * (FormulaDefault)实体类
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public class FormulaDefault implements Serializable {
    private static final long serialVersionUID = -78868559267469183L;

    private Integer formulaId;

    private String formulaType;

    private String defaultV;


    public Integer getFormulaId() {
        return formulaId;
    }

    public void setFormulaId(Integer formulaId) {
        this.formulaId = formulaId;
    }

    public String getFormulaType() {
        return formulaType;
    }

    public void setFormulaType(String formulaType) {
        this.formulaType = formulaType;
    }

    public String getDefaultV() {
        return defaultV;
    }

    public void setDefaultV(String defaultV) {
        this.defaultV = defaultV;
    }

}

