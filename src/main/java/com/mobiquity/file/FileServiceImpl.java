package com.mobiquity.file;

import com.mobiquity.exception.APIException;
import com.mobiquity.model.Item;
import com.mobiquity.model.Package;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public final class FileServiceImpl implements FileService {

    private static final Logger LOGGER = Logger.getLogger(FileServiceImpl.class.getName());

    @Override
    public final List<Package> readFile(final String filePath) throws APIException {
        LOGGER.log(Level.INFO, "Reading from file: {}", filePath);
        final Path path = Paths.get(filePath);
        final List<Package> packages = new LinkedList<>();
        try (final Stream<String> stream = Files.lines(path)) {
            stream.forEach(line -> packages.add(readPackage(line)));
        } catch (IOException ex) {
            throw new APIException("Exception occurred reading file: " + filePath, ex);
        }
        return packages;
    }

    private final Package readPackage(final String line) {
        List<Item> items = new LinkedList<>();
        final String[] lineParts = line.split(":");
        final Double weight = Double.parseDouble(lineParts[0].trim());
        Arrays.stream(lineParts[1].trim().split(" ")).forEach(item -> {
            String[] itemParts = item.replace('(', ' ')
                    .replace(')', ' ')
                    .split(",");
            final Integer index = Integer.parseInt(itemParts[0].trim());
            final Double itemWeight = Double.parseDouble(itemParts[1].trim());
            final Double cost = Double.parseDouble(itemParts[2].substring(1).trim());
            items.add(new Item(index, itemWeight, cost));
        });
        return new Package(weight, items);
    }
}
