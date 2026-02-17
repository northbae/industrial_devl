package ru.tigrbank.service;

import java.io.IOException;

public interface DataImportService {
    void importFromJson(String filePath) throws IOException;
    void importFromCsv(String directoryPath) throws IOException;
    void importFromYaml(String filePath) throws IOException;
}
