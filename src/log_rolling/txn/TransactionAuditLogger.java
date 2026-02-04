package log_rolling.txn;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionAuditLogger {
	private final Logger logger;

	public TransactionAuditLogger() {
		this.logger = LoggerFactory.getLogger("TXN_LOGGER");
	}

	public void log(TxnLog txnLog) {
		String maskedAccount = maskAccount(txnLog.getAccountNumber());
		
		if (txnLog.getResult().equals("SUCCESS")) {
			logger.info("txnId={} username={} account={} amount={} result={}", 
						txnLog.getTxnId(),
						txnLog.getUsername(), 
						maskedAccount,  
						txnLog.getAmount(), 
						txnLog.getResult()
					);
		} else {
			logger.warn("txnId={} username={} account={} amount={} result={}", 
					txnLog.getTxnId(),
					txnLog.getUsername(), 
					maskedAccount,  
					txnLog.getAmount(), 
					txnLog.getResult()
					);
		}
	}
	
	// 계좌번호 마스킹 처리
	private String maskAccount(String accountNumber) {
		if (accountNumber == null || accountNumber.isEmpty()) {
			return "****";
		}
		
		String[] parts = accountNumber.split("-");
		// 우리가 기대한 형식과 다르다면
		if (parts.length != 3) {
			return "****-****-****";
		}
		
		return parts[0] + "-" + "*".repeat(parts[1].length()) + "-" + parts[2];
	}
}
