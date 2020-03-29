package com.mobiquity.logic;

import com.mobiquity.model.Item;
import com.mobiquity.model.Package;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public final class PackagePackingSolverImpl implements PackagePackingSolver {

    @Override
    public Package solve(final Package pkg) {
        final List<Item> solutionItems = new LinkedList<>();
        final List<Item> items = pkg.getItems();
        final Double capacity = pkg.getWeight();
        items.sort(Comparator.comparing(Item::getCost).reversed().thenComparing(Item::getWeight));
        for (Item item : items) {
            if (solutionItems.isEmpty() && item.getWeight() <= capacity) {
                solutionItems.add(item);
            } else {
                if (solutionItems.stream().collect(Collectors.summingDouble(Item::getWeight)) + item.getWeight() <= capacity) {
                    solutionItems.add(item);
                }
            }
        }
        return new Package(capacity, solutionItems);
    }
}
