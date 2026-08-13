package com.whammich.invasion.wave;

public interface IEntityIMPattern {
    EntityConstruct generateEntityConstruct();

    EntityConstruct generateEntityConstruct(int minAngle, int maxAngle);
}
