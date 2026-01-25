SELECT * FROM accounts
    WHERE status = 'active' AND currency = 'EUR' AND opened_at > '2024-01-01'
    ORDER BY opened_at ASC;

SELECT a.full_name, b.account_type, b.currency, b.status
    FROM clients a
    JOIN accounts b on a.client_id = b.client_id;

SELECT a.full_name, COUNT(b.account_id) AS accounts_count
    FROM clients a
    LEFT JOIN accounts b ON a.client_id = b.client_id
    GROUP BY a.client_id;

SELECT a.full_name, COUNT(b.account_id) AS active_accounts_count
    FROM clients a
    JOIN accounts b ON a.client_id = b.client_id
    WHERE b.status = 'active'
    GROUP BY a.client_id
    HAVING COUNT(b.account_id) > 2;

WITH account_incoming AS (
    SELECT account_id, SUM(amount) AS total_incoming
        FROM transactions
        WHERE txn_type IN ('deposit', 'transfer_in')
        GROUP BY account_id)
SELECT a.account_id, a.client_id, a.account_type, ai.total_incoming
    FROM accounts a
    JOIN account_incoming ai ON a.account_id = ai.account_id
    WHERE ai.total_incoming > (SELECT AVG(total_incoming) FROM account_incoming)
    ORDER BY ai.total_incoming DESC;

SELECT c.full_name, SUM(t.amount) AS total_turnover
    FROM clients c
    JOIN accounts a ON c.client_id = a.client_id
    JOIN transactions t ON a.account_id = t.account_id
    WHERE t.txn_date >= '2025-01-01'
        AND t.txn_date < '2026-01-01'
    GROUP BY c.client_id
    ORDER BY total_turnover DESC
    LIMIT 5;

SELECT c.full_name, COUNT(t.transaction_id) AS txn_count,
    CASE
        WHEN COUNT(t.transaction_id) = 0 THEN 'inactive'
        WHEN COUNT(t.transaction_id) BETWEEN 1 AND 5 THEN 'low'
        WHEN COUNT(t.transaction_id) BETWEEN 6 AND 20 THEN 'medium'
        ELSE 'high'
    END AS activity_level
    FROM clients c
    LEFT JOIN accounts a ON c.client_id = a.client_id
    LEFT JOIN transactions t ON a.account_id = t.account_id
        AND t.txn_date >= CURRENT_DATE - INTERVAL '90 days'
    GROUP BY c.client_id
    ORDER BY txn_count DESC;

SELECT l.loan_id, l.client_id, l.principal, COALESCE(SUM(lp.amount), 0) AS total_paid,
    ROUND(COALESCE(SUM(lp.amount), 0) / l.principal * 100, 2) AS paid_percent
    FROM loans l
    LEFT JOIN loan_payments lp ON l.loan_id = lp.loan_id
        AND lp.status = 'success'
    GROUP BY l.loan_id
    HAVING COALESCE(SUM(lp.amount), 0) < l.principal * 0.5
    ORDER BY paid_percent ASC;

SELECT c.full_name, a.account_id, cr.card_id, cr.card_type, cr.expires_at
    FROM cards cr
    JOIN accounts a ON cr.account_id = a.account_id
    JOIN clients c ON a.client_id = c.client_id
    WHERE cr.status = 'active'
    ORDER BY c.full_name, cr.expires_at;

SELECT a.account_id, a.account_type, a.currency, COUNT(t.transaction_id) AS txn_count,
    COALESCE(SUM(
        CASE
            WHEN t.txn_type IN ('withdrawal', 'transfer_out', 'fee')
            THEN t.amount
        END
    ), 0) AS total_withdrawals
    FROM accounts a
    LEFT JOIN transactions t ON a.account_id = t.account_id
    GROUP BY a.account_id
    ORDER BY a.account_id;