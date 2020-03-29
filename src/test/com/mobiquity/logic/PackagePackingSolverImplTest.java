package com.mobiquity.logic;

import com.mobiquity.model.Item;
import com.mobiquity.model.Package;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PackagePackingSolverImplTest {

    private static PackagePackingSolver solver;

    @BeforeAll
    public static void setUp() {
        solver = new PackagePackingSolverImpl();
    }

    @Test
    public void testSolve() {
        final List<Item> items = new LinkedList<>();
        final Package pkg = new Package(Double.valueOf(81), items);
        items.add(new Item(1, 53.38, Double.valueOf(45)));
        items.add(new Item(2, 88.62, Double.valueOf(98)));
        items.add(new Item(3, 78.48, Double.valueOf(3)));
        items.add(new Item(4, 72.30, Double.valueOf(76)));
        items.add(new Item(5, 30.18, Double.valueOf(9)));
        items.add(new Item(6, 46.34, Double.valueOf(48)));
        final Package solution = solver.solve(pkg);
        assertNotNull(solution);
        assertEquals(Double.valueOf(81), solution.getWeight());
        assertFalse(solution.getItems().isEmpty());
        assertEquals(4, solution.getItems().get(0).getIndex());
    }
}