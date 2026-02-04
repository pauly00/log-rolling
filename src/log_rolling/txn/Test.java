package log_rolling.txn;

import java.util.UUID;

public class Test {

	public static void main(String[] args) throws InterruptedException {
		TransactionAuditLogger logger = new TransactionAuditLogger();
		
		for (int i = 1; i <= 5000; i++) {
            TxnLog t = randomTxn(i);
            logger.log(t);
            Thread.sleep(5);
        }
		

        System.out.println("Done. Check logs/txn/txn.log and archive folder.");

	}
	
	private static TxnLog randomTxn(int i) {
	    String txnId = UUID.randomUUID().toString();
	    String username = "user" + (i % 10);

	    long amount = (long) (Math.random() * 100_000) + 1;

	    String rawAccount = "3333" + "-" + (int)(Math.random() * 1_000_000 + 1) + "-" + (int)(Math.random() * 1_000_000 + 1);
	    String accountMasked = maskAccount(rawAccount);

	    boolean success = Math.random() < 0.8;
	    String result = success ? "SUCCESS" : "FAIL";

	    return new TxnLog(
	            txnId,
	            username,
	            amount,
	            accountMasked,
	            result
	    );
	}

	private static String maskAccount(String raw) {
        String[] parts = raw.split("-");

        String masked = parts[0] + "-" + "*".repeat(parts[1].length()) + "-" + parts[2];
        return masked;
    }
	


}
