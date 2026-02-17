package ru.tigrbank.service;

import java.io.IOException;

public interface DataExportService {
    void exportToJson(String filePath) throws IOException;
    void exportToCsv(String directoryPath) throws IOException;
    void exportToYaml(String filePath) throws IOException;
}
