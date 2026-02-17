package ru.tigrbank.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tigrbank.domain.model.BankAccount;
import ru.tigrbank.domain.model.Category;
import ru.tigrbank.domain.model.Operation;
import ru.tigrbank.dto.ExportData;
import ru.tigrbank.repository.BankAccountRepository;
import ru.tigrbank.repository.CategoryRepository;
import ru.tigrbank.repository.OperationRepository;
import ru.tigrbank.service.DataExportService;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DataExportServiceImpl implements DataExportService {

    private final BankAccountRepository bankAccountRepository;
    private final CategoryRepository categoryRepository;
    private final OperationRepository operationRepository;

    @Override
    public void exportToJson(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(new java.io.File(filePath), buildExportData());
    }

    @Override
    public void exportToYaml(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(new java.io.File(filePath), buildExportData());
    }

    @Override
    public void exportToCsv(String directoryPath) throws IOException {
        Files.createDirectories(Path.of(directoryPath));
        try (CSVWriter w = new CSVWriter(new FileWriter(directoryPath + "/accounts.csv"))) {
            w.writeNext(new String[]{"id", "name", "balance"});
            for (BankAccount a : bankAccountRepository.findAll()) {
                w.writeNext(new String[]{
                        a.getId().toString(), a.getName(), a.getBalance().toPlainString()
                });
            }
        }
        try (CSVWriter w = new CSVWriter(new FileWriter(directoryPath + "/categories.csv"))) {
            w.writeNext(new String[]{"id", "type", "name"});
            for (Category c : categoryRepository.findAll()) {
                w.writeNext(new String[]{
                        c.getId().toString(), c.getType().name(), c.getName()
                });
            }
        }
        try (CSVWriter w = new CSVWriter(new FileWriter(directoryPath + "/operations.csv"))) {
            w.writeNext(new String[]{
                    "id", "type", "bankAccountId", "amount", "categoryId", "date", "description"
            });
            for (Operation o : operationRepository.findAll()) {
                w.writeNext(new String[]{
                        o.getId().toString(),
                        o.getType().name(),
                        o.getBankAccountId().toString(),
                        o.getAmount().toPlainString(),
                        o.getCategoryId().toString(),
                        o.getDate().toString(),
                        o.getDescription() != null ? o.getDescription() : ""
                });
            }
        }
    }

    private ExportData buildExportData() {
        List<Map<String, String>> accounts = new ArrayList<>();
        for (BankAccount a : bankAccountRepository.findAll()) {
            Map<String, String> m = new LinkedHashMap<>();
            m.put("id", a.getId().toString());
            m.put("name", a.getName());
            m.put("balance", a.getBalance().toPlainString());
            accounts.add(m);
        }
        List<Map<String, String>> categories = new ArrayList<>();
        for (Category c : categoryRepository.findAll()) {
            Map<String, String> m = new LinkedHashMap<>();
            m.put("id", c.getId().toString());
            m.put("type", c.getType().name());
            m.put("name", c.getName());
            categories.add(m);
        }
        List<Map<String, String>> operations = new ArrayList<>();
        for (Operation o : operationRepository.findAll()) {
            Map<String, String> m = new LinkedHashMap<>();
            m.put("id", o.getId().toString());
            m.put("type", o.getType().name());
            m.put("bankAccountId", o.getBankAccountId().toString());
            m.put("amount", o.getAmount().toPlainString());
            m.put("categoryId", o.getCategoryId().toString());
            m.put("date", o.getDate().toString());
            m.put("description", o.getDescription() != null ? o.getDescription() : "");
            operations.add(m);
        }
        return new ExportData(accounts, categories, operations);
    }
}
