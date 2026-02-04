package log_rolling.txn;

import java.util.UUID;

public class Test {

	public static void main(String[] args) throws InterruptedException {
		TransactionAuditLogger logger = new TransactionAuditLogger();

		System.out.println("=== 로그 롤링 테스트 시작 ===");
		System.out.println("목표: 10MB 파일 생성으로 용량 기반 롤링 테스트");

		int count = 0;
		for (int i = 1; i <= 100_000; i++) {
			TxnLog t = randomTxn(i);
			logger.log(t); 

			count++;
			if (count % 10000 == 0) {
				System.out.println(count + "건 로그 기록 완료");
			}
		}

		System.out.println("\n=== 테스트 완료 ===");
		System.out.println("총 " + count + "건 로그 기록");
		System.out.println("\n확인사항:");
		System.out.println("1. logs/txn/txn.log 파일 생성 확인");
		System.out.println("2. logs/txn/archive/ 폴더에 롤링된 파일 확인 (.gz)");
		System.out.println("3. 파일 크기가 10MB 근처에서 분할되었는지 확인");
		System.out.println("4. 로그 내 계좌번호가 마스킹 처리되었는지 확인");
	}

	private static TxnLog randomTxn(int i) {
		String txnId = UUID.randomUUID().toString();
		String username = "user" + (i % 10);
		long amount = (long) (Math.random() * 100_000) + 1;

		String accountNumber = "3333" + "-" + (int) (Math.random() * 1_000_000 + 1) + "-"
				+ (int) (Math.random() * 1_000_000 + 1);

		boolean success = Math.random() < 0.8;
		String result = success ? "SUCCESS" : "FAIL";

		return new TxnLog(txnId, username, amount, accountNumber, // 원본 전달
				result);
	}

}
