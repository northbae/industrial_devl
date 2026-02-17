package ru.tigrbank.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tigrbank.domain.model.*;
import ru.tigrbank.dto.ExportData;
import ru.tigrbank.repository.BankAccountRepository;
import ru.tigrbank.repository.CategoryRepository;
import ru.tigrbank.repository.OperationRepository;
import ru.tigrbank.service.DataImportService;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DataImportServiceImpl implements DataImportService {

    private final BankAccountRepository bankAccountRepository;
    private final CategoryRepository categoryRepository;
    private final OperationRepository operationRepository;

    @Override
    public void importFromJson(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        importData(mapper.readValue(new java.io.File(filePath), ExportData.class));
    }

    @Override
    public void importFromYaml(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        importData(mapper.readValue(new java.io.File(filePath), ExportData.class));
    }

    @Override
    public void importFromCsv(String directoryPath) throws IOException {
        try {
            try (CSVReader r = new CSVReader(new FileReader(directoryPath + "/accounts.csv"))) {
                List<String[]> rows = r.readAll();
                for (int i = 1; i < rows.size(); i++) {
                    String[] row = rows.get(i);
                    bankAccountRepository.save(new BankAccount(
                            UUID.fromString(row[0]), row[1], new BigDecimal(row[2])
                    ));
                }
            }
            try (CSVReader r = new CSVReader(new FileReader(directoryPath + "/categories.csv"))) {
                List<String[]> rows = r.readAll();
                for (int i = 1; i < rows.size(); i++) {
                    String[] row = rows.get(i);
                    categoryRepository.save(new Category(
                            UUID.fromString(row[0]), OperationType.valueOf(row[1]), row[2]
                    ));
                }
            }
            try (CSVReader r = new CSVReader(new FileReader(directoryPath + "/operations.csv"))) {
                List<String[]> rows = r.readAll();
                for (int i = 1; i < rows.size(); i++) {
                    String[] row = rows.get(i);
                    operationRepository.save(new Operation(
                            UUID.fromString(row[0]),
                            OperationType.valueOf(row[1]),
                            UUID.fromString(row[2]),
                            new BigDecimal(row[3]),
                            UUID.fromString(row[4]),
                            LocalDate.parse(row[5]),
                            row.length > 6 && !row[6].isEmpty() ? row[6] : null
                    ));
                }
            }
        } catch (CsvException e) {
            throw new IOException("Ошибка чтения CSV: " + e.getMessage(), e);
        }
    }

    private void importData(ExportData data) {
        if (data.getAccounts() != null) {
            for (Map<String, String> m : data.getAccounts()) {
                bankAccountRepository.save(new BankAccount(
                        UUID.fromString(m.get("id")),
                        m.get("name"),
                        new BigDecimal(m.get("balance"))
                ));
            }
        }
        if (data.getCategories() != null) {
            for (Map<String, String> m : data.getCategories()) {
                categoryRepository.save(new Category(
                        UUID.fromString(m.get("id")),
                        OperationType.valueOf(m.get("type")),
                        m.get("name")
                ));
            }
        }
        if (data.getOperations() != null) {
            for (Map<String, String> m : data.getOperations()) {
                String desc = m.get("description");
                operationRepository.save(new Operation(
                        UUID.fromString(m.get("id")),
                        OperationType.valueOf(m.get("type")),
                        UUID.fromString(m.get("bankAccountId")),
                        new BigDecimal(m.get("amount")),
                        UUID.fromString(m.get("categoryId")),
                        LocalDate.parse(m.get("date")),
                        desc != null && !desc.isEmpty() ? desc : null
                ));
            }
        }
    }
}
