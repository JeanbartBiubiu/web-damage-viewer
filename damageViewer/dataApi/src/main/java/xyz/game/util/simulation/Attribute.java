package xyz.game.util.simulation;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Attribute {
    private Value maxValue;
    private Value currentValue;
    // buff结束需要重新计算
    private int reCalculateTime = 0;

}

@Data
class Value {
    private int type;
    private double value;
}
