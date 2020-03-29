package com.mobiquity.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public final class Item {

    private Integer index;
    private Double weight;
    private Double cost;
}
