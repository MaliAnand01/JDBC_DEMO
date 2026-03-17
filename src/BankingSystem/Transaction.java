package BankingSystem;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Model class for Transaction
*/
public class Transaction {
    private final BigDecimal amount;
    private final String type;
    private final Timestamp timestamp;

    public Transaction(BigDecimal amount, String type, Timestamp timestamp) {
        this.amount = amount;
        this.type = type;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format("%s | Amount: Rs.%s | Date: %s", type, amount, timestamp);
    }
}
