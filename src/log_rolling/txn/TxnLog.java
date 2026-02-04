package log_rolling.txn;


public class TxnLog {
	private String txnId;
	private String username;
	private long amount;
	private String accountNumber;  
	private String result; // SUCCECC/FAIL 

	public TxnLog(String txnId, String username, long amount, String accountNumber, String result) {
		this.txnId = txnId;
		this.username = username;
		this.amount = amount;
		this.accountNumber = accountNumber;
		this.result = result;
	}

	public String getTxnId() {
		return txnId;
	}

	public String getUsername() {
		return username;
	}

	public long getAmount() {
		return amount;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public String getResult() {
		return result;
	}
}
