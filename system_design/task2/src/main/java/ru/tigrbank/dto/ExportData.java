package ru.tigrbank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExportData {
    private List<Map<String, String>> accounts;
    private List<Map<String, String>> categories;
    private List<Map<String, String>> operations;
}
