package log_rolling.txn;

public class TxnLog {
	private String txnId; // UUID
	private String username; // 이미 넣었다고 했으니 OK
	private long amount; // 랜덤
	private String accountMasked; // 12****34
	private String result; // SUCCESS / FAIL

	public String getTxnId() {
		return txnId;
	}

	public String getUsername() {
		return username;
	}

	public long getAmount() {
		return amount;
	}

	public String getAccountMasked() {
		return accountMasked;
	}

	public String getResult() {
		return result;
	}


	public TxnLog(String txnId, String username, long amount, String accountMasked, String result) {
		super();
		this.txnId = txnId;
		this.username = username;
		this.amount = amount;
		this.accountMasked = accountMasked;
		this.result = result;
	}

}
