package com.mobiquity.packer;

import com.mobiquity.exception.APIException;
import com.mobiquity.file.FileService;
import com.mobiquity.file.FileServiceImpl;
import com.mobiquity.logic.PackagePackingSolver;
import com.mobiquity.logic.PackagePackingSolverImpl;
import com.mobiquity.model.Item;
import com.mobiquity.model.Package;

import java.util.List;
import java.util.stream.Collectors;

import static com.mobiquity.constant.ApplicationConstants.*;

public class Packer {

    private static final FileService fileService = new FileServiceImpl();
    private static final PackagePackingSolver solver = new PackagePackingSolverImpl();

    private Packer() {
    }

    public static String pack(final String filePath) throws APIException {
        final StringBuilder result = new StringBuilder();
        final List<Package> packages = fileService.readFile(filePath);
        checkConstraints(packages, filePath);
        packages.forEach(pkg -> {
            final Package solution = solver.solve(pkg);
            if (solution.getItems().isEmpty()) {
                result.append(EMPTY_RESULT);
            } else {
                final String output = solution.getItems().stream()
                        .map(item -> item.getIndex().toString())
                        .collect(Collectors.joining(DELIMITER));
                result.append(output);
            }
            result.append(System.getProperty(LINE_SEPARATOR));
        });
        return result.toString().trim();
    }

    private static void checkConstraints(final List<Package> packages, final String filePath) throws APIException {
        if (packages.isEmpty()) {
            throw new APIException(NO_PACKAGES_COULD_BE_READ_FOR_PATH + filePath);
        }
        for (Package pkg : packages) {
            if (pkg.getWeight() > MAX_WEIGHT) {
                throw new APIException(MAX_WEIGHT_THAT_A_PACKAGE_CAN_TAKE_IS_100);
            }
            if (pkg.getItems().size() > MAX_SIZE) {
                throw new APIException(MAX_ITEMS_ALLOWED_IN_A_PACKAGE_EXCEEDED);
            }
            for (Item item : pkg.getItems()) {
                if (item.getWeight() > MAX_WEIGHT || item.getCost() > MAX_COST) {
                    throw new APIException(MAX_WEIGHT_AND_COST_OF_AN_ITEM_IS_100);
                }
            }
        }
    }

}
