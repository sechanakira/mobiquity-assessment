package com.mobiquity.packer;

import com.mobiquity.exception.APIException;
import com.mobiquity.model.Item;
import com.mobiquity.model.Package;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.mobiquity.constant.ApplicationConstants.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PackerTest {

    private static final String EXAMPLE_OUTPUT = "example_output";
    private static final String EXAMPLE_INPUT = "example_input";
    private static final String FAKE_PATH = "/fake/path";

    @Test
    public void testPack() throws APIException {
        String expectedOutput = "";
        final String filePath = getClass().getClassLoader().getResource(EXAMPLE_INPUT).getPath();
        try (Stream<String> stream = Files.lines(Paths.get(getClass().getClassLoader().getResource(EXAMPLE_OUTPUT).getPath()))) {
            expectedOutput = stream.collect(Collectors.joining(System.getProperty(LINE_SEPARATOR)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        final String output = Packer.pack(filePath);
        assertNotNull(output);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testPackWithFakePathAndExpectException() {
        final String fakePath = FAKE_PATH;
        assertThatThrownBy(() ->
                Packer.pack(fakePath)
        ).isInstanceOf(APIException.class)
                .hasMessageContaining("Exception occurred reading file: " + fakePath);
    }

    @Test
    public void testCheckConstraintsWithViolationsAndExpectExceptions() {
        final String path = "/demo/path";

        assertThatThrownBy(() -> {
            final List<Package> packages = Collections.emptyList();
            Packer.checkConstraints(packages, path);
        }).isInstanceOf(APIException.class)
                .hasMessageContaining(NO_PACKAGES_COULD_BE_READ_FOR_PATH + path);

        assertThatThrownBy(() -> {
            final List<Package> packages = new LinkedList<>();
            final Package pkg = new Package(Double.valueOf(1000), Collections.emptyList());
            packages.add(pkg);
            Packer.checkConstraints(packages, path);
        }).isInstanceOf(APIException.class)
                .hasMessageContaining(MAX_WEIGHT_THAT_A_PACKAGE_CAN_TAKE_IS_100);

        assertThatThrownBy(() -> {
            final List<Package> packages = new LinkedList<>();
            final List<Item> items = new LinkedList<>();
            IntStream.rangeClosed(1, 50).forEach(i ->
                    items.add(new Item(1, 1.01, Double.valueOf(45)))
            );
            final Package pkg = new Package(Double.valueOf(10), items);
            packages.add(pkg);
            Packer.checkConstraints(packages, path);
        }).isInstanceOf(APIException.class)
                .hasMessageContaining(MAX_ITEMS_ALLOWED_IN_A_PACKAGE_EXCEEDED);

        assertThatThrownBy(() -> {
            final List<Package> packages = new LinkedList<>();
            final List<Item> items = new LinkedList<>();
            items.add(new Item(1, 101.01, Double.valueOf(45)));
            final Package pkg = new Package(Double.valueOf(10), items);
            packages.add(pkg);
            Packer.checkConstraints(packages, path);
        }).isInstanceOf(APIException.class)
                .hasMessageContaining(MAX_WEIGHT_AND_COST_OF_AN_ITEM_IS_100);

        assertThatThrownBy(() -> {
            final List<Package> packages = new LinkedList<>();
            final List<Item> items = new LinkedList<>();
            items.add(new Item(1, 10.01, Double.valueOf(450)));
            final Package pkg = new Package(Double.valueOf(10), items);
            packages.add(pkg);
            Packer.checkConstraints(packages, path);
        }).isInstanceOf(APIException.class)
                .hasMessageContaining(MAX_WEIGHT_AND_COST_OF_AN_ITEM_IS_100);
    }
}