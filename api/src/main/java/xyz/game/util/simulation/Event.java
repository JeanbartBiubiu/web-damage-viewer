package xyz.game.util.simulation;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class Event {
    private CD cd;
    private Set<Integer> types;
    // 有顺序的;耗蓝也在doing里减掉
    private List<Doing> doings;
    private Double qianyao;
    private Double houyao;
}

@Data
class CD {
    private Double OriginV;
    private Double CurrentV;
    private Double AfterCalculateV;
}

@Data
class Doing{
    // 可能直接该成可以计算的对象
    private String condition;
    private List<String> changeThing;
}
