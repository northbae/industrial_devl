package ru.tigrbank.service;

import ru.tigrbank.dto.AnalyticsReport;

import java.time.LocalDate;
import java.util.UUID;

public interface AnalyticsService {
    AnalyticsReport getReport(LocalDate from, LocalDate to);
    AnalyticsReport getReportByAccount(UUID bankAccountId, LocalDate from, LocalDate to);
}
