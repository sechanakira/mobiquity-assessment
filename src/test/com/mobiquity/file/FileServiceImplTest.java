package com.mobiquity.file;

import com.mobiquity.exception.APIException;
import com.mobiquity.model.Item;
import com.mobiquity.model.Package;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class FileServiceImplTest {

    private static final String EXAMPLE_INPUT = "example_input";
    private static FileService fileService;

    @BeforeAll
    public static void setUp() {
        fileService = new FileServiceImpl();
    }

    @Test
    public void testReadFile() throws APIException {
        final String filePath = getClass().getClassLoader().getResource(EXAMPLE_INPUT).getPath();
        final List<Package> packages = fileService.readFile(filePath);
        final List<Item> package1Items = new LinkedList<>();
        package1Items.add(new Item(1, 53.38, Double.valueOf(45)));
        package1Items.add(new Item(2, 88.62, Double.valueOf(98)));
        package1Items.add(new Item(3, 78.48, Double.valueOf(3)));
        package1Items.add(new Item(4, 72.30, Double.valueOf(76)));
        package1Items.add(new Item(5, 30.18, Double.valueOf(9)));
        package1Items.add(new Item(6, 46.34, Double.valueOf(48)));
        final Package package1 = new Package(Double.valueOf(81), package1Items);
        assertEquals(4, packages.size());
        assertEquals(package1.getWeight(), packages.get(0).getWeight());
        assertEquals(package1.getItems(), packages.get(0).getItems());
    }
}
