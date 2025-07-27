package xyz.game.entity;

import java.io.Serializable;

/**
 * (FormulaImpl)实体类
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public class FormulaImpl implements Serializable {
    private static final long serialVersionUID = -66376987437664877L;

    private Integer attributeId;

    private Integer formulaId;

    private String formulaImpl;


    public Integer getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Integer attributeId) {
        this.attributeId = attributeId;
    }

    public Integer getFormulaId() {
        return formulaId;
    }

    public void setFormulaId(Integer formulaId) {
        this.formulaId = formulaId;
    }

    public String getFormulaImpl() {
        return formulaImpl;
    }

    public void setFormulaImpl(String formulaImpl) {
        this.formulaImpl = formulaImpl;
    }

}

