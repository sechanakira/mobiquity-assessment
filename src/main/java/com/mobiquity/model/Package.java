package com.mobiquity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public final class Package {

    private Double weight;
    private List<Item> items;
}
