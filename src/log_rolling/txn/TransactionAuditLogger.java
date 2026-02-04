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

		if (txnLog.getResult().equals("SUCCESS")) {
			logger.info("txnId={} username={} account={} amount={} result={}", 
						txnLog.getTxnId(),
						txnLog.getUsername(), 
						txnLog.getAccountMasked(), 
						txnLog.getAmount(), 
						txnLog.getResult()
					);
		} else {
			logger.warn("txnId={} username={} account={} amount={} result={}", 
					txnLog.getTxnId(),
					txnLog.getUsername(), 
					txnLog.getAccountMasked(), 
					txnLog.getAmount(), 
					txnLog.getResult()
					);
		}
	}

}
