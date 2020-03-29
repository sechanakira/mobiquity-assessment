package com.mobiquity.logic;

import com.mobiquity.model.Package;

@FunctionalInterface
public interface PackagePackingSolver {
    Package solve(final Package pkg);
}
