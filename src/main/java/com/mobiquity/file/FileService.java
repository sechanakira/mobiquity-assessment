package com.mobiquity.file;

import com.mobiquity.exception.APIException;
import com.mobiquity.model.Package;

import java.util.List;

@FunctionalInterface
public interface FileService {

    List<Package> readFile(final String filePath) throws APIException;
}
