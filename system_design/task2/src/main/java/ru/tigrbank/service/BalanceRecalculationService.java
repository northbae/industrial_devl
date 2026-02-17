package ru.tigrbank.service;

import java.math.BigDecimal;
import java.util.UUID;

public interface BalanceRecalculationService {
    BigDecimal recalculateBalance(UUID bankAccountId);
    BigDecimal getDiscrepancy(UUID bankAccountId);
}
